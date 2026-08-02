package com.audifono.propietario;

import android.content.Context;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothManager {
    private Context context;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private boolean isConnected = false;

    // UUID para puerto serie Bluetooth
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothManager(Context context) {
        this.context = context;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void mostrarDispositivosEmparejados() {
        if (bluetoothAdapter == null) {
            Toast.makeText(context, "Bluetooth no disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(context, 
                    android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Permiso Bluetooth denegado", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        
        if (pairedDevices.isEmpty()) {
            Toast.makeText(context, "No hay dispositivos emparejados", Toast.LENGTH_SHORT).show();
        } else {
            StringBuilder deviceList = new StringBuilder("Dispositivos encontrados:\n");
            for (BluetoothDevice device : pairedDevices) {
                deviceList.append(device.getName()).append(" (").append(device.getAddress()).append(")\n");
            }
            Toast.makeText(context, deviceList.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void conectarADispositivo(String deviceAddress) {
        if (bluetoothAdapter == null) {
            Toast.makeText(context, "Bluetooth no disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(context, 
                    android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Permiso Bluetooth denegado", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        try {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
            bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothSocket.connect();
            
            inputStream = bluetoothSocket.getInputStream();
            outputStream = bluetoothSocket.getOutputStream();
            isConnected = true;
            
            Toast.makeText(context, "Conectado a Bluetooth", Toast.LENGTH_SHORT).show();
            
            // Iniciar lectura de datos
            iniciarLecturaBluetooth();
            
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al conectar Bluetooth: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void iniciarLecturaBluetooth() {
        new Thread(() -> {
            byte[] buffer = new byte[1024];
            while (isConnected) {
                try {
                    int bytes = inputStream.read(buffer);
                    // Procesar datos recibidos
                    String data = new String(buffer, 0, bytes);
                    // Aquí se pueden enviar comandos de control
                } catch (Exception e) {
                    e.printStackTrace();
                    isConnected = false;
                }
            }
        }).start();
    }

    public void enviarDatos(String datos) {
        if (isConnected && outputStream != null) {
            try {
                outputStream.write(datos.getBytes());
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void desconectar() {
        isConnected = false;
        try {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (bluetoothSocket != null) bluetoothSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean estaConectado() {
        return isConnected;
    }
}
