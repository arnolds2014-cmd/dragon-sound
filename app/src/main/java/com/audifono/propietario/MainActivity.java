package com.audifono.propietario;

import android.app.Activity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int AUDIO_PERMISSION_REQUEST = 101;
    
    private AudioService audioService;
    private BluetoothManager bluetoothManager;
    private WiFiManager wifiManager;
    
    private SeekBar gainSeekBar;
    private SeekBar trebleSeekBar;
    private SeekBar bassSeekBar;
    private Button startButton;
    private Button stopButton;
    private Button bluetoothButton;
    private Button wifiButton;
    private TextView gainTextView;
    private TextView statusTextView;
    private Spinner filterSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Inicializar componentes
        initializeUI();
        requestPermissions();
        
        // Inicializar servicios
        audioService = new AudioService(this);
        bluetoothManager = new BluetoothManager(this);
        wifiManager = new WiFiManager(this);
    }

    private void initializeUI() {
        gainSeekBar = findViewById(R.id.gainSeekBar);
        trebleSeekBar = findViewById(R.id.trebleSeekBar);
        bassSeekBar = findViewById(R.id.bassSeekBar);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        bluetoothButton = findViewById(R.id.bluetoothButton);
        wifiButton = findViewById(R.id.wifiButton);
        gainTextView = findViewById(R.id.gainTextView);
        statusTextView = findViewById(R.id.statusTextView);
        filterSpinner = findViewById(R.id.filterSpinner);

        // Configurar Spinner de filtros
        String[] filtros = {"Normal", "Voz (Pasa-banda)", "Grave (Bass)", "Agudo (Treble)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, filtros);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);

        // Listeners
        gainSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float ganancia = (progress / 100.0f) * 15.0f; // 0 a 15x (130dB)
                gainTextView.setText(String.format("Ganancia: %.1f x (%d dB)", 
                    ganancia, (int)(20 * Math.log10(ganancia))));
                audioService.setGanancia(ganancia);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        trebleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioService.setTreble((progress - 50) / 100.0f);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        bassSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioService.setBass((progress - 50) / 100.0f);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        startButton.setOnClickListener(v -> {
            audioService.iniciarAudifono();
            statusTextView.setText("Estado: ACTIVO");
            statusTextView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        });

        stopButton.setOnClickListener(v -> {
            audioService.detenerAudifono();
            statusTextView.setText("Estado: DETENIDO");
            statusTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        });

        bluetoothButton.setOnClickListener(v -> {
            bluetoothManager.mostrarDispositivosEmparejados();
        });

        wifiButton.setOnClickListener(v -> {
            wifiManager.mostrarRedes();
        });

        filterSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, 
                    int position, long id) {
                audioService.setFiltro(position);
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private void requestPermissions() {
        // Permisos críticos para Audio
        String[] audioPermissions = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
        };

        String[] bluetoothPermissions = {};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            bluetoothPermissions = new String[]{
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.ACCESS_FINE_LOCATION
            };
        }

        // Verificar y solicitar permisos
        boolean needsRequest = false;
        for (String permission : audioPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) 
                    != PackageManager.PERMISSION_GRANTED) {
                needsRequest = true;
                break;
            }
        }

        if (needsRequest) {
            ActivityCompat.requestPermissions(this, audioPermissions, AUDIO_PERMISSION_REQUEST);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            for (String permission : bluetoothPermissions) {
                if (ContextCompat.checkSelfPermission(this, permission) 
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, bluetoothPermissions, PERMISSION_REQUEST_CODE);
                    break;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        boolean allGranted = true;
        for (int grant : grantResults) {
            if (grant != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }

        if (allGranted) {
            Toast.makeText(this, "Permisos otorgados", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Algunos permisos fueron denegados", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioService != null) {
            audioService.detenerAudifono();
        }
    }
}
