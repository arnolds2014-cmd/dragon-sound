Expo + EAS migration notes

1) Overview
- This project will use Expo Bare workflow + EAS (cloud) to produce an Android APK.
- The repo contains native code (C/C++). After `prebuild` verify that your native sources
  are integrated into the generated `android/` project. You may need to move or merge files.

2) Quick commands (run locally)

```bash
# Install deps
npm install

# Ensure you have the EAS CLI (use npx if you prefer):
npm install -g eas-cli

# Login to Expo/EAS
eas login

# Generate native projects (prebuild). This will create/modify `android/` and `ios/` folders.
npx expo prebuild

# Build an APK in the cloud (profile: production -> APK)
eas build --platform android --profile production

# Download artifact from EAS build page or via `eas build:list` / `eas build:download`
```

3) Important notes
- If you rely on custom native C/C++ code (NDK), keep a copy of your current `app/src/main/cpp` and
  JNI/CMake files and merge them into the generated `android/` project after `prebuild`.
- To produce a signed APK, configure credentials in EAS (the CLI will guide you), or
  supply a keystore with `eas credentials`.
