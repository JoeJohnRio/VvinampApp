import { test, expect, type Locator, type Page } from '@playwright/test';

// DEMO=1 waits for Home to finish loading and pauses before each tap, so the recording reads like a person using the app.
const demo = process.env.DEMO === '1';

// Compose draws the app on a canvas that sits above its accessibility elements, so a normal
// click lands on the canvas and Playwright refuses it. Find the element, then touch its spot.
// A mouse click shows the press on a song tile but doesn't play it in phone mode; a touch tap does.
async function tap(page: Page, target: Locator) {
  await target.waitFor();
  const box = await target.boundingBox();
  if (!box) throw new Error('Element has no position on screen');
  await page.touchscreen.tap(box.x + box.width / 2, box.y + box.height / 2);
}

async function pause(page: Page, ms: number) {
  if (demo) await page.waitForTimeout(ms);
}

/** Resolves once no request has been in flight for `quietMs`, so covers have finished loading. */
function trackNetwork(page: Page) {
  let inFlight = 0;
  let lastChange = Date.now();
  const change = (delta: number) => { inFlight += delta; lastChange = Date.now(); };
  page.on('request', () => change(1));
  page.on('requestfinished', () => change(-1));
  page.on('requestfailed', () => change(-1));
  return async (quietMs = 1_000) => {
    await expect
      .poll(() => inFlight <= 0 && Date.now() - lastChange >= quietMs, { timeout: 30_000, intervals: [250] })
      .toBe(true);
  };
}

test('guest logs in and plays the first recommended song', async ({ page, headless }) => {
  // DEMO=1 with --headed is for screen recording: time to start recording first, and the window stays open at the end.
  const recording = demo && !headless;
  if (recording) test.setTimeout(0);
  const started = Date.now();
  const step = (name: string) => console.log(`${name}: ${((Date.now() - started) / 1000).toFixed(1)}s`);
  const networkQuiet = trackNetwork(page);

  await page.goto('/');
  const guest = page.getByRole('button', { name: 'Try as Guest (Demo)' });
  await guest.waitFor();
  if (demo) await networkQuiet();
  step('Landing page ready');
  if (recording) {
    for (let s = 10; s > 0; s--) {
      console.log(`Start recording now. Tapping in ${s}...`);
      await page.waitForTimeout(1_000);
    }
  }
  await pause(page, 1_500);

  await tap(page, guest);
  await expect(page.getByText('Recommended for you')).toBeVisible({ timeout: 60_000 });
  step('Home loaded');

  if (demo) {
    // Recently Played tiles are the Home buttons that aren't songs ("…, Play") or the YouTube card.
    const home = page.getByRole('list').first();
    await expect
      .poll(async () =>
        (await home.getByRole('button').count()) -
        (await home.getByRole('button', { name: /, Play$/ }).count()) -
        (await home.getByRole('button', { name: /^Stream from YouTube/ }).count()),
        { timeout: 30_000 })
      .toBeGreaterThan(0);
    await networkQuiet();
    step('Home finished loading');
    await pause(page, 2_000);
  }

  const firstSong = page.getByRole('list').getByRole('list').getByRole('button').first();
  const label = (await firstSong.getAttribute('aria-label')) ?? (await firstSong.textContent()) ?? '';
  const title = label.replace(/, Play$/, '');

  // The app plays through an audio element that isn't on the page, so watch for its audio file instead.
  const audio = page.waitForResponse(
    (r) => r.request().resourceType() === 'media' || /\.(m3u8|mp3|m4a|aac)(\?|$)/.test(r.url()),
    { timeout: 60_000 },
  );
  await tap(page, firstSong);
  const response = await audio;
  step(`Audio started loading for "${title}"`);
  expect(response.status()).toBeLessThan(400);

  // Leave a few seconds of playback in the recording.
  await page.waitForTimeout(recording ? 10_000 : 8_000);
  await page.screenshot({ path: test.info().outputPath('playing.png') });
  step('Done');

  if (recording) {
    console.log('Stop your recording, then close the Chrome window to finish.');
    await page.waitForEvent('close', { timeout: 0 });
  }
});
