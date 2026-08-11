import React, { useCallback, useState } from 'react';
import { View, Text, FlatList, StyleSheet, RefreshControl, Alert } from 'react-native';
import { useFocusEffect } from '@react-navigation/native';
import { api } from '../api/api';
import { colors } from '../theme/colors';
import { Card, Titulo, Subtitulo, Campo, Botao, Badge, Carregando, MensagemErro, Vazio } from '../components/UI';

const UNIDADES = ['kg', 'g', 'L', 'mL', 'un', 'cx', 'sc', 't', 'm', 'm²'];

const FORM_VAZIO = {
  nome: '', descricao: '', preco: '', unidade_medida: 'kg',
  quantidade_estoque: '', quantidade_ideal: '',
};

export default function EstoqueScreen() {
  const [produtos, setProdutos] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState(null);
  const [form, setForm] = useState(FORM_VAZIO);
  const [salvando, setSalvando] = useState(false);

  const carregar = useCallback(async () => {
    setErro(null);
    try {
      setProdutos(await api.getProdutos());
    } catch (e) {
      setErro(e.message);
    } finally {
      setCarregando(false);
    }
  }, []);

  useFocusEffect(useCallback(() => { carregar(); }, [carregar]));

  async function salvar() {
    if (!form.nome || !form.preco || !form.quantidade_estoque || !form.quantidade_ideal) {
      Alert.alert('Campos obrigatórios', 'Preencha nome, preço, estoque e quantidade ideal.');
      return;
    }
    setSalvando(true);
    try {
      await api.salvarProduto({
        nome: form.nome,
        descricao: form.descricao,
        preco: parseFloat(form.preco.replace(',', '.')),
        unidade_medida: form.unidade_medida,
        quantidade_estoque: parseInt(form.quantidade_estoque, 10),
        quantidade_ideal: parseInt(form.quantidade_ideal, 10),
      });
      setForm(FORM_VAZIO);
      carregar();
    } catch (e) {
      Alert.alert('Erro ao salvar', e.message);
    } finally {
      setSalvando(false);
    }
  }

  function confirmarRemocao(produto) {
    Alert.alert('Remover produto', `Remover "${produto.nome}" do estoque?`, [
      { text: 'Cancelar', style: 'cancel' },
      { text: 'Remover', style: 'destructive', onPress: () => remover(produto.id_produto) },
    ]);
  }

  async function remover(id) {
    try {
      await api.removerProduto(id);
      carregar();
    } catch (e) {
      Alert.alert('Erro ao remover', e.message);
    }
  }

  return (
    <FlatList
      style={styles.container}
      refreshControl={<RefreshControl refreshing={carregando} onRefresh={carregar} />}
      data={produtos}
      keyExtractor={(item) => String(item.id_produto)}
      ListHeaderComponent={
        <View>
          <Titulo>📦 Estoque</Titulo>
          <MensagemErro texto={erro} />
          <Card>
            <Subtitulo>➕ Novo Produto</Subtitulo>
            <Campo label="Nome" placeholder="Ex: Adubo NPK" value={form.nome}
                   onChangeText={(v) => setForm({ ...form, nome: v })} />
            <Campo label="Descrição" placeholder="Descrição do produto" value={form.descricao}
                   onChangeText={(v) => setForm({ ...form, descricao: v })} />
            <Campo label="Preço (R$)" placeholder="0,00" keyboardType="decimal-pad" value={form.preco}
                   onChangeText={(v) => setForm({ ...form, preco: v })} />
            <Campo label="Unidade de medida" placeholder={UNIDADES.join(' / ')} value={form.unidade_medida}
                   onChangeText={(v) => setForm({ ...form, unidade_medida: v })} />
            <Campo label="Qtd em estoque" placeholder="0" keyboardType="number-pad" value={form.quantidade_estoque}
                   onChangeText={(v) => setForm({ ...form, quantidade_estoque: v })} />
            <Campo label="Qtd mínima ideal" placeholder="0" keyboardType="number-pad" value={form.quantidade_ideal}
                   onChangeText={(v) => setForm({ ...form, quantidade_ideal: v })} />
            <Botao title={salvando ? 'Salvando...' : '✅ Cadastrar'} onPress={salvar} disabled={salvando} />
          </Card>
          <Subtitulo>📋 Produtos cadastrados</Subtitulo>
          {carregando && produtos.length === 0 ? <Carregando /> : null}
        </View>
      }
      ListEmptyComponent={!carregando ? <Vazio texto="Nenhum produto cadastrado." /> : null}
      renderItem={({ item }) => {
        const critico = item.quantidade_estoque <= item.quantidade_ideal;
        return (
          <Card>
            <View style={styles.linhaTopo}>
              <Text style={styles.nome}>{item.nome}</Text>
              <Badge ok={!critico} />
            </View>
            {item.descricao ? <Text style={styles.descricao}>{item.descricao}</Text> : null}
            <View style={styles.linhaInfo}>
              <Text style={styles.info}>Estoque: <Text style={styles.infoValor}>{item.quantidade_estoque} {item.unidade_medida}</Text></Text>
              <Text style={styles.info}>Ideal: <Text style={styles.infoValor}>{item.quantidade_ideal} {item.unidade_medida}</Text></Text>
            </View>
            <Text style={styles.preco}>R$ {Number(item.preco).toFixed(2)}</Text>
            <Botao title="🗑️ Remover" variant="danger" onPress={() => confirmarRemocao(item)} style={{ marginTop: 8 }} />
          </Card>
        );
      }}
      contentContainerStyle={{ padding: 16 }}
    />
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: colors.background },
  linhaTopo: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' },
  nome: { fontSize: 16, fontWeight: '700', color: colors.text, flexShrink: 1, marginRight: 8 },
  descricao: { color: colors.textMuted, marginTop: 4 },
  linhaInfo: { flexDirection: 'row', justifyContent: 'space-between', marginTop: 8 },
  info: { color: colors.textMuted, fontSize: 13 },
  infoValor: { color: colors.text, fontWeight: '700' },
  preco: { marginTop: 6, fontWeight: '700', color: colors.primaryDark, fontSize: 15 },
});
