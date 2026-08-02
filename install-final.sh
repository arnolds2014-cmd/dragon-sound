#!/bin/bash

###############################################
# INSTALADOR FINAL - Audífono 130dB APK
# Instala automáticamente el Release APK
###############################################

set -e

PROJECT_DIR="/home/elmer/Escritorio/dragon-sound"
RELEASE_APK="$PROJECT_DIR/app/build/outputs/apk/release/app-release.apk"
DEBUG_APK="$PROJECT_DIR/app/build/outputs/apk/debug/app-debug.apk"

echo ""
echo "╔═══════════════════════════════════════════╗"
echo "║    INSTALADOR - Audífono Propietario     ║"
echo "║         GANANCIA: 130 dB SPL             ║"
echo "╚═══════════════════════════════════════════╝"
echo ""

# Función para instalar APK
instalar_apk() {
    local apk_path=$1
    local apk_name=$(basename "$apk_path")
    
    if [ ! -f "$apk_path" ]; then
        echo "❌ APK no encontrado: $apk_path"
        return 1
    fi
    
    echo "✅ APK encontrado"
    echo "📦 Archivo: $apk_name"
    echo "📏 Tamaño: $(du -h "$apk_path" | cut -f1)"
    echo ""
    
    # Verificar adb
    if ! command -v adb &> /dev/null; then
        echo "❌ Error: 'adb' no encontrado en PATH"
        echo "   Instala Android SDK/Tools"
        return 1
    fi
    
    echo "🔍 Dispositivos detectados:"
    adb devices
    echo ""
    
    # Contar dispositivos
    device_count=$(adb devices | grep -v "List of" | grep -v "^$" | wc -l)
    
    if [ $device_count -lt 1 ]; then
        echo "❌ No hay dispositivos conectados"
        echo "   Activa USB Debug en tu dispositivo"
        return 1
    fi
    
    # Solicitar confirmación
    read -p "¿Instalar APK en el dispositivo? (s/n): " -n 1 -r
    echo ""
    
    if [[ ! $REPLY =~ ^[Ss]$ ]]; then
        echo "❌ Instalación cancelada"
        return 0
    fi
    
    echo ""
    echo "📱 Instalando..."
    if adb install -r "$apk_path"; then
        echo ""
        echo "╔═══════════════════════════════════════════╗"
        echo "║     ✅ ¡INSTALACIÓN EXITOSA!             ║"
        echo "╚═══════════════════════════════════════════╝"
        echo ""
        echo "🚀 Opciones:"
        echo "   • Inicia app: adb shell am start -n com.audifono.propietario/.MainActivity"
        echo "   • Ver logs:   adb logcat -s AudiefonoJNI"
        echo "   • Desinstalar: adb uninstall com.audifono.propietario"
        echo ""
        return 0
    else
        echo "❌ Error durante la instalación"
        return 1
    fi
}

# Seleccionar APK
echo "📋 Selecciona cuál APK instalar:"
echo ""
echo "1️⃣  Release (RECOMENDADO - Optimizado, ~20 MB)"
echo "2️⃣  Debug (Desarrollo, ~30 MB)"
echo "0️⃣  Salir"
echo ""
read -p "Selecciona opción (0-2): " -n 1 -r opcion
echo ""
echo ""

case $opcion in
    1)
        echo "📦 Instalando: RELEASE APK"
        instalar_apk "$RELEASE_APK"
        ;;
    2)
        echo "📦 Instalando: DEBUG APK"
        instalar_apk "$DEBUG_APK"
        ;;
    0)
        echo "👋 Saliendo..."
        exit 0
        ;;
    *)
        echo "❌ Opción no válida"
        exit 1
        ;;
esac
