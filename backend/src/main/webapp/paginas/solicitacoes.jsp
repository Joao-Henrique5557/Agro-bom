<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="br.com.agrobombackend.model.*"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Solicitações de Compra - AgroBom</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/paginas/menu.jsp"/>
    <div class="container">
        <div class="page-title">📋 Solicitações de Compra</div>

        <% if(request.getAttribute("msg") != null) { %>
            <div class="alert alert-success"><%= request.getAttribute("msg") %></div>
        <% } %>

        <!-- FORMULÁRIO -->
        <div class="card">
            <%
                SolicitacaoCompra sEdit = (SolicitacaoCompra) request.getAttribute("solicitacao");
                boolean editando = sEdit != null;
                List<Fornecedor> fornecedores = (List<Fornecedor>) request.getAttribute("fornecedores");
            %>
            <h2><%= editando ? "✏️ Editar Solicitação" : "➕ Nova Solicitação" %></h2>
            <form action="${pageContext.request.contextPath}/solicitacoes" method="post">
                <input type="hidden" name="acao" value="salvar">
                <% if(editando) { %>
                    <input type="hidden" name="id" value="<%= sEdit.getId_solicitacao() %>">
                <% } %>
                <div class="form-grid">
                    <div class="form-group">
                        <label>Número da Solicitação</label>
                        <input type="number" name="numero_solicitacao" min="1"
                               value="<%= editando ? sEdit.getNumero_solicitacao() : "" %>" required>
                    </div>
                    <div class="form-group">
                        <label>Data</label>
                        <input type="date" name="data_solicitacao"
                               value="<%= editando && sEdit.getData_solicitacao() != null
                                         ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(sEdit.getData_solicitacao())
                                         : "" %>" required>
                    </div>
                    <div class="form-group">
                        <label>Fornecedor</label>
                        <select name="id_fornecedor" required>
                            <option value="">Selecione o fornecedor...</option>
                            <% if(fornecedores != null) {
                                for(Fornecedor f : fornecedores) {
                                    boolean sel = editando && f.getId_fornecedor() == sEdit.getId_fornecedor(); %>
                            <option value="<%= f.getId_fornecedor() %>" <%= sel ? "selected" : "" %>>
                                <%= f.getNome() %> — <%= f.getCnpj() %>
                            </option>
                            <%  }
                               } %>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Situação</label>
                        <select name="situacao" required>
                            <option value="ABERTA"    <%= editando && "ABERTA".equals(sEdit.getSituacao())    ? "selected" : "" %>>Aberta</option>
                            <option value="ENCERRADA" <%= editando && "ENCERRADA".equals(sEdit.getSituacao()) ? "selected" : "" %>>Encerrada</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Valor Total (R$)</label>
                        <input type="number" name="valor_total" step="0.01" min="0" placeholder="0,00"
                               value="<%= editando ? sEdit.getValor_total() : "0" %>">
                    </div>
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <%= editando ? "💾 Salvar Alterações" : "✅ Registrar Solicitação" %>
                    </button>
                    <% if(editando) { %>
                        <a href="${pageContext.request.contextPath}/solicitacoes" class="btn btn-secondary">✖ Cancelar</a>
                    <% } %>
                </div>
            </form>
        </div>

        <!-- TABELA -->
        <div class="card">
            <h2>📋 Solicitações Registradas</h2>
            <div class="table-wrap">
                <table>
                    <thead>
                        <tr><th>#</th><th>Nº Sol.</th><th>Data</th><th>Fornecedor (ID)</th>
                            <th>Valor Total</th><th>Situação</th><th>Ações</th></tr>
                    </thead>
                    <tbody>
                    <%
                        List<SolicitacaoCompra> solicitacoes = (List<SolicitacaoCompra>) request.getAttribute("solicitacoes");
                        if(solicitacoes == null || solicitacoes.isEmpty()) {
                    %>
                        <tr><td colspan="7" class="empty-msg">Nenhuma solicitação registrada.</td></tr>
                    <%  } else {
                            for(SolicitacaoCompra s : solicitacoes) { %>
                        <tr>
                            <td><%= s.getId_solicitacao() %></td>
                            <td><strong><%= s.getNumero_solicitacao() %></strong></td>
                            <td><%= s.getData_solicitacao() != null ? new java.text.SimpleDateFormat("dd/MM/yyyy").format(s.getData_solicitacao()) : "" %></td>
                            <td><%= s.getId_fornecedor() %></td>
                            <td>R$ <%= String.format("%.2f", s.getValor_total()) %></td>
                            <td>
                                <% if("ABERTA".equals(s.getSituacao())) { %>
                                    <span class="badge badge-aberta">🔓 Aberta</span>
                                <% } else { %>
                                    <span class="badge badge-encerrada">✅ Encerrada</span>
                                <% } %>
                            </td>
                            <td class="td-actions">
                                <a href="${pageContext.request.contextPath}/solicitacoes?acao=editar&id=<%= s.getId_solicitacao() %>"
                                   class="btn btn-warning btn-sm">✏️ Editar</a>
                                <form action="${pageContext.request.contextPath}/solicitacoes" method="post" style="display:inline"
                                      onsubmit="return confirm('Remover esta solicitação?')">
                                    <input type="hidden" name="acao" value="remover">
                                    <input type="hidden" name="id" value="<%= s.getId_solicitacao() %>">
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
