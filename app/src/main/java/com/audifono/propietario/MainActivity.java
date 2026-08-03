package com.audifono.propietario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnActivar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Carga tu interfaz visual original intacta
        setContentView(R.layout.activity_main);

        // Enlace físico directo con el botón "▶ ACTIVAR" de tu XML
        btnActivar = findViewById(R.id.startButton);

        if (btnActivar != null) {
            btnActivar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent serviceIntent = new Intent(MainActivity.this, AudioService.class);
                    serviceIntent.setAction("START");
                    
                    // Lanza el servicio protegido para que el audio no se apague al salir de la app
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        startForegroundService(serviceIntent);
                    } else {
                        startService(serviceIntent);
                    }
                }
            });
        }
    }
}
