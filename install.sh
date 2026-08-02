#!/bin/bash

###############################################
# Script de Instalación - Audífono 130dB APK
###############################################

set -e

PROJECT_DIR="/home/elmer/Escritorio/dragon-sound"
APK_PATH="$PROJECT_DIR/app/build/outputs/apk/debug/app-debug.apk"

echo ""
echo "╔════════════════════════════════════════╗"
echo "║ INSTALADOR APK - Audífono Propietario ║"
echo "║ Ganancia: 130 dB                       ║"
echo "╚════════════════════════════════════════╝"
echo ""

# Verificar que el APK existe
if [ ! -f "$APK_PATH" ]; then
    echo "❌ Error: APK no encontrado en:"
    echo "   $APK_PATH"
    echo ""
    echo "Por favor, compila primero:"
    echo "   cd $PROJECT_DIR"
    echo "   gradle assembleDebug"
    exit 1
fi

echo "✅ APK encontrado"
echo "📦 Tamaño: $(du -h "$APK_PATH" | cut -f1)"
echo ""

# Verificar adb
if ! command -v adb &> /dev/null; then
    echo "❌ Error: adb no encontrado"
    exit 1
fi

echo "🔍 Buscando dispositivos conectados..."
adb devices

echo ""
read -p "¿Instalar en el dispositivo? (s/n): " -n 1 -r
echo ""
if [[ $REPLY =~ ^[Ss]$ ]]; then
    echo "📱 Instalando APK..."
    adb install -r "$APK_PATH"
    
    if [ $? -eq 0 ]; then
        echo ""
        echo "✅ ¡Instalación exitosa!"
        echo ""
        echo "🚀 Opciones:"
        echo "   1. Iniciar app: adb shell am start -n com.audifono.propietario/.MainActivity"
        echo "   2. Ver logs:    adb logcat | grep Audifono"
        echo ""
    else
        echo "❌ Error en la instalación"
        exit 1
    fi
else
    echo "❌ Instalación cancelada"
    exit 0
fi
