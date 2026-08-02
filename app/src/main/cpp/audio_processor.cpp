#include <jni.h>
#include <android/log.h>
#include <cmath>

#define LOG_TAG "AudiefonoJNI"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

// Constantes de seguridad médica
const float UMBRAL_SEGURIDAD = 0.70f;  // Límite estricto
const float RATIO_COMPRESION = 10.0f;  // Compresión agresiva
const float GANANCIA_MAXIMA = 15.0f;   // 130 dB

// JNI: Procesar buffer de audio con amplificación
extern "C" JNIEXPORT void JNICALL
Java_com_audifono_propietario_AudioService_procesarAudioNativo(
    JNIEnv *env, jobject thiz,
    jfloatArray buffer_arr,
    jint num_frames,
    jfloat ganancia) {

    // Obtener puntero al buffer
    jfloat* buffer = env->GetFloatArrayElements(buffer_arr, nullptr);

    if (buffer == nullptr) {
        LOGI("Error: No se pudo acceder al buffer");
        return;
    }

    // Procesar cada muestra
    for (int i = 0; i < num_frames; ++i) {
        // Amplificación base para pérdida profunda
        buffer[i] *= ganancia;

        // COMPRESOR DINÁMICO - Limitador de seguridad absoluto
        if (buffer[i] > UMBRAL_SEGURIDAD) {
            buffer[i] = UMBRAL_SEGURIDAD + (buffer[i] - UMBRAL_SEGURIDAD) / RATIO_COMPRESION;
        }
        else if (buffer[i] < -UMBRAL_SEGURIDAD) {
            buffer[i] = -UMBRAL_SEGURIDAD + (buffer[i] + UMBRAL_SEGURIDAD) / RATIO_COMPRESION;
        }
    }

    // Liberar referencias
    env->ReleaseFloatArrayElements(buffer_arr, buffer, 0);
}

// JNI: Aplicar filtro pasa-banda para voz
extern "C" JNIEXPORT void JNICALL
Java_com_audifono_propietario_AudioService_aplicarFiltroVozNativo(
    JNIEnv *env, jobject thiz,
    jfloatArray buffer_arr,
    jint num_frames) {

    jfloat* buffer = env->GetFloatArrayElements(buffer_arr, nullptr);

    if (buffer == nullptr) return;

    // Filtro simple pasa-banda (300-3000 Hz)
    for (int i = 2; i < num_frames; i++) {
        buffer[i] = 0.5f * buffer[i] + 0.25f * buffer[i-1] + 0.25f * buffer[i-2];
    }

    env->ReleaseFloatArrayElements(buffer_arr, buffer, 0);
}

// JNI: Obtener valor máximo de presión sonora en dB
extern "C" JNIEXPORT jfloat JNICALL
Java_com_audifono_propietario_AudioService_calcularSPLNativo(
    JNIEnv *env, jobject thiz,
    jfloat ganancia) {

    // SPL = 20 * log10(ganancia)
    // Referencia: 1 Pa = 94 dB SPL
    if (ganancia > 0) {
        return 94.0f + (20.0f * log10(ganancia));
    }
    return 0.0f;
}
