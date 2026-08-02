#!/bin/bash

###############################################
# VERIFICADOR DE APK
# Verifica integridad y properties del APK
###############################################

PROJECT_DIR="/home/elmer/Escritorio/dragon-sound"
APK_PATH="${1:-$PROJECT_DIR/app/build/outputs/apk/release/app-release.apk}"

echo ""
echo "╔═══════════════════════════════════════════╗"
echo "║     VERIFICADOR DE APK - Audífono         ║"
echo "╚═══════════════════════════════════════════╝"
echo ""

# Verificar si existe el APK
if [ ! -f "$APK_PATH" ]; then
    echo "❌ APK no encontrado: $APK_PATH"
    exit 1
fi

echo "✅ APK detectado"
echo ""
echo "📊 PROPIEDADES:"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Información del archivo
echo "📄 Archivo: $(basename "$APK_PATH")"
echo "📏 Tamaño: $(du -h "$APK_PATH" | cut -f1)"
echo "📅 Fecha: $(stat -c %y "$APK_PATH" | cut -d' ' -f1-2)"
echo "🔐 MD5: $(md5sum "$APK_PATH" | cut -d' ' -f1)"

echo ""
echo "📦 CONTENIDO:"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Contar archivos
total_files=$(unzip -l "$APK_PATH" | tail -1 | awk '{print $2}')
echo "📂 Total de archivos: $total_files"

# Clases
class_files=$(unzip -l "$APK_PATH" | grep -c "\.class$" || echo "0")
echo "☕ Archivos .class: $class_files"

# Recursos
resource_files=$(unzip -l "$APK_PATH" | grep -E "\.(xml|drawable|layout)$" | wc -l)
echo "🎨 Recursos (xml/drawable): $resource_files"

# Librerías nativas
native_libs=$(unzip -l "$APK_PATH" | grep -c "\.so$" || echo "0")
echo "🔧 Librerías nativas (.so): $native_libs"

echo ""
echo "🏢 INFORMACIÓN DE MANIFIESTO:"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Extraer información del manifest
if command -v aapt &> /dev/null; then
    echo ""
    aapt dump badging "$APK_PATH" | head -20
else
    echo "ℹ️  (aapt no disponible - Android SDK Tools requeridas)"
fi

echo ""
echo "✅ VERIFICACIÓN COMPLETADA"
echo ""
echo "🚀 Instalar:"
echo "   adb install -r \"$APK_PATH\""
echo ""
