import React, { useCallback, useState } from 'react';
import { View, Text, FlatList, StyleSheet, RefreshControl, Alert } from 'react-native';
import { Picker } from '@react-native-picker/picker';
import { useFocusEffect } from '@react-navigation/native';
import { api } from '../api/api';
import { colors } from '../theme/colors';
import { Card, Titulo, Subtitulo, Campo, Botao, Badge, Carregando, MensagemErro, Vazio } from '../components/UI';

function hojeISO() {
  return new Date().toISOString().slice(0, 10);
}

const FORM_VAZIO = { numero_solicitacao: '', data_solicitacao: hojeISO(), situacao: 'ABERTA', valor_total: '0', id_fornecedor: null };

export default function SolicitacoesScreen() {
  const [solicitacoes, setSolicitacoes] = useState([]);
  const [fornecedores, setFornecedores] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState(null);
  const [form, setForm] = useState(FORM_VAZIO);
  const [salvando, setSalvando] = useState(false);

  const carregar = useCallback(async () => {
    setErro(null);
    try {
      const [listaSolic, listaForn] = await Promise.all([api.getSolicitacoes(), api.getFornecedores()]);
      setSolicitacoes(listaSolic);
      setFornecedores(listaForn);
      setForm((f) => ({ ...f, id_fornecedor: f.id_fornecedor ?? (listaForn[0]?.id_fornecedor ?? null) }));
    } catch (e) {
      setErro(e.message);
    } finally {
      setCarregando(false);
    }
  }, []);

  useFocusEffect(useCallback(() => { carregar(); }, [carregar]));

  async function salvar() {
    if (!form.numero_solicitacao || !form.data_solicitacao || !form.id_fornecedor) {
      Alert.alert('Campos obrigatórios', 'Preencha número, data e selecione um fornecedor.');
      return;
    }
    setSalvando(true);
    try {
      await api.salvarSolicitacao({
        numero_solicitacao: parseInt(form.numero_solicitacao, 10),
        data_solicitacao: form.data_solicitacao,
        situacao: form.situacao,
        valor_total: parseFloat((form.valor_total || '0').replace(',', '.')),
        id_fornecedor: form.id_fornecedor,
      });
      setForm({ ...FORM_VAZIO, id_fornecedor: form.id_fornecedor });
      carregar();
    } catch (e) {
      Alert.alert('Erro ao salvar', e.message);
    } finally {
      setSalvando(false);
    }
  }

  function confirmarRemocao(s) {
    Alert.alert('Remover solicitação', `Remover a solicitação nº ${s.numero_solicitacao}?`, [
      { text: 'Cancelar', style: 'cancel' },
      { text: 'Remover', style: 'destructive', onPress: () => remover(s.id_solicitacao) },
    ]);
  }

  async function remover(id) {
    try {
      await api.removerSolicitacao(id);
      carregar();
    } catch (e) {
      Alert.alert('Erro ao remover', e.message);
    }
  }

  function nomeFornecedor(id) {
    const f = fornecedores.find((x) => x.id_fornecedor === id);
    return f ? f.nome : `#${id}`;
  }

  return (
    <FlatList
      style={styles.container}
      refreshControl={<RefreshControl refreshing={carregando} onRefresh={carregar} />}
      data={solicitacoes}
      keyExtractor={(item) => String(item.id_solicitacao)}
      ListHeaderComponent={
        <View>
          <Titulo>📋 Solicitações de Compra</Titulo>
          <MensagemErro texto={erro} />
          <Card>
            <Subtitulo>➕ Nova Solicitação</Subtitulo>
            <Campo label="Número da solicitação" placeholder="Ex: 501" keyboardType="number-pad"
                   value={form.numero_solicitacao} onChangeText={(v) => setForm({ ...form, numero_solicitacao: v })} />
            <Campo label="Data (AAAA-MM-DD)" value={form.data_solicitacao}
                   onChangeText={(v) => setForm({ ...form, data_solicitacao: v })} />

            <Text style={styles.label}>Fornecedor</Text>
            {fornecedores.length === 0 ? (
              <Text style={styles.aviso}>Cadastre um fornecedor antes de criar uma solicitação.</Text>
            ) : (
              <View style={styles.pickerWrap}>
                <Picker selectedValue={form.id_fornecedor}
                        onValueChange={(v) => setForm({ ...form, id_fornecedor: v })}>
                  {fornecedores.map((f) => (
                    <Picker.Item key={f.id_fornecedor} label={`${f.nome} — ${f.cnpj}`} value={f.id_fornecedor} />
                  ))}
                </Picker>
              </View>
            )}

            <Text style={styles.label}>Situação</Text>
            <View style={styles.pickerWrap}>
              <Picker selectedValue={form.situacao} onValueChange={(v) => setForm({ ...form, situacao: v })}>
                <Picker.Item label="Aberta" value="ABERTA" />
                <Picker.Item label="Encerrada" value="ENCERRADA" />
              </Picker>
            </View>

            <Campo label="Valor total (R$)" placeholder="0,00" keyboardType="decimal-pad" value={form.valor_total}
                   onChangeText={(v) => setForm({ ...form, valor_total: v })} />
            <Botao title={salvando ? 'Salvando...' : '✅ Registrar Solicitação'} onPress={salvar} disabled={salvando} />
          </Card>
          <Subtitulo>📋 Solicitações registradas</Subtitulo>
          {carregando && solicitacoes.length === 0 ? <Carregando /> : null}
        </View>
      }
      ListEmptyComponent={!carregando ? <Vazio texto="Nenhuma solicitação registrada." /> : null}
      renderItem={({ item }) => (
        <Card>
          <View style={styles.linhaTopo}>
            <Text style={styles.nome}>Solicitação nº {item.numero_solicitacao}</Text>
            <Badge ok={item.situacao === 'ENCERRADA'} textoOk="Encerrada" textoAlerta="Aberta" />
          </View>
          <Text style={styles.info}>Fornecedor: {nomeFornecedor(item.id_fornecedor)}</Text>
          <Text style={styles.valorTotal}>Total: R$ {Number(item.valor_total).toFixed(2)}</Text>
          <Botao title="🗑️ Remover" variant="danger" onPress={() => confirmarRemocao(item)} style={{ marginTop: 8 }} />
        </Card>
      )}
      contentContainerStyle={{ padding: 16 }}
    />
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: colors.background },
  linhaTopo: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' },
  nome: { fontSize: 16, fontWeight: '700', color: colors.text },
  info: { color: colors.textMuted, marginTop: 4, fontSize: 13 },
  valorTotal: { marginTop: 6, fontWeight: '700', color: colors.primaryDark, fontSize: 15 },
  label: { fontSize: 12, fontWeight: '600', color: '#555', marginBottom: 4, textTransform: 'uppercase' },
  pickerWrap: { borderWidth: 1.5, borderColor: '#ddd', borderRadius: 8, marginBottom: 12, backgroundColor: '#fafafa' },
  aviso: { color: colors.warning, marginBottom: 12, fontSize: 13 },
});
