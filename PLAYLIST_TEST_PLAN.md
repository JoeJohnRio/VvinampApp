# Playlist Test Plan

Covers the playlist page and everything reachable from it: layout and scrolling, Name & details (cover upload included), privacy, delete, adding songs from search and recommendations, playback, sorting and editing.

Main target is the **web build** (what a reviewer is most likely to open). The cases marked **[all]** should also be run on Android and iOS.

Status column: what the code suggests will happen **today**, before any fixes. `FAIL` rows are the bugs to fix; `?` rows haven't been checked yet.

---

## 1. Problems found while reading the code

These shaped the cases below. File links point to where each one lives.

| # | Problem | Where |
|---|---|---|
| P1 | Web image picker never reads the file. It returns the fixed text `"wasm-image-picked"`, so the preview has nothing to load (the blank cover in the sheet). | [ImagePicker.wasmJs.kt:19](composeApp/src/wasmJsMain/kotlin/com/example/vvinamp/util/ImagePicker.wasmJs.kt:19) |
| P2 | Web upload sends an empty file: `readBytesFromUri` returns `ByteArray(0)`. iOS has the same two gaps. Only Android reads real bytes. | [ImageUtils.wasmJs.kt:5](composeApp/src/wasmJsMain/kotlin/com/example/vvinamp/util/ImageUtils.wasmJs.kt:5), [ImagePicker.ios.kt:24](composeApp/src/iosMain/kotlin/com/example/vvinamp/util/ImagePicker.ios.kt:24), [ImageUtils.kt (iOS):5](composeApp/src/iosMain/kotlin/com/example/vvinamp/util/ImageUtils.kt:5) |
| P3 | No upload progress. The only feedback is the Save text turning into "Saving...". The upload is one GraphQL mutation with no progress callback. Content type is always sent as `image/jpeg`. | [PlaylistViewModel.kt:222](composeApp/src/commonMain/kotlin/com/example/vvinamp/ui/home/library/detailplaylist/PlaylistViewModel.kt:222) |
| P4 | "Add to this playlist" opens search without saying which playlist. So search can't add straight to it: YouTube ⊕ opens a picker of all your playlists, and catalog ⊕ and ⋮ have empty click handlers (they do nothing). | [BottomNavRouter.kt:35](composeApp/src/commonMain/kotlin/com/example/vvinamp/navigation/router/BottomNavRouter.kt:35), [SearchViewModel.kt:79](composeApp/src/commonMain/kotlin/com/example/vvinamp/ui/search/SearchViewModel.kt:79), [SearchItemComponent.kt:233](composeApp/src/commonMain/kotlin/com/example/vvinamp/ui/search/searchresult/SearchItemComponent.kt:233) |
| P5 | Any error on the playlist page (recommendations failing, privacy toggle failing, save failing) sets `uiState.error`, and `ScreenWrapper` then replaces the whole page with the error text, with no Retry button. | [PlaylistScreen.kt (ScreenWrapper call)](composeApp/src/commonMain/kotlin/com/example/vvinamp/ui/home/library/detailplaylist/PlaylistScreen.kt), [ScreenWrapper.kt:42](composeApp/src/commonMain/kotlin/com/example/vvinamp/ui/main/ScreenWrapper.kt:42) |
| P6 | The empty-playlist header shows the pencil cover, "Change" and "Add to this playlist" to everyone, not only the playlist's creator. | [PlaylistDetailComponent.kt](composeApp/src/commonMain/kotlin/com/example/vvinamp/ui/home/library/detailplaylist/PlaylistDetailComponent.kt) |
| P7 | "Has songs" is decided by `totalDuration > 0`. A playlist whose only songs report 0 duration (possible for YouTube items) shows the empty layout. | [PlaylistScreen.kt](composeApp/src/commonMain/kotlin/com/example/vvinamp/ui/home/library/detailplaylist/PlaylistScreen.kt) |
| P8 | Cover-colored gradient doesn't show on web (seen during the last check); the page stays dark grey. | `ExtractDominantColorEffect` |

### Status after the 2026-11-22 fixes (checked on web)

| # | Now | Checked |
|---|---|---|
| P1 | Fixed on web: picker hands over the real file, preview shows it | C7 on localhost |
| P2 | Fixed on web: real bytes uploaded, type read from the file (JPG/PNG/WebP, max 10 MB). iOS still can't read picked images; it now says "Couldn't read that image" instead of uploading nothing | C7, C9 (bad file rejected) |
| P3 | Spinner over the cover while uploading; "Cover updated" on success; failure keeps the sheet open with the message | Spinner and failure seen. Success not seen: the server's storage upload fails, see below |
| P4 | Fixed: search opened from a playlist shows "Adding to …" and both tabs add straight in, with a spinner then a tick. Search opened from the bottom bar opens the picker for both tabs | E1, E2, E3, E7 |
| P5 | Fixed: only a failed first load shows the error screen (now with Retry). Save, upload, add, privacy and delete failures show a message and keep the page | C10 |
| P6 | Fixed: someone else's empty playlist shows a music note, no "Change", no "Add to this playlist" | Not checked (no second account) |
| Duplicate | Adding a song that's already there shows "Already in …" and a tick, from search, the picker and Recommended | E5 via search and picker |

**Blocking C8 success:** the backend's cover upload fails with `supabase storage upload error (HTTP 400): {"statusCode":"403","error":"Unauthorized","message":"Invalid Compact JWS"}`. That's `SUPABASE_SERVICE_ROLE_KEY` on the server ([storage.go](../vvinamp-backend/internal/pkg/supabase/storage.go)) being sent as a Bearer token but not being a valid JWT. Fix the key in the server's environment, then rerun C8.

---

## 2. Setup

1. Build and serve the web app:
   ```bash
   JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home ./gradlew :composeApp:wasmJsBrowserDistribution
   ```
   then start `web-dist` (serves `composeApp/build/dist/wasmJs/productionExecutable` on http://localhost:8080).
2. Sign in with **Try as Guest (Demo)**.
3. Test data to have ready:
   - **Empty**: a new playlist with no songs.
   - **One catalog song**: playlist with one song added from the catalog tab.
   - **One YouTube song**: playlist with one song added from the YouTube tab.
   - **Big**: playlist with 4+ different albums (mosaic cover) and 50+ songs (paging).
   - **Not mine**: a public playlist created by another account.
   - Two images: a normal JPG under 1 MB, and a PNG over 5 MB.

---

## 3. Manual test cases

### A. Opening the page

| ID | Steps | Expected | Today |
|---|---|---|---|
| A1 [all] | Open **Empty** from Library | Grey background, pencil cover below the top bar, title with "Change", creator row, globe, share, white "Add to this playlist", Recommended Songs | PASS |
| A2 [all] | Open **Big** | Hero cover (mosaic), title, creator, duration, action row, songs, Recommended Songs | PASS (gradient: see A7) |
| A3 | Open a playlist on a slow connection (DevTools throttling) | Spinner, then content. No flash of the empty layout before songs appear | ? (null playlist renders empty layout first) |
| A4 | Open a playlist id that doesn't exist (edit the URL hash) | Clear error with a way back or Retry | FAIL (P5: error text, no Retry) |
| A5 | Open **Big**, go back, open **Empty** right away | **Empty** shows, never **Big**'s title or songs | ? |
| A6 | Back arrow | Returns to the previous screen | ? |
| A7 | Open a playlist with a colorful cover | Top of page tinted with the cover color | FAIL on web (P8) |
| A8 | Open **One YouTube song** | Song layout, not the empty layout | ? (P7) |

### B. Scrolling and top bar

| ID | Steps | Expected | Today |
|---|---|---|---|
| B1 | **Empty**: scroll slowly (trackpad) | Bar fades in, fully solid when the bar reaches the middle of the cover; title slides in at the end | PASS |
| B2 | **Big**: scroll slowly | Bar fully solid when the header's bottom reaches the bar; hero cover shrinks and fades | PASS |
| B3 | At top, then scrolled | Dark circles behind back and ⋮ at top; gone once the bar is solid | PASS |
| B4 | Scroll back to the top | Bar clear again, circles back, title hidden | PASS |
| B5 | Long title (40+ chars) | Title in the bar ends with "…", never overlaps the buttons | ? |
| B6 | Phone width (375px) and wide window | No horizontal scroll, header doesn't clip | ? |

### C. Name & details and cover upload

| ID | Steps | Expected | Today |
|---|---|---|---|
| C1 | Open the sheet three ways: "Change", the pencil cover, "Name & details" row (**Big**) | Same sheet, name and description prefilled, current cover shown | ? |
| C2 | Open the sheet, change nothing | Save is grey and does nothing | PASS |
| C3 [all] | Rename, Save | Sheet closes; new name in header, top bar and Library | ? |
| C4 | Add a description, Save, reopen | Description kept | ? |
| C5 | Edit name, Cancel, reopen | Original name back | ? |
| C6 | Clear the name, Save | Blocked with a message (empty name not allowed) | ? (no check in code) |
| C7 [all] | Pencil on cover, pick the JPG | Picked image shows in the sheet right away; Save turns green | FAIL on web and iOS (P1) |
| C8 [all] | After C7, Save | Progress shown on the cover while uploading; on success the sheet closes and the new cover shows in the sheet, header, hero and Library | FAIL (P2 empty file on web/iOS, P3 no progress) |
| C9 | Pick the 5 MB PNG, Save | Uploads with correct type, or a clear "too large" message | ? (always sent as `image/jpeg`) |
| C10 | Make the upload fail (DevTools offline right after Save) | Progress stops, error message in the sheet, sheet stays open with the picked image, Save works again | FAIL (P5: page behind turns into the error screen) |
| C11 | Open picker, cancel it | Nothing changes, Save stays grey | ? |
| C12 | Click Save twice fast during upload | One upload only | PASS (Save disabled while saving) |
| C13 | Change cover, then change it again to the same file | Shows the new cover, not a cached old one | ? |

### D. Privacy and delete

| ID | Steps | Expected | Today |
|---|---|---|---|
| D1 | Sheet: "Make private" | Header icon becomes a lock; row now says "Make public" with a globe | ? |
| D2 | "Make public" | Reverse of D1 | ? |
| D3 | Privacy toggle while offline | Short error message; page stays usable | FAIL (P5) |
| D4 | "Delete playlist", then Cancel | Dialog closes, playlist kept | PASS |
| D5 [all] | "Delete playlist", then Delete | Toast "Playlist deleted", back to Library, playlist gone from Library | ? |
| D6 | Delete while offline | Error message, playlist kept, page usable | FAIL (P5) |

### E. Adding songs

| ID | Steps | Expected | Today |
|---|---|---|---|
| E1 [all] | **Empty**: "Add to this playlist" | Search opens, set up to add to **Empty** (for example "Adding to Empty" shown at the top) | FAIL (P4) |
| E2 [all] | From E1, catalog tab, ⊕ on a song | Song added to **Empty** directly, no picker; feedback ("Added to Empty") and the ⊕ turns into a tick | FAIL (P4: button does nothing) |
| E3 [all] | From E1, YouTube tab, ⊕ on a video | Same as E2, no list of other playlists | FAIL (P4: opens picker of all playlists) |
| E4 | After E2 or E3, go back | Playlist now in the song layout with the new song listed | ? |
| E5 | Add the same song twice | Second time says it's already there, or the button is already a tick | ? |
| E6 | **Big**: Add from the action row | Same behaviour as E1 to E3 | FAIL (P4) |
| E7 | Search from the bottom bar (no playlist), ⊕ on catalog and on YouTube | Both open the same playlist picker; picking one adds the song | FAIL (catalog ⊕ does nothing) |
| E8 | Recommended Songs: ⊕ | Tick appears, song added to the list above, recommendation stays with its tick | ? |
| E9 | Reopen the playlist | Songs already in the playlist aren't recommended | PASS (only checks loaded pages) |

### F. Playback

| ID | Steps | Expected | Today |
|---|---|---|---|
| F1 [all] | Green play button | Plays from the first song; player shows "Playing from playlist" with the title | ? |
| F2 | Shuffle | Icon turns green; playback order shuffled | ? |
| F3 | Tap the 3rd song | Plays from the 3rd song, queue continues with the playlist | ? |
| F4 | Tap a recommended song | Plays it | ? |
| F5 | Play a YouTube song in the playlist | Plays | ? |

### G. Song list, sort and edit

| ID | Steps | Expected | Today |
|---|---|---|---|
| G1 | **Big**: scroll to the end | More songs load, no duplicates, placeholders while loading | ? |
| G2 | Sort: try each option | List reorders each time; chosen option marked | ? |
| G3 | Edit: drag a song to a new spot, Save | Order kept after reopening (custom sort) | ? |
| G4 | Edit: remove a song, Save | Song gone, duration updated; removing the last song switches to the empty layout | ? |
| G5 | Song row ⋮, top bar ⋮, Share | "Coming soon" message (placeholder, acceptable for now) | PASS |

### H. Someone else's playlist

| ID | Steps | Expected | Today |
|---|---|---|---|
| H1 | Open **Not mine** (with songs) | No Add/Edit/Sort/Name & details row | PASS (row is creator-only) |
| H2 | Open an empty playlist that isn't yours | No pencil cover, no "Change", no "Add to this playlist" | FAIL (P6) |

---

## 4. Automated tests to add

`PlaylistViewModel` has no tests yet. `FakePlaylistRepository` already exists in `composeApp/src/commonTest/kotlin/com/example/vvinamp/ui/playlist/`.

`PlaylistViewModelTest`:
- `savePlaylistDetail` with a cover: upload is called before the update, `isSaving` goes true then false, sheet closes, playlist reloaded.
- `savePlaylistDetail` upload failure: update not called, `isSaving` false, error set, sheet stays open.
- `savePlaylistDetail` with no cover change: upload not called.
- `addSongsToPlaylist`: recommended track marked added, `RefreshTracks` emitted, playlist reloaded quietly.
- `deletePlaylist`: success emits `PlaylistDeleted`; failure keeps the playlist and sets the error.
- `togglePrivacy`: calls update with the flipped value, then reloads.
- `getPlaylist` for a different id clears the old playlist straight away (A5).

`SearchViewModelTest` (extend the existing one, once P4 is fixed):
- Started with a target playlist: adding a catalog song and a YouTube song both add to that playlist without opening the picker.
- Started without one: both open the picker.

---

## 5. Decisions needed before fixing

1. **Upload progress**: the upload is a single GraphQL request, so there's no real percentage without extra work. Is a spinner over the cover plus a success/failure state enough, or do you want a real percentage bar?
2. **After adding from search (E2, E3)**: stay on search with a tick and "Added to …" message (what Spotify does), or go straight back to the playlist?
3. **Errors (P5)**: show a short message and keep the page, with the full error screen only when the playlist itself can't load. OK?
4. **Image limits (C9)**: maximum size and allowed types for covers? Should large images be shrunk before upload?
