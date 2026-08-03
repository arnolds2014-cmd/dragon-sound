{
  "expo": {
    "name": "Audifono Propietario",
    "slug": "audifono-propietario",
    "version": "1.0.0",
    "orientation": "portrait",
    "userInterfaceStyle": "dark",
    "assetBundlePatterns": [
      "**/*"
    ],
    "ios": {
      "supportsTablet": true
    },
    "android": {
      "package": "com.audifono.propietario",
      "versionCode": 1,
      "permissions": [
        "android.permission.RECORD_AUDIO",
        "android.permission.MODIFY_AUDIO_SETTINGS",
        "android.permission.BLUETOOTH",
        "android.permission.BLUETOOTH_ADMIN"
      ],
      "config": {
        "ndk": {
          "cFlags": ["-std=c++17", "-O3"],
          "abiFilters": ["armeabi-v7a", "arm64-v8a"]
        }
      }
    }
  }
}
