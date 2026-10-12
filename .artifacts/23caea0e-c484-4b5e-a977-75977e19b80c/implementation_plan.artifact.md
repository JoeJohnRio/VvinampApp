# Implementation Plan - Fix Room Database Timeout and Audio Playback on Web

The user is encountering a persistent `SQLiteException` (Error code: 5, SQLITE_BUSY) in the Wasm environment, which manifests as a writer connection timeout. This prevents the app from saving/loading playback state and often blocks the main thread or coroutines, hindering audio playback. Additionally, HLS playback is problematic on desktop browsers.

## User Review Required

> [!IMPORTANT]
> **Missing Database Worker:** The analysis indicates that the required SQLite Web Worker files (`worker.js` and associated `.wasm` files) are missing from the `src/wasmJsMain/resources` directory. Without these, the Room database will not function on Web.
> **HLS Compatibility:** Desktop browsers (Chrome/Firefox) do not natively support HLS (`.m3u8`). We will prioritize MP3 fallback or graceful degradation if HLS is unavailable.

## Proposed Changes

### 1. Database Infrastructure (Wasm)

The `WebWorkerSQLiteDriver` requires the SQLite worker files to be available at runtime. We need to ensure these are bundled correctly.

#### [MODIFY] [DatabaseFactory.kt](file:///Users/joel/Programming/jerman/VvinampApp/data/src/wasmJsMain/kotlin/com/example/data/db/music/DatabaseFactory.kt)
- Review the `createWorker` implementation to ensure it matches the expected resource path.

### 2. Playback Resilience (Web)

Since the database is currently unstable, we must decouple playback initialization from database operations to prevent the UI from hanging.

#### [MODIFY] [PlaybackViewModel.kt](file:///Users/joel/Programming/jerman/VvinampApp/composeApp/src/wasmJsMain/kotlin/com/example/vvinamp/ui/main/playback/PlaybackViewModel.kt)
- Wrap the initial track loading in `init` with a `try-catch` to prevent the `ViewModel` from becoming unusable if the database fails immediately.
- Ensure `start()` is called even if `saveCurrentTrack` fails (partially addressed in previous turn, but needs review for completeness).
- Add more logging to identify if the worker is failing to load.

### 3. Build Configuration

Ensure the SQLite Web Worker assets are correctly handled.

#### [MODIFY] [build.gradle.kts](file:///Users/joel/Programming/jerman/VvinampApp/composeApp/build.gradle.kts)
- Add a task to copy the required SQLite worker files from the `androidx.sqlite:sqlite-web` dependency into the web distribution if they aren't being picked up automatically.

## Verification Plan

### Manual Verification
1.  **Inspect Web Console:** Check for 404 errors related to `worker.js` or `sqlite3.wasm`.
2.  **Verify Playback:** Trigger a song playback and observe if the "audio element" starts playing despite database logs showing errors.
3.  **Check Local Storage/IndexedDB:** Verify if the database is actually being created in the browser's application tab.
