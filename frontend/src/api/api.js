import Constants from 'expo-constants';

// Base da API. Pode ser sobrescrita em app.json (expo.extra.apiBaseUrl) sem
// precisar mexer no código-fonte:
//   - Emulador Android : http://10.0.2.2:8080/api
//   - Simulador iOS    : http://localhost:8080/api
//   - Dispositivo físico: http://<IP-da-sua-máquina-na-rede>:8080/api
const API_BASE_URL =
  Constants.expoConfig?.extra?.apiBaseUrl || 'http://10.0.2.2:8080/api';

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  });

  if (!response.ok) {
    let mensagem = `Erro ${response.status}`;
    try {
      const body = await response.json();
      if (body?.erro) mensagem = body.erro;
    } catch (e) {
      // resposta sem corpo JSON, mantém a mensagem padrão
    }
    throw new Error(mensagem);
  }

  if (response.status === 204) return null;
  return response.json();
}

export const api = {
  baseUrl: API_BASE_URL,

  // Dashboard
  getDashboard: () => request('/dashboard'),

  // Clientes
  getClientes: () => request('/clientes'),
  salvarCliente: (cliente) =>
    request('/clientes', { method: 'POST', body: JSON.stringify(cliente) }),
  removerCliente: (cpf) =>
    request(`/clientes?cpf=${encodeURIComponent(cpf)}`, { method: 'DELETE' }),

  // Produtos / Estoque
  getProdutos: () => request('/produtos'),
  salvarProduto: (produto) =>
    request('/produtos', { method: 'POST', body: JSON.stringify(produto) }),
  removerProduto: (id) => request(`/produtos?id=${id}`, { method: 'DELETE' }),

  // Fornecedores
  getFornecedores: () => request('/fornecedores'),
  salvarFornecedor: (fornecedor) =>
    request('/fornecedores', { method: 'POST', body: JSON.stringify(fornecedor) }),
  removerFornecedor: (id) => request(`/fornecedores?id=${id}`, { method: 'DELETE' }),

  // Pedidos
  getPedidos: () => request('/pedidos'),
  salvarPedido: (pedido) =>
    request('/pedidos', { method: 'POST', body: JSON.stringify(pedido) }),
  removerPedido: (id) => request(`/pedidos?id=${id}`, { method: 'DELETE' }),

  // Solicitações de compra
  getSolicitacoes: () => request('/solicitacoes'),
  salvarSolicitacao: (solicitacao) =>
    request('/solicitacoes', { method: 'POST', body: JSON.stringify(solicitacao) }),
  removerSolicitacao: (id) => request(`/solicitacoes?id=${id}`, { method: 'DELETE' }),

  // Relatórios (rel: 1 a 6)
  getRelatorio: (rel, params = {}) => {
    const query = new URLSearchParams({ rel, ...params }).toString();
    return request(`/relatorios?${query}`);
  },
};

export default api;
