package com.audifono.propietario;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private boolean isAmplificadorEncendido = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Conserva exactamente tu diseño visual original sin romper sliders ni botones
        setContentView(R.layout.activity_main);
    }

    // Esta función se activa automáticamente al presionar el interruptor de encendido
    public void alternarAmplificador() {
        Intent serviceIntent = new Intent(this, AudioService.class);
        
        if (!isAmplificadorEncendido) {
            // Lanza el servicio protegido para que el audio no se apague al salir de la app
            serviceIntent.setAction("START");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
            isAmplificadorEncendido = true;
        } else {
            // Detiene el servicio y apaga el micrófono inmediatamente
            serviceIntent.setAction("STOP");
            startService(serviceIntent);
            isAmplificadorEncendido = false;
        }
    }
}
