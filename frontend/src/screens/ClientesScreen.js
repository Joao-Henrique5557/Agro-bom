import React, { useCallback, useState } from 'react';
import { View, Text, FlatList, StyleSheet, RefreshControl, Alert } from 'react-native';
import { useFocusEffect } from '@react-navigation/native';
import { api } from '../api/api';
import { colors } from '../theme/colors';
import { Card, Titulo, Subtitulo, Campo, Botao, Carregando, MensagemErro, Vazio } from '../components/UI';

const FORM_VAZIO = { cpf: '', nome: '', telefone: '', endereco: '' };

export default function ClientesScreen() {
  const [clientes, setClientes] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState(null);
  const [form, setForm] = useState(FORM_VAZIO);
  const [salvando, setSalvando] = useState(false);

  const carregar = useCallback(async () => {
    setErro(null);
    try {
      setClientes(await api.getClientes());
    } catch (e) {
      setErro(e.message);
    } finally {
      setCarregando(false);
    }
  }, []);

  useFocusEffect(useCallback(() => { carregar(); }, [carregar]));

  async function salvar() {
    if (!form.cpf || !form.nome || !form.telefone || !form.endereco) {
      Alert.alert('Campos obrigatórios', 'Preencha todos os campos do cliente.');
      return;
    }
    setSalvando(true);
    try {
      await api.salvarCliente({
        cpf_cliente: form.cpf,
        nome_cliente: form.nome,
        telefone_cliente: form.telefone,
        endereco_cliente: form.endereco,
      });
      setForm(FORM_VAZIO);
      carregar();
    } catch (e) {
      Alert.alert('Erro ao salvar', e.message);
    } finally {
      setSalvando(false);
    }
  }

  function confirmarRemocao(cliente) {
    Alert.alert('Remover cliente', `Remover "${cliente.nome_cliente}"?`, [
      { text: 'Cancelar', style: 'cancel' },
      { text: 'Remover', style: 'destructive', onPress: () => remover(cliente.cpf_cliente) },
    ]);
  }

  async function remover(cpf) {
    try {
      await api.removerCliente(cpf);
      carregar();
    } catch (e) {
      Alert.alert('Erro ao remover', e.message);
    }
  }

  return (
    <FlatList
      style={styles.container}
      refreshControl={<RefreshControl refreshing={carregando} onRefresh={carregar} />}
      data={clientes}
      keyExtractor={(item) => item.cpf_cliente}
      ListHeaderComponent={
        <View>
          <Titulo>👤 Clientes</Titulo>
          <MensagemErro texto={erro} />
          <Card>
            <Subtitulo>➕ Novo Cliente</Subtitulo>
            <Campo label="CPF" placeholder="000.000.000-00" value={form.cpf}
                   onChangeText={(v) => setForm({ ...form, cpf: v })} />
            <Campo label="Nome" placeholder="Nome completo" value={form.nome}
                   onChangeText={(v) => setForm({ ...form, nome: v })} />
            <Campo label="Telefone" placeholder="(00) 00000-0000" value={form.telefone}
                   onChangeText={(v) => setForm({ ...form, telefone: v })} />
            <Campo label="Endereço" placeholder="Rua, número, bairro..." value={form.endereco}
                   onChangeText={(v) => setForm({ ...form, endereco: v })} />
            <Botao title={salvando ? 'Salvando...' : '✅ Cadastrar'} onPress={salvar} disabled={salvando} />
          </Card>
          <Subtitulo>📋 Clientes cadastrados</Subtitulo>
          {carregando && clientes.length === 0 ? <Carregando /> : null}
        </View>
      }
      ListEmptyComponent={!carregando ? <Vazio texto="Nenhum cliente cadastrado." /> : null}
      renderItem={({ item }) => (
        <Card>
          <Text style={styles.nome}>{item.nome_cliente}</Text>
          <Text style={styles.info}>CPF: {item.cpf_cliente}</Text>
          <Text style={styles.info}>Tel: {item.telefone_cliente}</Text>
          <Text style={styles.info}>{item.endereco_cliente}</Text>
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
