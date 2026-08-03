import React, { useState } from 'react';
import { StyleSheet, Text, View, TouchableOpacity, Switch } from 'react-native';
import { StatusBar } from 'expo-status-bar';

export default function App() {
  const [isEnabled, setIsEnabled] = useState(false);
  const [volume, setVolume] = useState(50);

  const toggleSwitch = () => setIsEnabled(previousState => !previousState);

  const ajustarVolumen = (direccion) => {
    setVolume(prev => {
      if (direccion === 'subir') return Math.min(prev + 10, 100);
      return Math.max(prev - 10, 0);
    });
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Dragon Sound 🐉</Text>
      <Text style={styles.subtitle}>Amplificador Auditivo Propietario</Text>

      {/* Tarjeta de Control Principal */}
      <View style={styles.card}>
        <Text style={styles.cardText}>
          Estado del Dispositivo: {isEnabled ? '🟢 ACTIVO' : '🔴 INACTIVO'}
        </Text>
        <Switch
          trackColor={{ false: '#767577', true: '#0055ff' }}
          thumbColor={isEnabled ? '#ffffff' : '#f4f3f4'}
          ios_backgroundColor="#3e3e3e"
          onValueChange={toggleSwitch}
          value={isEnabled}
        />
      </View>

      {/* Control de Volumen */}
      <View style={[styles.card, { opacity: isEnabled ? 1 : 0.4 }]}>
        <Text style={styles.cardText}>Nivel de Ganancia: {volume}%</Text>
        <View style={styles.buttonRow}>
          <TouchableOpacity 
            style={styles.actionButton} 
            disabled={!isEnabled}
            onPress={() => ajustarVolumen('bajar')}
          >
            <Text style={styles.btnText}>-</Text>
          </TouchableOpacity>
          
          <TouchableOpacity 
            style={styles.actionButton} 
            disabled={!isEnabled}
            onPress={() => ajustarVolumen('subir')}
          >
            <Text style={styles.btnText}>+</Text>
          </TouchableOpacity>
        </View>
      </View>

      {/* Sección de Estado Técnico */}
      <Text style={styles.footer}>Permisos de Micrófono Otorgados</Text>
      <StatusBar style="light" />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#121212',
    alignItems: 'center',
    justifyContent: 'center',
    padding: 24,
  },
  title: {
    fontSize: 32,
    fontWeight: '800',
    color: '#fff',
    marginBottom: 4,
  },
  subtitle: {
    fontSize: 16,
    color: '#0055ff',
    fontWeight: '600',
    marginBottom: 40,
    textAlign: 'center',
  },
  card: {
    width: '100%',
    backgroundColor: '#1e1e1e',
    borderRadius: 16,
    padding: 20,
    alignItems: 'center',
    justifyContent: 'space-between',
    flexDirection: 'row',
    marginBottom: 20,
    borderWidth: 1,
    borderColor: '#2a2a2a',
  },
  cardText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '500',
  },
  buttonRow: {
    flexDirection: 'row',
    gap: 12,
  },
  actionButton: {
    backgroundColor: '#0055ff',
    width: 44,
    height: 44,
    borderRadius: 12,
    alignItems: 'center',
    justifyContent: 'center',
  },
  btnText: {
    color: '#fff',
    fontSize: 24,
    fontWeight: 'bold',
  },
  footer: {
    position: 'absolute',
    bottom: 40,
    fontSize: 12,
    color: '#666',
  },
});
