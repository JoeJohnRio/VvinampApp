import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
  testDir: './tests',
  // The backend on Render can take a while to wake up.
  timeout: 120_000,
  use: {
    baseURL: process.env.BASE_URL ?? 'https://vvinamp.vercel.app',
    ...devices['Pixel 7'],
    // Use the Chrome already installed instead of downloading Playwright's own browser.
    channel: 'chrome',
    // VIDEO=off skips recording.
    video: process.env.VIDEO === 'off' ? 'off' : { mode: 'on', size: { width: 412, height: 839 } },
    trace: 'retain-on-failure',
  },
});
