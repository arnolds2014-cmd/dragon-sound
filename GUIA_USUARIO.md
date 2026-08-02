# GUÍA DE USUARIO - Audífono Propietario 130dB

## ¿QUÉ ES ESTA APLICACIÓN?

Una aplicación Android especializada para personas sordas con pérdida auditiva profunda. 
Amplifica sonidos hasta **130 dB** de presión sonora (SPL) con protección de seguridad médica.

### Características Principales

✅ **Amplificación Médica**
- Rango: 0 a 15x amplificación (0-130 dB SPL)
- Limitador de seguridad automático
- Compresor dinámico para proteger el tímpano

✅ **Múltiples Filtros**
1. **Normal** - Amplificación plana sin filtro
2. **Voz** - Pasa-banda (300-3000 Hz) - Ideal para conversación
3. **Bass** - Amplifica frecuencias bajas - Detectar música
4. **Treble** - Amplifica frecuencias altas - Alertas y sonidos agudos

✅ **Ecualización Ajustable**
- Control de Graves (Bass)
- Control de Agudos (Treble)
- Ajuste en tiempo real

✅ **Conectividad**
- 🔵 **Bluetooth** - Auriculares inalámbricos
- 📡 **WiFi** - Conectividad de red
- 🔌 **Audio Jack** - Auriculares con cable

✅ **Interfaz Fácil**
- Tema oscuro para comodidad visual
- Controles grandes y legibles
- Estado claro (Activo/Detenido)

---

## INSTALACIÓN

### Requisitos
- Android 7.0 o superior (recomendado: Android 9+)
- 50 MB de espacio libre
- Micrófono funcional
- Auriculares o altavoz

### Pasos de Instalación

#### 1. Desde APK directa (más simple)

```bash
# Conectar dispositivo con USB Debug activado
adb install app/build/outputs/apk/debug/app-debug.apk
```

#### 2. Desde Android Studio

1. Abre Android Studio
2. Archivo → Abrir proyecto → Selecciona `/dragon-sound`
3. Run → Run 'app'
4. Selecciona tu dispositivo

#### 3. Desde navegador

- Coloca el archivo `app-debug.apk` en tu dispositivo
- Abre el gestor de archivos
- Toca el APK para instalar
- Acepta permisos

### Permisos Requeridos

La app necesita estos permisos (se piden al iniciar):

```
✓ Micrófono           → Captar audio
✓ Audio               → Amplificar y reproducir
✓ Bluetooth           → Auriculares inalámbricos
✓ WiFi                → Conectividad de red
✓ Ubicación (Android 12+) → Bluetooth requiere ubicación
```

---

## CÓMO USAR

### Interfaz Principal

```
┌─────────────────────────────────────┐
│   Audífono Propietario 130dB       │
│   Estado: ACTIVO/DETENIDO          │
├─────────────────────────────────────┤
│ GANANCIA AUDITIVA                   │
│ [======●==========] 7.5x (94 dB)   │
│                                      │
│ FILTRO DE SONIDO                    │
│ [Seleccionar: Voz ▼]                │
│                                      │
│ ECUALIZACIÓN                        │
│ Graves:   [=====●=====]             │
│ Agudos:   [========●==]             │
│                                      │
│ CONECTIVIDAD                        │
│ [🔵 Ver Bluetooth] [📡 Ver WiFi]   │
├─────────────────────────────────────┤
│ [▶ ACTIVAR]     [⏹ DETENER]        │
└─────────────────────────────────────┘
```

### Instrucciones Paso a Paso

#### 1. ACTIVAR LA APP

1. Abre la aplicación
2. Toca el botón verde **▶ ACTIVAR**
3. Verás el estado cambiar a "Estado: ACTIVO" (verde)
4. El micrófono comenzará a captar sonido

#### 2. AJUSTAR GANANCIA

La ganancia es la amplificación del volumen:

```
Ganancia: 1.0x  = Sin amplificación
Ganancia: 5.0x  = 94 dB (Moderada)
Ganancia: 10.0x = 100 dB (Fuerte)
Ganancia: 15.0x = 103 dB (Máxima con seguridad)
```

**Cómo ajustar:**

1. Mira el control deslizante "GANANCIA AUDITIVA"
2. Arrastra a la izquierda para REDUCIR amplificación
3. Arrastra a la derecha para AUMENTAR amplificación
4. La amplificación se ajusta EN TIEMPO REAL
5. Prueba con diferentes niveles para encontrar comodidad

#### 3. SELECCIONAR FILTRO

Los filtros ayudan a enfatizar diferentes tipos de sonido:

```
1. NORMAL
   - Sin filtro adicional
   - Amplificación plana
   - Uso: General

2. VOZ (Pasa-banda)
   - Enfatiza 300-3000 Hz
   - Reduce ruido de fondo
   - Uso: Conversaciones, clases

3. BASS (Graves)
   - Amplifica frecuencias bajas
   - Mejora detectabilidad de baja frecuencia
   - Uso: Detectar música, bajos

4. TREBLE (Agudos)
   - Amplifica frecuencias altas
   - Mejora claridad de sonidos agudos
   - Uso: Alertas, tonos de campana, tonos altos
```

**Cómo seleccionar:**

1. Toca el menú desplegable "FILTRO DE SONIDO"
2. Elige el filtro deseado
3. El filtro se activa inmediatamente
4. Prueba cada uno en diferentes situaciones

#### 4. ECUALIZACIÓN FINA

Los controles de Graves y Agudos ajustan aún más:

```
GRAVES (Bass):
← Menos bajos  |  Neutral  |  Más bajos →

AGUDOS (Treble):  
← Menos agudos |  Neutral  |  Más agudos →
```

**Cómo ajustar:**

1. Arrastra "Graves" para enfatizar o reducir bajos
2. Arrastra "Agudos" para enfatizar o reducir agudos
3. Combina con el filtro seleccionado
4. Ejemplo: Filtro VOZ + Agudos elevados = Mejor claridad en conversación

#### 5. CONECTAR AURICULARES

##### Auriculares con Cable (3.5mm Jack)

1. Enchufa los auriculares en la salida de audio
2. La app detecta automáticamente
3. El sonido se reproduce en los auriculares

##### Auriculares Bluetooth

1. Empareja en Configuración del teléfono:
   - Ajustes → Bluetooth → Activar
   - Buscar dispositivos → Selecciona auriculares

2. En la app, toca **"🔵 Bluetooth - Ver Dispositivos"**
3. Verás lista de dispositivos emparejados
4. La conexión se establece automáticamente
5. El sonido se reproduce en los auriculares inalámbricos

##### Verificar Conexión

- Los auriculares deben tener luz indicadora (roja/verde)
- Luz VERDE = Conectado
- Luz ROJA = Desconectado

#### 6. CONECTAR A WiFi

1. Toca **"📡 WiFi - Ver Redes"**
2. Verás lista de redes disponibles
3. Para Android 10+, la app puede conectar automáticamente
4. Para Android 9 y anterior, conéctate desde Ajustes

#### 7. DETENER LA APP

1. Toca el botón rojo **⏹ DETENER**
2. El estado cambiará a "Estado: DETENIDO"
3. El micrófono deja de amplificar
4. Puedes cerrar la app

---

## CONFIGURACIÓN RECOMENDADA

### Para Conversación

```
Ganancia:    7-10x (100-103 dB)
Filtro:      Voz (Pasa-banda)
Graves:      Centro (-0 dB)
Agudos:      Ligeramente elevado (+0.2)
Auriculares: Bluetooth o Jack
```

### Para Detectar Sonidos Ambientales

```
Ganancia:    5-8x (94-100 dB)
Filtro:      Normal
Graves:      Elevado (+0.3)
Agudos:      Ligeramente elevado (+0.1)
Auriculares: Jack (mejor respuesta de bass)
```

### Para Música

```
Ganancia:    8-12x (100-103 dB)
Filtro:      Normal
Graves:      Elevado (+0.4)
Agudos:      Centro (0 dB)
Auriculares: Bluetooth (mejor movilidad)
```

### Para Alertas

```
Ganancia:    10-15x (103-106 dB)
Filtro:      Treble (Agudos)
Graves:      Reducido (-0.3)
Agudos:      Muy elevado (+0.5)
Auriculares: Cualquiera
```

---

## PROTECCIÓN DE SEGURIDAD MÉDICA

⚠️ **IMPORTANTE**

Esta app incluye limitadores automáticos para proteger tu audición:

### 1. Umbral de Seguridad
- Máximo: 130 dB SPL
- Si los picos superan esto, se comprimen automáticamente
- Evita daño auditivo

### 2. Compresor Dinámico
- Reduce automáticamente picos fuertes
- Suaviza distorsión
- Mantiene claridad

### 3. Recomendaciones

```
✓ SEGURO (< 100 dB SPL)
  Uso: >8 horas diarias
  Ambiente: Cualquiera
  
✓ CÓMODO (100-110 dB SPL)
  Uso: 4-8 horas diarias
  Ambiente: Moderado
  
⚠ ALERTA (110-120 dB SPL)
  Uso: 2-4 horas diarias
  Ambiente: Ruidoso
  
❌ MÁXIMO (>120 dB SPL)
  Uso: 1 hora máximo
  Ambiente: Muy ruidoso
```

---

## TROUBLESHOOTING

### Problema: No escucho nada

**Soluciones:**
1. ¿Está el botón en ACTIVAR? ✓
2. ¿Están conectados los auriculares?
3. ¿Está el volumen del teléfono al máximo?
4. ¿Está la ganancia por encima de cero?
5. Intenta: Detener → Activar (reiniciar)

### Problema: Sonido muy distorsionado

**Soluciones:**
1. Reduce la ganancia
2. Reduce los controles de Graves/Agudos
3. Cambia el filtro a "Normal"
4. Comprueba que los auriculares funcionan correctamente

### Problema: Bluetooth no conecta

**Soluciones:**
1. Activa Bluetooth en Ajustes
2. Empareja el dispositivo en Ajustes
3. Reinicia el Bluetooth
4. Acerca los auriculares al teléfono
5. Comprueba batería de auriculares

### Problema: Falta ganancia

**Soluciones:**
1. Aumenta el control deslizante
2. Intenta filtro "Treble" para agudos
3. Intenta filtro "Bass" para bajos
4. Verifica que el micrófono no esté obstruido

---

## PREGUNTAS FRECUENTES

**P: ¿Es seguro usarla 24/7?**
R: No. Máximo 8 horas diarias con ganancia moderada (<100 dB).

**P: ¿Funciona sin conexión a internet?**
R: Sí. Todo funciona offline. WiFi es solo opcional.

**P: ¿Puedo usarla en lugar de un audífono real?**
R: No. Es una AYUDA AUDITIVA temporal. Consulta a un especialista.

**P: ¿Qué pasa con el acento?**
R: Funciona con cualquier idioma.

**P: ¿Se puede grabar audio?**
R: Esta versión no graba, solo amplifica en tiempo real.

---

## ESPECIFICACIONES TÉCNICAS

```
Muestreo:          44,100 Hz
Formato:           PCM 16-bit Mono
Ganancia máxima:   15x (130 dB SPL)
Latencia:          <50ms (baja)
Procesamiento:     Nativo C++ (rápido)
Android mín:       7.0 (API 24)
Tamaño APK:        ~25-30 MB
```

---

**Versión:** 1.0.0
**Última actualización:** 2026
**Licencia:** Propietario - Uso médico autorizado
