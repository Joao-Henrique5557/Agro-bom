import React, { useCallback, useState } from 'react';
import { View, Text, FlatList, StyleSheet, RefreshControl, Alert } from 'react-native';
import { useFocusEffect } from '@react-navigation/native';
import { api } from '../api/api';
import { colors } from '../theme/colors';
import { Card, Titulo, Subtitulo, Campo, Botao, Carregando, MensagemErro, Vazio } from '../components/UI';

const FORM_VAZIO = { nome: '', cnpj: '', telefone: '' };

export default function FornecedoresScreen() {
  const [fornecedores, setFornecedores] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState(null);
  const [form, setForm] = useState(FORM_VAZIO);
  const [salvando, setSalvando] = useState(false);

  const carregar = useCallback(async () => {
    setErro(null);
    try {
      setFornecedores(await api.getFornecedores());
    } catch (e) {
      setErro(e.message);
    } finally {
      setCarregando(false);
    }
  }, []);

  useFocusEffect(useCallback(() => { carregar(); }, [carregar]));

  async function salvar() {
    if (!form.nome || !form.cnpj || !form.telefone) {
      Alert.alert('Campos obrigatórios', 'Preencha nome, CNPJ e telefone.');
      return;
    }
    setSalvando(true);
    try {
      await api.salvarFornecedor(form);
      setForm(FORM_VAZIO);
      carregar();
    } catch (e) {
      Alert.alert('Erro ao salvar', e.message);
    } finally {
      setSalvando(false);
    }
  }

  function confirmarRemocao(fornecedor) {
    Alert.alert('Remover fornecedor', `Remover "${fornecedor.nome}"?`, [
      { text: 'Cancelar', style: 'cancel' },
      { text: 'Remover', style: 'destructive', onPress: () => remover(fornecedor.id_fornecedor) },
    ]);
  }

  async function remover(id) {
    try {
      await api.removerFornecedor(id);
      carregar();
    } catch (e) {
      Alert.alert('Erro ao remover', e.message);
    }
  }

  return (
    <FlatList
      style={styles.container}
      refreshControl={<RefreshControl refreshing={carregando} onRefresh={carregar} />}
      data={fornecedores}
      keyExtractor={(item) => String(item.id_fornecedor)}
      ListHeaderComponent={
        <View>
          <Titulo>🏭 Fornecedores</Titulo>
          <MensagemErro texto={erro} />
          <Card>
            <Subtitulo>➕ Novo Fornecedor</Subtitulo>
            <Campo label="Nome" placeholder="Razão social" value={form.nome}
                   onChangeText={(v) => setForm({ ...form, nome: v })} />
            <Campo label="CNPJ" placeholder="00.000.000/0000-00" value={form.cnpj}
                   onChangeText={(v) => setForm({ ...form, cnpj: v })} />
            <Campo label="Telefone" placeholder="(00) 00000-0000" value={form.telefone}
                   onChangeText={(v) => setForm({ ...form, telefone: v })} />
            <Botao title={salvando ? 'Salvando...' : '✅ Cadastrar'} onPress={salvar} disabled={salvando} />
          </Card>
          <Subtitulo>📋 Fornecedores cadastrados</Subtitulo>
          {carregando && fornecedores.length === 0 ? <Carregando /> : null}
        </View>
      }
      ListEmptyComponent={!carregando ? <Vazio texto="Nenhum fornecedor cadastrado." /> : null}
      renderItem={({ item }) => (
        <Card>
          <Text style={styles.nome}>{item.nome}</Text>
          <Text style={styles.info}>CNPJ: {item.cnpj}</Text>
          <Text style={styles.info}>Tel: {item.telefone}</Text>
          <Botao title="🗑️ Remover" variant="danger" onPress={() => confirmarRemocao(item)} style={{ marginTop: 8 }} />
        </Card>
      )}
      contentContainerStyle={{ padding: 16 }}
    />
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: colors.background },
  nome: { fontSize: 16, fontWeight: '700', color: colors.text },
  info: { color: colors.textMuted, marginTop: 2, fontSize: 13 },
});
