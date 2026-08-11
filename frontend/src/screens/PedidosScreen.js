import React, { useCallback, useState } from 'react';
import { View, Text, FlatList, StyleSheet, RefreshControl, Alert } from 'react-native';
import { Picker } from '@react-native-picker/picker';
import { useFocusEffect } from '@react-navigation/native';
import { api } from '../api/api';
import { colors } from '../theme/colors';
import { Card, Titulo, Subtitulo, Campo, Botao, Carregando, MensagemErro, Vazio } from '../components/UI';

const FORM_VAZIO = { numero_pedido: '', data_pedido: '', desconto: '0', valor_total: '', cpf_cliente: '' };

function hojeISO() {
  return new Date().toISOString().slice(0, 10);
}

export default function PedidosScreen() {
  const [pedidos, setPedidos] = useState([]);
  const [clientes, setClientes] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState(null);
  const [form, setForm] = useState({ ...FORM_VAZIO, data_pedido: hojeISO() });
  const [salvando, setSalvando] = useState(false);

  const carregar = useCallback(async () => {
    setErro(null);
    try {
      const [listaPedidos, listaClientes] = await Promise.all([api.getPedidos(), api.getClientes()]);
      setPedidos(listaPedidos);
      setClientes(listaClientes);
      if (!form.cpf_cliente && listaClientes.length > 0) {
        setForm((f) => ({ ...f, cpf_cliente: listaClientes[0].cpf_cliente }));
      }
    } catch (e) {
      setErro(e.message);
    } finally {
      setCarregando(false);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useFocusEffect(useCallback(() => { carregar(); }, [carregar]));

  async function salvar() {
    if (!form.numero_pedido || !form.data_pedido || !form.valor_total || !form.cpf_cliente) {
      Alert.alert('Campos obrigatórios', 'Preencha número, data, valor total e selecione um cliente.');
      return;
    }
    setSalvando(true);
    try {
      await api.salvarPedido({
        numero_pedido: parseInt(form.numero_pedido, 10),
        data_pedido: form.data_pedido,
        desconto: parseFloat((form.desconto || '0').replace(',', '.')),
        valor_total: parseFloat(form.valor_total.replace(',', '.')),
        cpf_cliente: form.cpf_cliente,
      });
      setForm({ ...FORM_VAZIO, data_pedido: hojeISO(), cpf_cliente: form.cpf_cliente });
      carregar();
    } catch (e) {
      Alert.alert('Erro ao salvar', e.message);
    } finally {
      setSalvando(false);
    }
  }

  function confirmarRemocao(pedido) {
    Alert.alert('Remover pedido', `Remover o pedido nº ${pedido.numero_pedido}?`, [
      { text: 'Cancelar', style: 'cancel' },
      { text: 'Remover', style: 'destructive', onPress: () => remover(pedido.id_pedido) },
    ]);
  }

  async function remover(id) {
    try {
      await api.removerPedido(id);
      carregar();
    } catch (e) {
      Alert.alert('Erro ao remover', e.message);
    }
  }

  function nomeCliente(cpf) {
    const c = clientes.find((cl) => cl.cpf_cliente === cpf);
    return c ? c.nome_cliente : cpf;
  }

  return (
    <FlatList
      style={styles.container}
      refreshControl={<RefreshControl refreshing={carregando} onRefresh={carregar} />}
      data={pedidos}
      keyExtractor={(item) => String(item.id_pedido)}
      ListHeaderComponent={
        <View>
          <Titulo>🛒 Pedidos</Titulo>
          <MensagemErro texto={erro} />
          <Card>
            <Subtitulo>➕ Novo Pedido</Subtitulo>
            <Campo label="Número do pedido" placeholder="Ex: 1001" keyboardType="number-pad"
                   value={form.numero_pedido} onChangeText={(v) => setForm({ ...form, numero_pedido: v })} />
            <Campo label="Data (AAAA-MM-DD)" placeholder="2026-08-11" value={form.data_pedido}
                   onChangeText={(v) => setForm({ ...form, data_pedido: v })} />

            <Text style={styles.label}>Cliente</Text>
            {clientes.length === 0 ? (
              <Text style={styles.avisoSemCliente}>Cadastre um cliente antes de registrar um pedido.</Text>
            ) : (
              <View style={styles.pickerWrap}>
                <Picker selectedValue={form.cpf_cliente}
                        onValueChange={(v) => setForm({ ...form, cpf_cliente: v })}>
                  {clientes.map((c) => (
                    <Picker.Item key={c.cpf_cliente} label={`${c.nome_cliente} — ${c.cpf_cliente}`} value={c.cpf_cliente} />
                  ))}
                </Picker>
              </View>
            )}

            <Campo label="Desconto (R$)" placeholder="0,00" keyboardType="decimal-pad" value={form.desconto}
                   onChangeText={(v) => setForm({ ...form, desconto: v })} />
            <Campo label="Valor total (R$)" placeholder="0,00" keyboardType="decimal-pad" value={form.valor_total}
                   onChangeText={(v) => setForm({ ...form, valor_total: v })} />
            <Botao title={salvando ? 'Salvando...' : '✅ Registrar Pedido'} onPress={salvar} disabled={salvando} />
          </Card>
          <Subtitulo>📋 Pedidos registrados</Subtitulo>
          {carregando && pedidos.length === 0 ? <Carregando /> : null}
        </View>
      }
      ListEmptyComponent={!carregando ? <Vazio texto="Nenhum pedido registrado." /> : null}
      renderItem={({ item }) => (
        <Card>
          <View style={styles.linhaTopo}>
            <Text style={styles.nome}>Pedido nº {item.numero_pedido}</Text>
            <Text style={styles.data}>{item.data_pedido}</Text>
          </View>
          <Text style={styles.info}>Cliente: {nomeCliente(item.cpf_cliente)}</Text>
          <Text style={styles.info}>Desconto: R$ {Number(item.desconto).toFixed(2)}</Text>
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
  linhaTopo: { flexDirection: 'row', justifyContent: 'space-between' },
  nome: { fontSize: 16, fontWeight: '700', color: colors.text },
  data: { color: colors.textMuted },
  info: { color: colors.textMuted, marginTop: 2, fontSize: 13 },
  valorTotal: { marginTop: 6, fontWeight: '700', color: colors.primaryDark, fontSize: 15 },
  label: { fontSize: 12, fontWeight: '600', color: '#555', marginBottom: 4, textTransform: 'uppercase' },
  pickerWrap: { borderWidth: 1.5, borderColor: '#ddd', borderRadius: 8, marginBottom: 12, backgroundColor: '#fafafa' },
  avisoSemCliente: { color: colors.warning, marginBottom: 12, fontSize: 13 },
});
