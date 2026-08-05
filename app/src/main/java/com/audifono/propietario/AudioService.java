package com.audifono.propietario;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.AudioManager;
import android.media.MediaRecorder;

public class AudioService {
    private Context context;
    private boolean isRunning = false;
    private final int sampleRate = 44100;
    
    // Parámetros médicos para pérdida profunda
    private final float umbralSeguridad = 0.50f;
    private final float ratioCompresion = 12.0f;
    private float factorGanancia = 10.0f;
    
    // Filtros
    private float trebleGain = 0.0f;
    private float bassGain = 0.0f;
    private int filtroActual = 0; // 0=Normal, 1=Voz, 2=Bass, 3=Treble
    
    private AudioRecord recorder;
    private AudioTrack player;
    private Thread audioThread;

    public AudioService(Context context) {
        this.context = context;
    }

    public void iniciarAudifono() {
        if (isRunning) return;
        isRunning = true;

        audioThread = new Thread(() -> {
            try {
                int bufferSize = AudioRecord.getMinBufferSize(sampleRate,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
                bufferSize = Math.max(bufferSize, sampleRate / 10);

                recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);

                player = new AudioTrack(AudioManager.STREAM_MUSIC,
                    sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize, AudioTrack.MODE_STREAM);

                if (recorder.getState() != AudioRecord.STATE_INITIALIZED || 
                    player.getState() != AudioTrack.STATE_INITIALIZED) {
                    return;
                }

                recorder.startRecording();
                player.play();

                short[] buffer = new short[bufferSize];
                float[] floatBuffer = new float[bufferSize];

                while (isRunning) {
                    int read = recorder.read(buffer, 0, buffer.length);
                    if (read <= 0) continue;

                    // Convertir a float
                    for (int i = 0; i < read; i++) {
                        floatBuffer[i] = buffer[i] / 32768.0f;
                    }

                    // Aplicar filtro seleccionado
                    aplicarFiltro(floatBuffer, read);

                    // Amplificación base
                    for (int i = 0; i < read; i++) {
                        floatBuffer[i] *= factorGanancia;
                    }

                    // Compresor dinámico (Limitador de seguridad)
                    for (int i = 0; i < read; i++) {
                        if (floatBuffer[i] > umbralSeguridad) {
                            floatBuffer[i] = umbralSeguridad + 
                                (floatBuffer[i] - umbralSeguridad) / ratioCompresion;
                        } else if (floatBuffer[i] < -umbralSeguridad) {
                            floatBuffer[i] = -umbralSeguridad + 
                                (floatBuffer[i] + umbralSeguridad) / ratioCompresion;
                        }
                    }

                    // Convertir de vuelta a short
                    for (int i = 0; i < read; i++) {
                        floatBuffer[i] = Math.max(-1.0f, Math.min(1.0f, floatBuffer[i]));
                        buffer[i] = (short) (floatBuffer[i] * 32767.0f);
                    }

                    player.write(buffer, 0, read);
                }

                recorder.stop();
                player.stop();
                recorder.release();
                player.release();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        audioThread.start();
    }

    private void aplicarFiltro(float[] buffer, int samples) {
        switch (filtroActual) {
            case 0: // Normal
                break;
            case 1: // Pasa-banda para voz (300-3000 Hz)
                filtroVoz(buffer, samples);
                break;
            case 2: // Bass boost
                filtroGrave(buffer, samples);
                break;
            case 3: // Treble boost
                filtroAgudo(buffer, samples);
                break;
        }
    }

    private void filtroVoz(float[] buffer, int samples) {
        // Filtro simple pasa-banda para voz
        for (int i = 2; i < samples; i++) {
            buffer[i] = 0.5f * buffer[i] + 0.25f * buffer[i-1] + 0.25f * buffer[i-2];
        }
    }

    private void filtroGrave(float[] buffer, int samples) {
        // Boost de bajos
        float boost = 1.0f + bassGain;
        for (int i = 0; i < samples; i++) {
            buffer[i] *= boost;
        }
    }

    private void filtroAgudo(float[] buffer, int samples) {
        // Boost de agudos (diferencia)
        float boost = 1.0f + trebleGain;
        for (int i = 1; i < samples; i++) {
            buffer[i] = buffer[i] + (buffer[i] - buffer[i-1]) * boost;
        }
    }

    public void detenerAudifono() {
        isRunning = false;
        if (audioThread != null) {
            try {
                audioThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setGanancia(float nuevaGanancia) {
        this.factorGanancia = nuevaGanancia;
    }

    public void setTreble(float treble) {
        this.trebleGain = treble;
    }

    public void setBass(float bass) {
        this.bassGain = bass;
    }

    public void setFiltro(int filtro) {
        this.filtroActual = filtro;
    }
}
