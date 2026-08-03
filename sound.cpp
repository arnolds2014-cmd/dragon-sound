#include <jni.h>
#include <string>
#include <vector>
#include <cmath>
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>

// Variables globales para el motor OpenSL ES
SLObjectItf engineObject = nullptr;
SLEngineItf engineEngine = nullptr;
SLObjectItf recorderObject = nullptr;
SLRecordItf recorderRecord = nullptr;
SLAndroidSimpleBufferQueueItf recorderBufferQueue = nullptr;
SLObjectItf bqPlayerObject = nullptr;
SLPlayItf bqPlayerPlay = nullptr;
SLAndroidSimpleBufferQueueItf bqPlayerBufferQueue = nullptr;

#define BUFFER_SIZE 512
int16_t audioBuffer[BUFFER_SIZE];

// Parámetros de procesamiento DSP para sordera profunda (130dB Max)
bool isProcessingActive = false;
float currentGain = 50.0f; 
bool isNoiseFilterEnabled = true;

// Filtros pasa-banda Bi-quad (Enfoque en frecuencias de voz humana: 250Hz - 4000Hz)
float b0 = 0.45f, b1 = 0.0f, b2 = -0.45f, a1 = -1.2f, a2 = 0.5f;
float x1 = 0, x2 = 0, y1 = 0, y2 = 0;

void processAudioDSP(int16_t* buffer, int length) {
    if (!isProcessingActive) return;

    // Convertir decibelios de la interfaz a factor multiplicador lineal
    float factorGananciaProfunda = std::pow(10.0f, (currentGain / 20.0f)) * 0.15f;
    
    // Límite de seguridad estricto de tu código original (Limiter de protección)
    float umbralSeguridad = 22937.0f; // 0.70f escalado a short de 16 bits (32767 * 0.7)
    float ratioCompresion = 10.0f;    // Compresión agresiva para sonidos fuertes imprevistos

    for (int i = 0; i < length; i++) {
        float sample = (float)buffer[i];

        // 1. Filtro Pasa-Banda (Aislamiento de voz humana antes de amplificar)
        float filteredSample = b0 * sample + b1 * x1 + b2 * x2 - a1 * y1 - a2 * y2;
        x2 = x1; x1 = sample;
        y2 = y1; y1 = filteredSample;

        // 2. Filtro de Ruido Dinámico / Compuerta para silenciar ruido de fondo estático
        if (isNoiseFilterEnabled) {
            if (std::abs(filteredSample) < 1500.0f) {
                filteredSample *= 0.15f; 
            }
        }

        // 3. Tu lógica de amplificación base para pérdida profunda
        filteredSample *= factorGananciaProfunda; 

        // 4. Tu lógica original de Compresión y Limitación de picos para protección
        if (filteredSample > umbralSeguridad) {
            filteredSample = umbralSeguridad + (filteredSample - umbralSeguridad) / ratioCompresion;
        }
        else if (filteredSample < -umbralSeguridad) {
            filteredSample = -umbralSeguridad + (filteredSample + umbralSeguridad) / ratioCompresion;
        }

        // Hard limiter definitivo para evitar desbordamiento del buffer
        if (filteredSample > 32767.0f) filteredSample = 32767.0f;
        if (filteredSample < -32768.0f) filteredSample = -32768.0f;

        buffer[i] = (int16_t)filteredSample;
    }
}

// Callback de cola de buffers para streaming nativo simultáneo de baja latencia
void bqRecorderCallback(SLAndroidSimpleBufferQueueItf bq, void *context) {
    processAudioDSP(audioBuffer, BUFFER_SIZE);
    if (bqPlayerBufferQueue) {
        (*bqPlayerBufferQueue)->Enqueue(bqPlayerBufferQueue, audioBuffer, sizeof(audioBuffer));
    }
    (*recorderBufferQueue)->Enqueue(recorderBufferQueue, audioBuffer, sizeof(audioBuffer));
}

extern "C" JNIEXPORT void JNICALL
Java_com_audifono_propietario_AudioEngine_startEngine(JNIEnv *env, jobject thiz) {
    isProcessingActive = true;
    slCreateEngine(&engineObject, 0, nullptr, 0, nullptr, nullptr);
    if (engineObject) {
        (*engineObject)->Realize(engineObject, SL_BOOLEAN_FALSE);
        (*engineObject)->GetInterface(engineObject, SL_IID_ENGINE, &engineEngine);
    }

    const SLInterfaceID ids = {SL_IID_ANDROIDSIMPLEBUFFERQUEUE};
    const SLboolean req = {SL_BOOLEAN_TRUE};
    
    SLDataLocator_IODevice loc_dev = {SL_DATALOCATOR_IODEVICE, SL_IODEVICE_AUDIOINPUT, SL_DEFAULTDEVICE_AUDIOINPUT, nullptr};
    SLDataSource audioSrc = {&loc_dev, nullptr};
    SLDataLocator_AndroidSimpleBufferQueue loc_bq = {SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, 2};
    SLDataFormat_PCM format_pcm = {SL_DATAFORMAT_PCM, 1, SL_SAMPLINGRATE_44_1, SL_PCMBITS_PER_SAMPLE_16, SL_PCMBITS_PER_SAMPLE_16, SL_SPEAKER_FRONT_CENTER, SL_BYTEORDER_LITTLEENDIAN};
    SLDataSink audioSnk = {&loc_bq, &format_pcm};

    if (engineEngine) {
        (*engineEngine)->CreateAudioRecorder(engineEngine, &recorderObject, &audioSrc, &audioSnk, 1, ids, req);
        if (recorderObject) {
            (*recorderObject)->Realize(recorderObject, SL_BOOLEAN_FALSE);
            (*recorderObject)->GetInterface(recorderObject, SL_IID_RECORD, &recorderRecord);
            (*recorderObject)->GetInterface(recorderObject, SL_IID_ANDROIDSIMPLEBUFFERQUEUE, &recorderBufferQueue);
        }
    }

    if (recorderBufferQueue) {
        (*recorderBufferQueue)->RegisterCallback(recorderBufferQueue, bqRecorderCallback, nullptr);
        (*recorderRecord)->SetRecordState(recorderRecord, SL_RECORDSTATE_RECORDING);
        (*recorderBufferQueue)->Enqueue(recorderBufferQueue, audioBuffer, sizeof(audioBuffer));
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_audifono_propietario_AudioEngine_stopEngine(JNIEnv *env, jobject thiz) {
    isProcessingActive = false;
    if (recorderRecord) (*recorderRecord)->SetRecordState(recorderRecord, SL_RECORDSTATE_STOPPED);
}

extern "C" JNIEXPORT void JNICALL
Java_com_audifono_propietario_AudioEngine_setGainNative(JNIEnv *env, jobject thiz, jfloat gain) {
    currentGain = gain;
}

extern "C" JNIEXPORT void JNICALL
Java_com_audifono_propietario_AudioEngine_toggleNoiseFilter(JNIEnv *env, jobject thiz, jboolean enable) {
    isNoiseFilterEnabled = enable;
}
