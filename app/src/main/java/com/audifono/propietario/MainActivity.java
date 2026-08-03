package com.audifono.propietario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private boolean isAmplificadorEncendido = false;
    private Button btnControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnControl = findViewById(R.id.btn_power); // Asegúrate de que coincida con el ID de tu botón en XML

        btnControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(MainActivity.this, AudioService.class);
                
                if (!isAmplificadorEncendido) {
                    // Enciende el amplificador en segundo plano protegido
                    serviceIntent.setAction("START");
                    startService(serviceIntent);
                    btnControl.setText("AMPLIFICADOR ACTIVO (SEGUNDO PLANO)");
                    isAmplificadorEncendido = true;
                } else {
                    // Apaga el sistema por completo
                    serviceIntent.setAction("STOP");
                    startService(serviceIntent);
                    btnControl.setText("AMPLIFICADOR APAGADO");
                    isAmplificadorEncendido = false;
                }
            }
        });
    }
}
