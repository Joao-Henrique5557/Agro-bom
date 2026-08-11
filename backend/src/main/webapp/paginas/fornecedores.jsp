<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="br.com.agrobombackend.model.Fornecedor"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Fornecedores - AgroBom</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/paginas/menu.jsp"/>
    <div class="container">
        <div class="page-title">🏭 Fornecedores</div>

        <% if(request.getAttribute("msg") != null) { %>
            <div class="alert alert-success"><%= request.getAttribute("msg") %></div>
        <% } %>

        <div class="card">
            <%
                Fornecedor fEdit = (Fornecedor) request.getAttribute("fornecedor");
                boolean editando = fEdit != null;
            %>
            <h2><%= editando ? "✏️ Editar Fornecedor" : "➕ Novo Fornecedor" %></h2>
            <form action="${pageContext.request.contextPath}/fornecedores" method="post">
                <input type="hidden" name="acao" value="salvar">
                <% if(editando) { %>
                    <input type="hidden" name="id" value="<%= fEdit.getId_fornecedor() %>">
                <% } %>
                <div class="form-grid">
                    <div class="form-group">
                        <label>Nome</label>
                        <input type="text" name="nome" placeholder="Razão social"
                               value="<%= editando ? fEdit.getNome() : "" %>" required>
                    </div>
                    <div class="form-group">
                        <label>CNPJ</label>
                        <input type="text" name="cnpj" maxlength="18" placeholder="00.000.000/0000-00"
                               value="<%= editando ? fEdit.getCnpj() : "" %>" required>
                    </div>
                    <div class="form-group">
                        <label>Telefone</label>
                        <input type="text" name="telefone" placeholder="(00) 00000-0000"
                               value="<%= editando ? fEdit.getTelefone() : "" %>" required>
                    </div>
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <%= editando ? "💾 Salvar Alterações" : "✅ Cadastrar" %>
                    </button>
                    <% if(editando) { %>
                        <a href="${pageContext.request.contextPath}/fornecedores" class="btn btn-secondary">✖ Cancelar</a>
                    <% } %>
                </div>
            </form>
        </div>

        <div class="card">
            <h2>📋 Fornecedores Cadastrados</h2>
            <div class="table-wrap">
                <table>
                    <thead>
                        <tr><th>#</th><th>Nome</th><th>CNPJ</th><th>Telefone</th><th>Ações</th></tr>
                    </thead>
                    <tbody>
                    <%
                        List<Fornecedor> fornecedores = (List<Fornecedor>) request.getAttribute("fornecedores");
                        if(fornecedores == null || fornecedores.isEmpty()) {
                    %>
                        <tr><td colspan="5" class="empty-msg">Nenhum fornecedor cadastrado.</td></tr>
                    <%  } else {
                            for(Fornecedor f : fornecedores) { %>
                        <tr>
                            <td><%= f.getId_fornecedor() %></td>
                            <td><%= f.getNome() %></td>
                            <td><%= f.getCnpj() %></td>
                            <td><%= f.getTelefone() %></td>
                            <td class="td-actions">
                                <a href="${pageContext.request.contextPath}/fornecedores?acao=editar&id=<%= f.getId_fornecedor() %>"
                                   class="btn btn-warning btn-sm">✏️ Editar</a>
                                <form action="${pageContext.request.contextPath}/fornecedores" method="post" style="display:inline"
                                      onsubmit="return confirm('Remover este fornecedor?')">
                                    <input type="hidden" name="acao" value="remover">
                                    <input type="hidden" name="id" value="<%= f.getId_fornecedor() %>">
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
