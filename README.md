# Audífono Propietario 130dB - APK para Sordos

Esta es una aplicación Android especializada para ayudar a personas con pérdida auditiva profunda.

## Características

✅ **Amplificación de hasta 130 dB** con limitador de seguridad médico
✅ **Soporte Bluetooth** para auriculares inalámbricos  
✅ **Soporte WiFi** para conectividad remota
✅ **Filtros de Sonido** configurables:
   - Normal
   - Pasa-banda para voz (300-3000 Hz)
   - Boost de graves (Bass)
   - Boost de agudos (Treble)

✅ **Ecualización ajustable** en tiempo real
✅ **Interfaz oscura** fácil de usar
✅ **Compilación optimizada para ARM**

## Requisitos

- Android 7.0+ (API 24)
- Android Studio 2022.1+
- Android NDK (para compilar C++)
- Gradle 8.0+

## Estructura del Proyecto

```
dragon-sound/
├── app/
│   ├── src/main/
│   │   ├── java/com/audifono/propietario/
│   │   │   ├── MainActivity.java          # Interfaz principal
│   │   │   ├── AudioService.java          # Motor de audio
│   │   │   ├── BluetoothManager.java      # Gestión Bluetooth
│   │   │   ├── WiFiManager.java           # Gestión WiFi
│   │   ├── cpp/
│   │   │   ├── audio_processor.cpp        # Procesamiento de audio nativo
│   │   │   ├── CMakeLists.txt             # Configuración CMake
│   │   ├── res/
│   │   │   ├── layout/activity_main.xml   # Interfaz gráfica
│   │   │   ├── drawable/                  # Recursos gráficos
│   │   │   ├── values/                    # Strings y estilos
│   │   ├── AndroidManifest.xml            # Configuración de la app
│   ├── build.gradle                       # Configuración de compilación
├── build.gradle                           # Configuración raíz
├── settings.gradle                        # Configuración de Gradle
├── gradle.properties                      # Propiedades globales
└── README.md                              # Este archivo

## Instalación y Compilación

### 1. Clonar/Descargar el proyecto

```bash
cd /home/elmer/Escritorio/dragon-sound
```

### 2. Configurar Android SDK

Asegúrate de que en ~/.gradle/gradle.properties está configurado:

```bash
sdk.dir=/path/to/your/Android/SDK
```

O en Android Studio: File → Project Structure → SDK Location

### 3. Compilar Debug APK

```bash
./gradlew assembleDebug
```

El APK se generará en: `app/build/outputs/apk/debug/app-debug.apk`

### 4. Compilar Release APK (Optimizado)

```bash
./gradlew assembleRelease
```

El APK se generará en: `app/build/outputs/apk/release/app-release.apk`

### 5. Instalar en dispositivo

```bash
# Con USB Debug activado
adb install app/build/outputs/apk/debug/app-debug.apk

# O si necesitas reinstalar
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Permisos Requeridos

- `RECORD_AUDIO` - Captar audio del micrófono
- `MODIFY_AUDIO_SETTINGS` - Controlar ganancia y volumen
- `BLUETOOTH` - Conectar dispositivos Bluetooth
- `BLUETOOTH_ADMIN` - Administrar conexión Bluetooth
- `BLUETOOTH_CONNECT` - Android 12+ - Conectar a periféricos
- `BLUETOOTH_SCAN` - Android 12+ - Escanear dispositivos
- `ACCESS_WIFI_STATE` - Ver estado de WiFi
- `CHANGE_WIFI_STATE` - Cambiar red WiFi
- `INTERNET` - Conectividad web
- `ACCESS_NETWORK_STATE` - Estado de red
- `ACCESS_FINE_LOCATION` - Ubicación para Bluetooth (Android 12+)

## Características Técnicas

### Audio Processing

- **Frecuencia de muestreo**: 44.1 kHz
- **Formato**: PCM 16-bit Mono
- **Latencia baja**: Buffer mínimo para evitar eco
- **JNI Nativo**: Procesamiento C++ para máximo rendimiento

### Amplificación

- Rango: 0 - 15x (0 - 130 dB SPL)
- Limitador de seguridad: -0.70 dB
- Ratio de compresión: 10:1 (previene clipping)

### Filtros de Audio

1. **Normal**: Sin filtro, amplificación plana
2. **Voz**: Pasa-banda 300-3000 Hz (reduce ruido)
3. **Bass**: Amplifica frecuencias bajas
4. **Treble**: Amplifica frecuencias altas

## Soporte Bluetooth

- Conexión RFCOMM estándar
- UUID: `00001101-0000-1000-8000-00805F9B34FB`
- Compatible con auriculares estándar
- Emparejamiento automático

## Soporte WiFi

- Escaneo de redes disponibles
- Conexión a redes WPA2/WPA3
- API nativa Android 10+ (WifiNetworkSpecifier)

## Limitaciones de Seguridad Médica

⚠️ **IMPORTANTE**: Esta aplicación incluye limitadores de seguridad obligatorios:

- **Umbral máximo de presión sonora**: 130 dB SPL
- **Compresión automática**: Se activa cuando se detectan picos
- **Clipping prevention**: Suavizado de ondas distorsionadas

Esto es para PROTEGER LA SALUD AUDITIVA del usuario.

## Compilación con Android Studio

1. Abre Android Studio
2. File → Open Project → Selecciona `/home/elmer/Escritorio/dragon-sound`
3. Wait for Gradle sync
4. Run → Run 'app' (o presiona Shift+F10)

## Troubleshooting

### Error: "Android NDK not found"

```bash
# Instalar NDK
./gradlew build
# Si persiste, instálalo manualmente desde Android Studio:
# Tools → SDK Manager → SDK Tools → NDK
```

### Error: "Gradle build failed"

```bash
# Limpiar caché
./gradlew clean

# Volver a compilar
./gradlew assembleDebug
```

### APK no instala

```bash
# Verificar que el dispositivo está en modo debug
adb devices

# Forzar instalación
adb install -r -g app/build/outputs/apk/debug/app-debug.apk
```

## Licencia

Propietario - Uso médico autorizado solamente

## Autor

Proyecto especializado para audífonos con ganancia profunda (130 dB)

---

**Última actualización**: 2026
**Versión**: 1.0.0
