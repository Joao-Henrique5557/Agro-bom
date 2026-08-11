import React from 'react';
import { View, Text, TextInput, TouchableOpacity, StyleSheet, ActivityIndicator } from 'react-native';
import { colors } from '../theme/colors';

export function Card({ children, style }) {
  return <View style={[styles.card, style]}>{children}</View>;
}

export function Titulo({ children }) {
  return <Text style={styles.titulo}>{children}</Text>;
}

export function Subtitulo({ children }) {
  return <Text style={styles.subtitulo}>{children}</Text>;
}

export function Campo({ label, ...props }) {
  return (
    <View style={styles.campoWrap}>
      {label ? <Text style={styles.label}>{label}</Text> : null}
      <TextInput style={styles.input} placeholderTextColor="#999" {...props} />
    </View>
  );
}

export function Botao({ title, onPress, variant = 'primary', style, disabled }) {
  const bg =
    variant === 'primary' ? colors.primary :
    variant === 'danger' ? colors.danger :
    variant === 'info' ? colors.accent :
    variant === 'warning' ? colors.warning : colors.textMuted;

  return (
    <TouchableOpacity
      onPress={onPress}
      disabled={disabled}
      style={[styles.botao, { backgroundColor: bg, opacity: disabled ? 0.6 : 1 }, style]}
    >
      <Text style={styles.botaoTexto}>{title}</Text>
    </TouchableOpacity>
  );
}

export function Badge({ ok, textoOk = 'Normal', textoAlerta = 'Crítico' }) {
  return (
    <View style={[styles.badge, { backgroundColor: ok ? colors.badgeOkBg : colors.badgeAbertaBg }]}>
      <Text style={{ color: ok ? colors.badgeOkText : colors.badgeAbertaText, fontWeight: '700', fontSize: 12 }}>
        {ok ? `✔ ${textoOk}` : `⚠️ ${textoAlerta}`}
      </Text>
    </View>
  );
}

export function Carregando() {
  return (
    <View style={{ padding: 30, alignItems: 'center' }}>
      <ActivityIndicator size="large" color={colors.primary} />
    </View>
  );
}

export function MensagemErro({ texto }) {
  if (!texto) return null;
  return (
    <View style={styles.erroBox}>
      <Text style={{ color: colors.danger }}>{texto}</Text>
    </View>
  );
}

export function Vazio({ texto = 'Nenhum registro encontrado.' }) {
  return <Text style={styles.vazio}>{texto}</Text>;
}

const styles = StyleSheet.create({
  card: {
    backgroundColor: colors.card,
    borderRadius: 12,
    padding: 16,
    marginBottom: 16,
    shadowColor: '#000',
    shadowOpacity: 0.06,
    shadowRadius: 6,
    shadowOffset: { width: 0, height: 2 },
    elevation: 2,
  },
  titulo: { fontSize: 22, fontWeight: '700', color: colors.primaryDark, marginBottom: 12 },
  subtitulo: {
    fontSize: 16, fontWeight: '700', color: colors.primary, marginBottom: 10,
    borderBottomWidth: 2, borderBottomColor: '#e8f5e9', paddingBottom: 6,
  },
  campoWrap: { marginBottom: 12 },
  label: { fontSize: 12, fontWeight: '600', color: '#555', marginBottom: 4, textTransform: 'uppercase' },
  input: {
    borderWidth: 1.5, borderColor: '#ddd', borderRadius: 8, paddingHorizontal: 12,
    paddingVertical: 9, fontSize: 15, backgroundColor: '#fafafa', color: colors.text,
  },
  botao: { borderRadius: 8, paddingVertical: 11, paddingHorizontal: 18, alignItems: 'center' },
  botaoTexto: { color: '#fff', fontWeight: '700', fontSize: 14 },
  badge: { borderRadius: 20, paddingHorizontal: 10, paddingVertical: 3, alignSelf: 'flex-start' },
  erroBox: { backgroundColor: '#ffebee', borderLeftWidth: 4, borderLeftColor: colors.danger, padding: 10, borderRadius: 6, marginBottom: 12 },
  vazio: { color: '#aaa', fontStyle: 'italic', textAlign: 'center', padding: 20 },
});
