import { test, expect, type Locator, type Page } from '@playwright/test';

// Compose draws the app on a canvas that sits above its accessibility elements, so a normal
// click lands on the canvas and Playwright refuses it. Find the element, then touch its spot.
// A mouse click shows the press on a song tile but doesn't play it in phone mode; a touch tap does.
async function tap(page: Page, target: Locator) {
  await target.waitFor();
  const box = await target.boundingBox();
  if (!box) throw new Error('Element has no position on screen');
  await page.touchscreen.tap(box.x + box.width / 2, box.y + box.height / 2);
}

test('guest logs in and plays the first recommended song', async ({ page }) => {
  const started = Date.now();
  const step = (name: string) => console.log(`${name}: ${((Date.now() - started) / 1000).toFixed(1)}s`);

  await page.goto('/');
  const guest = page.getByRole('button', { name: 'Try as Guest (Demo)' });
  await guest.waitFor();
  step('Landing page ready');

  await tap(page, guest);
  await expect(page.getByText('Recommended for you')).toBeVisible({ timeout: 60_000 });
  step('Home loaded');

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
  await page.waitForTimeout(8_000);
  await page.screenshot({ path: test.info().outputPath('playing.png') });
  step('Done');
});
