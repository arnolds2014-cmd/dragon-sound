#!/bin/bash

# Script de compilación para generar APK

echo "========================================"
echo "Compilador de APK - Audífono Propietario"
echo "========================================"

# Verificar que estamos en el directorio correcto
if [ ! -f "settings.gradle" ]; then
    echo "❌ Error: No se encontró settings.gradle"
    echo "Ejecutar este script desde la raíz del proyecto"
    exit 1
fi

# Limpiar compilaciones anteriores
echo "🧹 Limpiando compilaciones anteriores..."
./gradlew clean

# Compilar DEBUG
echo ""
echo "🔨 Compilando versión DEBUG..."
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo "✅ APK Debug compilado exitosamente"
    echo "📦 Ubicación: app/build/outputs/apk/debug/app-debug.apk"
else
    echo "❌ Error en compilación Debug"
    exit 1
fi

# Compilar RELEASE
echo ""
echo "🔨 Compilando versión RELEASE (optimizada)..."
./gradlew assembleRelease

if [ $? -eq 0 ]; then
    echo "✅ APK Release compilado exitosamente"
    echo "📦 Ubicación: app/build/outputs/apk/release/app-release.apk"
else
    echo "❌ Error en compilación Release"
    exit 1
fi

echo ""
echo "========================================"
echo "✅ Compilación completada"
echo "========================================"
