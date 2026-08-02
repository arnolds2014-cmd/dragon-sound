# assets/README — placeholder and instructions

This repository referenced the following asset files from (misnamed) config files:

- ./assets/icon.png
- ./assets/adaptive-icon.png
- ./assets/splash.png

They are required for a successful Expo/EAS prebuild and build. Please replace the placeholders below with your final artwork.

Quick ways to add temporary images:

1) Create a 512x512 PNG for the icon and save it to `assets/icon.png`.
2) Create a 432x432 (or same as above) PNG for `assets/adaptive-icon.png`.
3) Create a splash image with recommended size (e.g., 1242x2436) and save to `assets/splash.png`.

If you don't have image editing tools, you can create a 1x1 transparent PNG locally using the following base64 data (decode and save as PNG):

data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR4nGNgYAAAAAMAASsJTYQAAAAASUVORK5CYII=

On Unix/macOS you can create a 1x1 PNG quickly:

  printf '%s' "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR4nGNgYAAAAAMAASsJTYQAAAAASUVORK5CYII=" | base64 --decode > assets/icon.png

Repeat for `adaptive-icon.png` and `splash.png`.
