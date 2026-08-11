import React, { useCallback, useState } from 'react';
import { View, Text, ScrollView, StyleSheet, RefreshControl, TouchableOpacity } from 'react-native';
import { useFocusEffect } from '@react-navigation/native';
import { api } from '../api/api';
import { colors } from '../theme/colors';
import { Card, Titulo, Carregando, MensagemErro } from '../components/UI';

const ITENS = [
  { chave: 'clientes', icone: '👤', label: 'Clientes', destino: 'Clientes' },
  { chave: 'fornecedores', icone: '🏭', label: 'Fornecedores', destino: 'Fornecedores' },
  { chave: 'produtos', icone: '📦', label: 'Produtos', destino: 'Estoque' },
  { chave: 'pedidos', icone: '🛒', label: 'Pedidos', destino: 'Pedidos' },
  { chave: 'solicitacoes', icone: '📋', label: 'Solicitações', destino: 'Fornecedores' },
];

export default function DashboardScreen({ navigation }) {
  const [resumo, setResumo] = useState(null);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState(null);

  const carregar = useCallback(async () => {
    setErro(null);
    try {
      const dados = await api.getDashboard();
      setResumo(dados);
    } catch (e) {
      setErro('Não foi possível conectar ao backend. Verifique se a API está no ar e se API_BASE_URL em app.json aponta para o endereço correto.');
    } finally {
      setCarregando(false);
    }
  }, []);

  useFocusEffect(useCallback(() => { carregar(); }, [carregar]));

  return (
    <ScrollView
      style={styles.container}
      refreshControl={<RefreshControl refreshing={carregando} onRefresh={carregar} />}
    >
      <Titulo>🌿 Painel de Controle</Titulo>

      <MensagemErro texto={erro} />
      {carregando && !resumo ? <Carregando /> : null}

      {resumo ? (
        <View style={styles.grid}>
          {ITENS.map((item) => (
            <TouchableOpacity
              key={item.chave}
              style={styles.dashCard}
              onPress={() => navigation.navigate(item.destino)}
            >
              <Text style={styles.icone}>{item.icone}</Text>
              <Text style={styles.label}>{item.label}</Text>
              <Text style={styles.valor}>{resumo[item.chave] ?? 0}</Text>
            </TouchableOpacity>
          ))}
          <TouchableOpacity style={styles.dashCard} onPress={() => navigation.navigate('Relatórios')}>
            <Text style={styles.icone}>📊</Text>
            <Text style={styles.label}>Relatórios</Text>
            <Text style={styles.valor}>6</Text>
          </TouchableOpacity>
        </View>
      ) : null}

      <Card>
        <Text style={{ color: colors.textMuted, fontSize: 13 }}>
          AgroBom · Sistema de Gestão Agrícola{'\n'}
          API: {api.baseUrl}
        </Text>
      </Card>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: colors.background, padding: 16 },
  grid: { flexDirection: 'row', flexWrap: 'wrap', justifyContent: 'space-between', marginBottom: 16 },
  dashCard: {
    width: '48%', backgroundColor: colors.card, borderRadius: 12, padding: 16, marginBottom: 12,
    borderTopWidth: 4, borderTopColor: colors.primary, shadowColor: '#000', shadowOpacity: 0.06,
    shadowRadius: 6, elevation: 2,
  },
  icone: { fontSize: 26, marginBottom: 6 },
  label: { fontSize: 12, color: colors.textMuted, fontWeight: '700', textTransform: 'uppercase' },
  valor: { fontSize: 24, fontWeight: '700', color: colors.primaryDark, marginTop: 4 },
});
