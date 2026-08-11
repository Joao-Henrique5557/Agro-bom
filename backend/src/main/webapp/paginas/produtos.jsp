<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="br.com.agrobombackend.model.Produto"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Produtos - AgroBom</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/paginas/menu.jsp"/>
    <div class="container">
        <div class="page-title">📦 Produtos</div>

        <% if(request.getAttribute("msg") != null) { %>
            <div class="alert alert-success"><%= request.getAttribute("msg") %></div>
        <% } %>

        <div class="card">
            <%
                Produto pEdit = (Produto) request.getAttribute("produto");
                boolean editando = pEdit != null;
            %>
            <h2><%= editando ? "✏️ Editar Produto" : "➕ Novo Produto" %></h2>
            <form action="${pageContext.request.contextPath}/produtos" method="post">
                <input type="hidden" name="acao" value="salvar">
                <% if(editando) { %>
                    <input type="hidden" name="id" value="<%= pEdit.getId_produto() %>">
                <% } %>
                <div class="form-grid">
                    <div class="form-group">
                        <label>Nome</label>
                        <input type="text" name="nome" placeholder="Ex: Adubo NPK"
                               value="<%= editando ? pEdit.getNome() : "" %>" required>
                    </div>
                    <div class="form-group">
                        <label>Descrição</label>
                        <input type="text" name="descricao" placeholder="Descrição do produto"
                               value="<%= editando ? pEdit.getDescricao() : "" %>">
                    </div>
                    <div class="form-group">
                        <label>Preço (R$)</label>
                        <input type="number" name="preco" step="0.01" min="0" placeholder="0,00"
                               value="<%= editando ? pEdit.getPreco() : "" %>" required>
                    </div>
                    <div class="form-group">
                        <label>Unidade de Medida</label>
                        <select name="unidade_medida" required>
                            <option value="">Selecione...</option>
                            <% String[] unidades = {"kg","g","L","mL","un","cx","sc","t","m","m²"};
                               for(String u : unidades) {
                                   boolean sel = editando && u.equals(pEdit.getUnidade_medida()); %>
                            <option value="<%= u %>" <%= sel ? "selected" : "" %>><%= u %></option>
                            <% } %>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Qtd em Estoque</label>
                        <input type="number" name="quantidade_estoque" min="0" placeholder="0"
                               value="<%= editando ? pEdit.getQuantidade_estoque() : "0" %>" required>
                    </div>
                    <div class="form-group">
                        <label>Qtd Mínima Ideal</label>
                        <input type="number" name="quantidade_ideal" min="0" placeholder="0"
                               value="<%= editando ? pEdit.getQuantidade_ideal() : "0" %>" required>
                    </div>
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <%= editando ? "💾 Salvar Alterações" : "✅ Cadastrar" %>
                    </button>
                    <% if(editando) { %>
                        <a href="${pageContext.request.contextPath}/produtos" class="btn btn-secondary">✖ Cancelar</a>
                    <% } %>
                </div>
            </form>
        </div>

        <div class="card">
            <h2>📋 Produtos em Estoque</h2>
            <div class="table-wrap">
                <table>
                    <thead>
                        <tr><th>#</th><th>Nome</th><th>Descrição</th><th>Preço</th>
                            <th>Unidade</th><th>Estoque</th><th>Qtd Ideal</th><th>Status</th><th>Ações</th></tr>
                    </thead>
                    <tbody>
                    <%
                        List<Produto> produtos = (List<Produto>) request.getAttribute("produtos");
                        if(produtos == null || produtos.isEmpty()) {
                    %>
                        <tr><td colspan="9" class="empty-msg">Nenhum produto cadastrado.</td></tr>
                    <%  } else {
                            for(Produto p : produtos) {
                                boolean critico = p.getQuantidade_estoque() <= p.getQuantidade_ideal(); %>
                        <tr>
                            <td><%= p.getId_produto() %></td>
                            <td><%= p.getNome() %></td>
                            <td><%= p.getDescricao() %></td>
                            <td>R$ <%= String.format("%.2f", p.getPreco()) %></td>
                            <td><%= p.getUnidade_medida() %></td>
                            <td><strong><%= p.getQuantidade_estoque() %></strong></td>
                            <td><%= p.getQuantidade_ideal() %></td>
                            <td>
                                <% if(critico) { %>
                                    <span class="badge badge-aberta">⚠️ Crítico</span>
                                <% } else { %>
                                    <span class="badge badge-encerrada">✔ Normal</span>
                                <% } %>
                            </td>
                            <td class="td-actions">
                                <a href="${pageContext.request.contextPath}/produtos?acao=editar&id=<%= p.getId_produto() %>"
                                   class="btn btn-warning btn-sm">✏️ Editar</a>
                                <form action="${pageContext.request.contextPath}/produtos" method="post" style="display:inline"
                                      onsubmit="return confirm('Remover este produto?')">
                                    <input type="hidden" name="acao" value="remover">
                                    <input type="hidden" name="id" value="<%= p.getId_produto() %>">
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
