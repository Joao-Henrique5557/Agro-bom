import React, { useState } from 'react';
import { View, Text, ScrollView, StyleSheet, ActivityIndicator } from 'react-native';
import { api } from '../api/api';
import { colors } from '../theme/colors';
import { Card, Titulo, Subtitulo, Campo, Botao, MensagemErro, Vazio } from '../components/UI';

const RELATORIOS = [
  { id: '1', titulo: '📦 Posição de Estoque', precisaPeriodo: false },
  { id: '2', titulo: '🛒 Pedidos por Mês', precisaPeriodo: 'mes' },
  { id: '3', titulo: '📅 Pedidos por Intervalo de Datas', precisaPeriodo: 'intervalo' },
  { id: '4', titulo: '🏭 Fornecedores por Produto', precisaPeriodo: false },
  { id: '5', titulo: '📋 Solicitações por Mês', precisaPeriodo: 'mes' },
  { id: '6', titulo: '💰 Volume Financeiro (12 meses)', precisaPeriodo: false },
];

function hoje() {
  const d = new Date();
  return { mes: String(d.getMonth() + 1), ano: String(d.getFullYear()) };
}

export default function RelatoriosScreen() {
  const [selecionado, setSelecionado] = useState(null);
  const [mes, setMes] = useState(hoje().mes);
  const [ano, setAno] = useState(hoje().ano);
  const [dataInicio, setDataInicio] = useState('');
  const [dataFim, setDataFim] = useState('');
  const [linhas, setLinhas] = useState(null);
  const [carregando, setCarregando] = useState(false);
  const [erro, setErro] = useState(null);

  async function gerar(rel) {
    setSelecionado(rel.id);
    setErro(null);
    setLinhas(null);
    setCarregando(true);
    try {
      let params = {};
      if (rel.precisaPeriodo === 'mes') {
        params = rel.id === '2' ? { mes, ano } : { mes5: mes, ano5: ano };
      } else if (rel.precisaPeriodo === 'intervalo') {
        if (!dataInicio || !dataFim) {
          setErro('Informe data início e data fim.');
          setCarregando(false);
          return;
        }
        params = { data_inicio: dataInicio, data_fim: dataFim };
      }
      const dados = await api.getRelatorio(rel.id, params);
      setLinhas(dados);
    } catch (e) {
      setErro(e.message);
    } finally {
      setCarregando(false);
    }
  }

  const relatorioAtivo = RELATORIOS.find((r) => r.id === selecionado);

  return (
    <ScrollView style={styles.container} contentContainerStyle={{ padding: 16 }}>
      <Titulo>📊 Relatórios Gerenciais</Titulo>

      {RELATORIOS.map((rel) => (
        <Card key={rel.id}>
          <Subtitulo>{rel.titulo}</Subtitulo>

          {rel.precisaPeriodo === 'mes' && selecionado === rel.id ? (
            <View style={styles.linha}>
              <Campo label="Mês" keyboardType="number-pad" value={mes} onChangeText={setMes} style={{ flex: 1 }} />
              <View style={{ width: 10 }} />
              <Campo label="Ano" keyboardType="number-pad" value={ano} onChangeText={setAno} style={{ flex: 1 }} />
            </View>
          ) : null}

          {rel.precisaPeriodo === 'intervalo' && selecionado === rel.id ? (
            <View>
              <Campo label="Data início (AAAA-MM-DD)" value={dataInicio} onChangeText={setDataInicio} />
              <Campo label="Data fim (AAAA-MM-DD)" value={dataFim} onChangeText={setDataFim} />
            </View>
          ) : null}

          <Botao title="🔍 Gerar Relatório" variant="info" onPress={() => gerar(rel)} />

          {selecionado === rel.id ? (
            <View style={{ marginTop: 12 }}>
              <MensagemErro texto={erro} />
              {carregando ? <ActivityIndicator color={colors.primary} /> : null}
              {!carregando && linhas ? (
                linhas.length === 0 ? (
                  <Vazio texto="Sem dados para o período/consulta." />
                ) : (
                  linhas.map((linha, idx) => (
                    <View key={idx} style={styles.linhaResultado}>
                      {Object.entries(linha).map(([chave, valor]) => (
                        <Text key={chave} style={styles.campoResultado}>
                          <Text style={styles.chaveResultado}>{chave}: </Text>
                          {String(valor)}
                        </Text>
                      ))}
                    </View>
                  ))
                )
              ) : null}
            </View>
          ) : null}
        </Card>
      ))}
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: colors.background },
  linha: { flexDirection: 'row' },
  linhaResultado: {
    borderTopWidth: 1, borderTopColor: colors.border, paddingVertical: 8,
  },
  campoResultado: { fontSize: 13, color: colors.text },
  chaveResultado: { fontWeight: '700', color: colors.primaryDark },
});
