// Límite de seguridad estricto (Limiter de protección)
float umbralSeguridad = 0.70f; // Evita el pico máximo de distorsión en el hardware
float ratioCompresion = 10.0f; // Compresión agresiva para sonidos fuertes imprevistos

for (int i = 0; i < numFrames; ++i) {
    // Amplificación base para pérdida profunda
    floatData[i] *= factorGananciaProfunda; 

    // Si el sonido supera el umbral seguro, se comprime inmediatamente
    if (floatData[i] > umbralSeguridad) {
        floatData[i] = umbralSeguridad + (floatData[i] - umbralSeguridad) / ratioCompresion;
    }
    else if (floatData[i] < -umbralSeguridad) {
        floatData[i] = -umbralSeguridad + (floatData[i] + umbralSeguridad) / ratioCompresion;
    }
}