package com.audifono.propietario;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

public class AudioService extends Service {
    private static final String CHANNEL_ID = "DragonSoundChannel";
    private static final int NOTIFICATION_ID = 101;
    private boolean isRunning = false;

    static {
        // Carga tu motor de procesamiento en C++ (sound.cpp)
        System.loadLibrary("sound");
    }

    // Declaración de las funciones nativas de tu archivo C++
    public native void startEngine();
    public native void stopEngine();

    @Override
    public void onCreate() {
        super.onCreate();
        crearCanalNotificacion();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent != null ? intent.getAction() : null;

        if ("START".equals(action) && !isRunning) {
            isRunning = true;
            
            // 1. Despierta el micrófono y los filtros de C++ de ultra baja latencia
            startEngine(); 

            // 2. Registra el servicio ante Android con el Banner de Control Persistente
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Dragon Sound 🐉")
                    .setContentText("Amplificador Auditivo Activo en Segundo Plano")
                    .setSmallIcon(android.R.drawable.ic_media_play)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true) // Impide que el usuario lo borre deslizando
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build();

            startForeground(NOTIFICATION_ID, notification);

        } else if ("STOP".equals(action)) {
            isRunning = false;
            stopEngine(); // Apaga el micrófono de inmediato
            stopForeground(true);
            stopSelf();
        }

        return START_NOT_STICKY;
    }

    private void crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Asistencia Auditiva",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
