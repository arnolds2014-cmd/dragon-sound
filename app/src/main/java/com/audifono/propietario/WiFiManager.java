package com.audifono.propietario;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiNetworkSpecifier;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;
import android.widget.Toast;

import java.util.List;

public class WiFiManager {
    private Context context;
    private WifiManager wifiManager;
    private ConnectivityManager connectivityManager;

    public WiFiManager(Context context) {
        this.context = context;
        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public void mostrarRedes() {
        if (wifiManager == null) {
            Toast.makeText(context, "WiFi no disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        // Escanear redes disponibles
        wifiManager.startScan();
        List<ScanResult> results = wifiManager.getScanResults();

        if (results.isEmpty()) {
            Toast.makeText(context, "No se encontraron redes WiFi", Toast.LENGTH_SHORT).show();
        } else {
            StringBuilder networkList = new StringBuilder("Redes WiFi disponibles:\n");
            for (ScanResult result : results) {
                networkList.append(result.SSID).append(" (")
                    .append(result.level).append(" dBm)\n");
            }
            Toast.makeText(context, networkList.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void conectarARedWiFi(String ssid, String password) {
        if (wifiManager == null) {
            Toast.makeText(context, "WiFi no disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+: Usar WifiNetworkSpecifier
                WifiNetworkSpecifier specifier = new WifiNetworkSpecifier.Builder()
                    .setSsid(ssid)
                    .setWpa2Passphrase(password)
                    .build();

                NetworkRequest request = new NetworkRequest.Builder()
                    .addTransportType(android.net.NetworkCapabilities.TRANSPORT_WIFI)
                    .setNetworkSpecifier(specifier)
                    .build();

                connectivityManager.requestNetwork(request, 
                    new ConnectivityManager.NetworkCallback());
                
                Toast.makeText(context, "Conectando a WiFi: " + ssid, Toast.LENGTH_SHORT).show();
            } else {
                // Android 9 y anterior
                Toast.makeText(context, "WiFi manual requerido para esta versión", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al conectar WiFi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public String obtenerSSIDConectado() {
        if (wifiManager == null) return null;

        try {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                return wifiInfo.getSSID().replace("\"", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void activarWiFi() {
        if (wifiManager != null && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
            Toast.makeText(context, "Activando WiFi...", Toast.LENGTH_SHORT).show();
        }
    }

    public void desactivarWiFi() {
        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
            Toast.makeText(context, "Desactivando WiFi...", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean estaWiFiActivo() {
        return wifiManager != null && wifiManager.isWifiEnabled();
    }
}
