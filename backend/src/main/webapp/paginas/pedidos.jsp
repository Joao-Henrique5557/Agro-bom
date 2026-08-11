<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="br.com.agrobombackend.model.*"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Pedidos - AgroBom</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/paginas/menu.jsp"/>
    <div class="container">
        <div class="page-title">🛒 Pedidos</div>

        <% if(request.getAttribute("msg") != null) { %>
            <div class="alert alert-success"><%= request.getAttribute("msg") %></div>
        <% } %>
        <% if(request.getAttribute("erro") != null) { %>
            <div class="alert alert-danger"><%= request.getAttribute("erro") %></div>
        <% } %>

        <!-- FORMULÁRIO -->
        <div class="card">
            <%
                Pedido pEdit = (Pedido) request.getAttribute("pedido");
                boolean editando = pEdit != null;
                List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
            %>
            <h2><%= editando ? "✏️ Editar Pedido" : "➕ Novo Pedido" %></h2>
            <form action="${pageContext.request.contextPath}/pedidos" method="post">
                <input type="hidden" name="acao" value="salvar">
                <% if(editando) { %>
                    <input type="hidden" name="id" value="<%= pEdit.getId_pedido() %>">
                <% } %>
                <div class="form-grid">
                    <div class="form-group">
                        <label>Número do Pedido</label>
                        <input type="number" name="numero_pedido" min="1"
                               value="<%= editando ? pEdit.getNumero_pedido() : "" %>" required>
                    </div>
                    <div class="form-group">
                        <label>Data</label>
                        <input type="date" name="data_pedido"
                               value="<%= editando ? pEdit.getData_pedido().toString() : "" %>" required>
                    </div>
                    <div class="form-group">
                        <label>Cliente</label>
                        <select name="cpf_cliente" required>
                            <option value="">Selecione o cliente...</option>
                            <% if(clientes != null) {
                                for(Cliente c : clientes) {
                                    boolean sel = editando && c.getCpf_cliente().equals(pEdit.getCpf_cliente()); %>
                            <option value="<%= c.getCpf_cliente() %>" <%= sel ? "selected" : "" %>>
                                <%= c.getNome_cliente() %> — <%= c.getCpf_cliente() %>
                            </option>
                            <%  }
                               } %>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Desconto (R$)</label>
                        <input type="number" name="desconto" step="0.01" min="0" placeholder="0,00"
                               value="<%= editando ? pEdit.getDesconto() : "0" %>">
                    </div>
                    <div class="form-group">
                        <label>Valor Total (R$)</label>
                        <input type="number" name="valor_total" step="0.01" min="0" placeholder="0,00"
                               value="<%= editando ? pEdit.getValor_total() : "" %>" required>
                    </div>
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <%= editando ? "💾 Salvar Alterações" : "✅ Registrar Pedido" %>
                    </button>
                    <% if(editando) { %>
                        <a href="${pageContext.request.contextPath}/pedidos" class="btn btn-secondary">✖ Cancelar</a>
                    <% } %>
                </div>
            </form>
        </div>

        <!-- TABELA -->
        <div class="card">
            <h2>📋 Pedidos Registrados</h2>
            <div class="table-wrap">
                <table>
                    <thead>
                        <tr><th>#</th><th>Nº Pedido</th><th>Data</th><th>Cliente</th>
                            <th>Desconto</th><th>Valor Total</th><th>Ações</th></tr>
                    </thead>
                    <tbody>
                    <%
                        List<Pedido> pedidos = (List<Pedido>) request.getAttribute("pedidos");
                        if(pedidos == null || pedidos.isEmpty()) {
                    %>
                        <tr><td colspan="7" class="empty-msg">Nenhum pedido registrado.</td></tr>
                    <%  } else {
                            for(Pedido p : pedidos) { %>
                        <tr>
                            <td><%= p.getId_pedido() %></td>
                            <td><strong><%= p.getNumero_pedido() %></strong></td>
                            <td><%= p.getData_pedido() %></td>
                            <td><%= p.getCpf_cliente() %></td>
                            <td>R$ <%= String.format("%.2f", p.getDesconto()) %></td>
                            <td><strong>R$ <%= String.format("%.2f", p.getValor_total()) %></strong></td>
                            <td class="td-actions">
                                <a href="${pageContext.request.contextPath}/pedidos?acao=editar&id=<%= p.getId_pedido() %>"
                                   class="btn btn-warning btn-sm">✏️ Editar</a>
                                <form action="${pageContext.request.contextPath}/pedidos" method="post" style="display:inline"
                                      onsubmit="return confirm('Remover este pedido?')">
                                    <input type="hidden" name="acao" value="remover">
                                    <input type="hidden" name="id" value="<%= p.getId_pedido() %>">
                                    <button type="submit" class="btn btn-danger btn-sm">🗑️ Remover</button>
                                </form>
                            </td>
                        </tr>
                    <%      }
                        } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <footer>AgroBom &copy; 2026</footer>
</body>
</html>
