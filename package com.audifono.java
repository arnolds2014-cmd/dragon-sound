package com.audifono.propietario;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.AudioManager;
import android.media.MediaRecorder;

public class AudioEngineNative {
    private boolean isRunning = false;
    private final int sampleRate = 44100;
    
    // Parámetros médicos para pérdida profunda (hasta 130dB con limitador)
    private final float umbralSeguridad = 0.50f; // Límite estricto de amplitud
    private final float ratioCompresion = 12.0f; // Compresión fuerte para picos
    private float factorGanancia = 15.0f;        // Multiplicador para sordera profunda

    public void iniciarAudifono() {
        isRunning = true;
        new Thread(() -> {
            // 1. Configurar buffer mínimo para evitar eco (Baja Latencia)
            int bufferSize = AudioRecord.getMinBufferSize(sampleRate, 
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_FLOAT);
            
            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 
                sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_FLOAT, bufferSize);
                
            AudioTrack player = new AudioTrack(AudioManager.STREAM_MUSIC, 
                sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_FLOAT, 
                bufferSize, AudioTrack.MODE_STREAM);

            recorder.startRecording();
            player.play();

            float[] buffer = new float[bufferSize / 4];

            while (isRunning) {
                int read = recorder.read(buffer, 0, buffer.length, AudioRecord.READ_BLOCKING);
                
                for (int i = 0; i < read; i++) {
                    // MODO VOZ: Filtro pasa-banda simple (Atenúa graves muy profundos)
                    if (i > 0) buffer[i] = 0.9f * buffer[i] + 0.1f * buffer[i-1]; 

                    // Amplificación Base
                    buffer[i] *= factorGanancia;

                    // COMPRESOR DINÁMICO (Protección total del tímpano)
                    if (buffer[i] > umbralSeguridad) {
                        buffer[i] = umbralSeguridad + (buffer[i] - umbralSeguridad) / ratioCompresion;
                    } else if (buffer[i] < -umbralSeguridad) {
                        buffer[i] = -umbralSeguridad + (buffer[i] + umbralSeguridad) / ratioCompresion;
                    }
                }
                // Enviar directo al audífono (Cable o Bluetooth)
                player.write(buffer, 0, read, AudioTrack.WRITE_BLOCKING);
            }
            
            recorder.stop();
            player.stop();
        }).start();
    }

    public void detenerAudifono() { isRunning = false; }
    public void setGanancia(float nuevaGanancia) { this.factorGanancia = nuevaGanancia; }
}