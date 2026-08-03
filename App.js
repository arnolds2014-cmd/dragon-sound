import React, { useState } from 'react';
import { StyleSheet, Text, View, TouchableOpacity, Switch, NativeModules } from 'react-native';
import { StatusBar } from 'expo-status-bar';

// Enlace directo al motor en C++ (sound.cpp)
const { AudioEngine } = NativeModules;

export default function App() {
  const [isActive, setIsActive] = useState(false);
  const [decibels, setDecibels] = useState(50);
  const [voiceFocus, setVoiceFocus] = useState(true);
  const [wirelessMode, setWirelessMode] = useState('Local'); // Local, Bluetooth, Wi-Fi

  const handleToggleEngine = () => {
    if (!isActive) {
      AudioEngine?.startEngine();
      setIsActive(true);
    } else {
      AudioEngine?.stopEngine();
      setIsActive(false);
    }
  };

  const modificarDecibelios = (direccion) => {
    setDecibels(prev => {
      let nuevoValor = direccion === 'subir' ? prev + 5 : prev - 5;
      if (nuevoValor > 130) nuevoValor = 130; // Techo clínico para hipoacusia profunda
      if (nuevoValor < 0) nuevoValor = 0;
      
      // Envía los decibelios en tiempo real al algoritmo de sound.cpp
      AudioEngine?.setGainNative(nuevoValor);
      return nuevoValor;
    });
  };

  const toggleVoiceFocus = () => {
    const nuevoEstado = !voiceFocus;
    setVoiceFocus(nuevoEstado);
    // Activa o desactiva la compuerta y el filtro en sound.cpp
    AudioEngine?.toggleNoiseFilter(nuevoEstado);
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Dragon Sound 🐉</Text>
      <Text style={styles.subtitle}>Sistemas de Asistencia Auditiva Avanzada</Text>

      {/* Botón de Encendido Médico */}
      <TouchableOpacity 
        style={[styles.powerButton, isActive ? styles.powerOn : styles.powerOff]} 
        onPress={handleToggleEngine}
      >
        <Text style={styles.powerButtonText}>{isActive ? 'AMPLIFICADOR ACTIVO' : 'AMPLIFICADOR APAGADO'}</Text>
      </TouchableOpacity>

      {/* Tarjeta de Potencia en Decibelios (Hasta 130 dB) */}
      <View style={[styles.card, !isActive && styles.disabledCard]}>
        <View style={styles.cardHeader}>
          <Text style={styles.cardTitle}>Salida Dinámica</Text>
          <Text style={styles.dbText}>{decibels} dB SPL</Text>
        </View>
        <Text style={styles.warningText}>{decibels >= 90 ? '⚠️ Nivel Clínico para Sordera Profunda' : 'Ganancia Estándar'}</Text>
        <View style={styles.buttonRow}>
          <TouchableOpacity style={styles.actionButton} disabled={!isActive} onPress={() => modificarDecibelios('bajar')}>
            <Text style={styles.btnText}>-</Text>
          </TouchableOpacity>
          <TouchableOpacity style={styles.actionButton} disabled={!isActive} onPress={() => modificarDecibelios('subir')}>
            <Text style={styles.btnText}>+</Text>
          </TouchableOpacity>
        </View>
      </View>

      {/* Filtro de Aislamiento de Frecuencias Vocales */}
      <View style={styles.card}>
        <View style={styles.row}>
          <View>
            <Text style={styles.cardTitle}>Enfoque de Voz IA</Text>
            <Text style={styles.cardDesc}>Filtro de ruido pasa-banda (250Hz - 4kHz)</Text>
          </View>
          <Switch
            trackColor={{ false: '#333', true: '#0055ff' }}
            thumbColor="#fff"
            onValueChange={toggleVoiceFocus}
            value={voiceFocus}
          />
        </View>
      </View>

      {/* Selector de Conectividad Inalámbrica */}
      <View style={styles.card}>
        <Text style={styles.cardTitle}>Enrutamiento de Señal Digital</Text>
        <View style={styles.selectorRow}>
          {['Local', 'Bluetooth', 'Wi-Fi'].map((mode) => (
            <TouchableOpacity 
              key={mode} 
              style={[styles.selectorButton, wirelessMode === mode && styles.selectorActive]}
              onPress={() => setWirelessMode(mode)}
            >
              <Text style={[styles.selectorText, wirelessMode === mode && styles.activeSelectorText]}>{mode}</Text>
            </TouchableOpacity>
          ))}
        </View>
      </View>

      <Text style={styles.footer}>Baja Latencia Activa • JNI OpenSL Engine</Text>
      <StatusBar style="light" />
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#0d0d0d', padding: 24, justifyContent: 'center' },
  title: { fontSize: 30, fontWeight: '900', color: '#fff', textAlign: 'center' },
  subtitle: { fontSize: 13, color: '#0055ff', textAlign: 'center', marginBottom: 30, fontWeight: '600', letterSpacing: 1 },
  powerButton: { width: '100%', padding: 20, borderRadius: 16, alignItems: 'center', marginBottom: 24 },
  powerOn: { backgroundColor: '#00cc66' },
  powerOff: { backgroundColor: '#1a1a1a', borderWidth: 1, borderColor: '#333' },
  powerButtonText: { color: '#fff', fontWeight: '800', fontSize: 16, letterSpacing: 1 },
  card: { backgroundColor: '#141414', borderRadius: 20, padding: 20, marginBottom: 16, borderWidth: 1, borderColor: '#222' },
  disabledCard: { opacity: 0.4 },
  cardHeader: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' },
  cardTitle: { color: '#fff', fontSize: 17, fontWeight: '700' },
  cardDesc: { color: '#666', fontSize: 12, marginTop: 2 },
  dbText: { color: '#0055ff', fontSize: 28, fontWeight: '900' },
  warningText: { color: '#ff9900', fontSize: 12, marginTop: 4, fontWeight: '600' },
  buttonRow: { flexDirection: 'row', justifyContent: 'flex-end', gap: 12, marginTop: 15 },
  actionButton: { backgroundColor: '#222', width: 50, height: 50, borderRadius: 14, alignItems: 'center', justifyContent: 'center', borderWidth: 1, borderColor: '#333' },
  btnText: { color: '#fff', fontSize: 24, fontWeight: '400' },
  row: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' },
  selectorRow: { flexDirection: 'row', gap: 10, marginTop: 12 },
  selectorButton: { flex: 1, padding: 12, borderRadius: 10, backgroundColor: '#1c1c1c', alignItems: 'center' },
  selectorActive: { backgroundColor: '#0055ff' },
  selectorText: { color: '#666', fontWeight: '700', fontSize: 13 },
  activeSelectorText: { color: '#fff' },
  footer: { textAlign: 'center', color: '#444', fontSize: 11, marginTop: 20, fontWeight: '500' }
});
