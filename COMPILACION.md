# MANUAL DE COMPILACIÓN - APK Audífono 130dB

## Método 1: Compilación Automática (Recomendado)

### Requisitos Previos

```bash
# 1. Android SDK instalado
ls ~/Android/Sdk/

# 2. Android NDK instalado
ls ~/Android/Sdk/ndk/

# 3. Gradle 8.0 o superior
gradle --version

# 4. Java 11 o superior
java -version
```

### Pasos de Compilación

```bash
# 1. Navegar al directorio del proyecto
cd /home/elmer/Escritorio/dragon-sound

# 2. Limpiar compilaciones anteriores
gradle clean

# 3. Compilar APK Debug (desarrollo)
gradle assembleDebug

# 4. Compilar APK Release (producción, optimizado)
gradle assembleRelease

# Resultado:
# Debug:   app/build/outputs/apk/debug/app-debug.apk
# Release: app/build/outputs/apk/release/app-release.apk
```

## Método 2: Usando Android Studio

1. Abre Android Studio
2. File → Open → `/home/elmer/Escritorio/dragon-sound`
3. Espera a que sincronice Gradle (puede tardar 5-10 minutos)
4. Build → Make Project
5. Build → Build Bundle(s) / APK(s) → Build APK(s)
6. El APK se generará automáticamente

## Método 3: Compilación Usando Wrapper (Windows compatible)

```bash
cd /home/elmer/Escritorio/dragon-sound

# Linux/Mac
./gradlew assembleDebug

# Windows (en PowerShell)
.\gradlew.bat assembleDebug
```

## Archivos Generados

```
Después de compilar, encontrarás:

app/build/outputs/apk/debug/
├── app-debug.apk          ← APK para desarrollo (Sin firmar)
├── output.json            ← Metadata

app/build/outputs/apk/release/
├── app-release.apk        ← APK optimizado (Requiere firma)
└── app-release-unsigned.apk

app/build/outputs/
└── bundle/release/
    └── app-release.aab    ← Android App Bundle (Google Play)
```

## Tamaño y Propiedades

```
app-debug.apk:     ~30-35 MB
app-release.apk:   ~20-25 MB (comprimido)

Mínimo SDK:        API 24 (Android 7.0)
Target SDK:        API 33 (Android 13)
ABI soportados:    armeabi-v7a, arm64-v8a
```

## Problemas Comunes

### Error: "NDK not found"

```bash
# Verificar NDK instalado
ls ~/Android/Sdk/ndk/

# Si no está instalado:
# 1. Abre Android Studio
# 2. Tools → SDK Manager
# 3. SDK Tools → NDK (Side by side) → Instalar

# Luego actualiza local.properties:
echo "ndk.dir=/home/elmer/Android/Sdk/ndk/26.1.10909125" >> local.properties
```

### Error: "Gradle version too old"

```bash
# Descargar Gradle 8.0
cd /tmp
wget https://services.gradle.org/distributions/gradle-8.0-bin.zip
unzip gradle-8.0-bin.zip
export PATH="/tmp/gradle-8.0/bin:$PATH"

# O instalar globalmente
sudo apt install gradle=8.0-1
```

### Error: "Source files not found"

```bash
# Verificar estructura
ls -la app/src/main/java/com/audifono/propietario/

# Debería tener:
# - MainActivity.java
# - AudioService.java
# - BluetoothManager.java
# - WiFiManager.java
```

### Proceso de compilación muy lento

```bash
# Aumentar memoria de Gradle
export GRADLE_OPTS="-Xmx4096m -Xms1024m"

# Habilitar compilación paralela
echo "org.gradle.parallel=true" >> gradle.properties
echo "org.gradle.workers.max=8" >> gradle.properties
```

## Firmar APK para Google Play

```bash
# 1. Generar keystore
keytool -genkey -v -keystore audifono.keystore \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias audifono

# 2. Compilar y firmar
gradle bundleRelease

# 3. Firmar con jarsigner
jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 \
  -keystore audifono.keystore \
  app/build/outputs/bundle/release/app-release.aab audifono
```

## Instalación en Dispositivo

```bash
# Ver dispositivos conectados
adb devices

# Instalar APK Debug
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Instalar APK Release
adb install -r app/build/outputs/apk/release/app-release.apk

# Desinstalar
adb uninstall com.audifono.propietario

# Ver logs en vivo
adb logcat -s "AudiefonoJNI"
```

## Información de la Compilación

```gradle
# Desde app/build.gradle
- Gradle Plugin: com.android.tools.build:gradle:8.0.0
- Kotlin: 1.7.10 (si se usa)
- Target API: 33
- Min API: 24

# Dependencias
- AndroidX AppCompat 1.6.1
- Material Design 1.9.0
- Android NDK para compilación C++
```

## Información Técnica

**Módulos compilados:**

1. **Java/Kotlin**
   - MainActivity (UI)
   - AudioService (Audio engine)
   - BluetoothManager (BT connectivity)
   - WiFiManager (WiFi connectivity)

2. **C++ Nativo**
   - audio_processor.cpp
   - Compilado con CMake
   - JNI interface para llamadas Java

3. **Recursos**
   - Layouts XML
   - Drawables
   - Strings
   - Estilos

## Distribución

### Para desarrollo interno
```bash
gradle assembleDebug
# Compartir: app/build/outputs/apk/debug/app-debug.apk
```

### Para Google Play
```bash
gradle bundleRelease
# Subir: app/build/outputs/bundle/release/app-release.aab
```

### Para distribución directa
```bash
gradle assembleRelease
# Firmar y distribuir: app-release.apk
```

## Verificación

Después de compilar, verifica que el APK sea válido:

```bash
# Ver contenido del APK
unzip -l app/build/outputs/apk/debug/app-debug.apk

# Verificar firma
jarsigner -verify -verbose app/build/outputs/apk/debug/app-debug.apk

# Obtener información del APK
aapt dump badging app/build/outputs/apk/debug/app-debug.apk
```

## Referencias

- [Android Build Documentation](https://developer.android.com/build)
- [Gradle Plugin Guide](https://developer.android.com/studio/build/gradle-plugin)
- [NDK Setup](https://developer.android.com/ndk/guides/setup)
- [JNI Guide](https://developer.android.com/training/articles/perf-jni)
