-keep class com.audifono.propietario.** { *; }
-keepclasseswithmembernames class * {
    native <methods>;
}
-keep class android.media.** { *; }
-keep class android.bluetooth.** { *; }
-dontwarn android.media.**
-dontwarn android.bluetooth.**
