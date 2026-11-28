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
    // VIDEO=off skips recording. The video is window-sized (412 px wide): good for checking runs, too soft for a demo.
    video: process.env.VIDEO === 'off' ? 'off' : { mode: 'on', size: devices['Pixel 7'].viewport },
    trace: 'retain-on-failure',
  },
});
