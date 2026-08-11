<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="br.com.agrobombackend.model.Cliente"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Clientes - AgroBom</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/paginas/menu.jsp"/>
    <div class="container">
        <div class="page-title">👤 Clientes</div>

        <% if(request.getAttribute("msg") != null) { %>
            <div class="alert alert-success"><%= request.getAttribute("msg") %></div>
        <% } %>

        <!-- FORMULÁRIO CADASTRO / EDIÇÃO -->
        <div class="card">
            <%
                Cliente clienteEdit = (Cliente) request.getAttribute("cliente");
                boolean editando = clienteEdit != null;
            %>
            <h2><%= editando ? "✏️ Editar Cliente" : "➕ Novo Cliente" %></h2>
            <form action="${pageContext.request.contextPath}/clientes" method="post">
                <input type="hidden" name="acao" value="salvar">
                <div class="form-grid">
                    <div class="form-group">
                        <label>CPF</label>
                        <input type="text" name="cpf" maxlength="14" placeholder="000.000.000-00"
                               value="<%= editando ? clienteEdit.getCpf_cliente() : "" %>"
                               <%= editando ? "readonly" : "" %> required>
                    </div>
                    <div class="form-group">
                        <label>Nome</label>
                        <input type="text" name="nome" placeholder="Nome completo"
                               value="<%= editando ? clienteEdit.getNome_cliente() : "" %>" required>
                    </div>
                    <div class="form-group">
                        <label>Telefone</label>
                        <input type="text" name="telefone" placeholder="(00) 00000-0000"
                               value="<%= editando ? clienteEdit.getTelefone_cliente() : "" %>" required>
                    </div>
                    <div class="form-group">
                        <label>Endereço</label>
                        <input type="text" name="endereco" placeholder="Rua, número, bairro..."
                               value="<%= editando ? clienteEdit.getEndereco_cliente() : "" %>" required>
                    </div>
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <%= editando ? "💾 Salvar Alterações" : "✅ Cadastrar" %>
                    </button>
                    <% if(editando) { %>
                        <a href="${pageContext.request.contextPath}/clientes" class="btn btn-secondary">✖ Cancelar</a>
                    <% } %>
                </div>
            </form>
        </div>

        <!-- TABELA DE CLIENTES -->
        <div class="card">
            <h2>📋 Clientes Cadastrados</h2>
            <div class="table-wrap">
                <table>
                    <thead>
                        <tr>
                            <th>CPF</th><th>Nome</th><th>Telefone</th><th>Endereço</th><th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                    <%
                        List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
                        if (clientes == null || clientes.isEmpty()) {
                    %>
                        <tr><td colspan="5" class="empty-msg">Nenhum cliente cadastrado.</td></tr>
                    <%  } else {
                            for (Cliente c : clientes) { %>
                        <tr>
                            <td><%= c.getCpf_cliente() %></td>
                            <td><%= c.getNome_cliente() %></td>
                            <td><%= c.getTelefone_cliente() %></td>
                            <td><%= c.getEndereco_cliente() %></td>
                            <td class="td-actions">
                                <a href="${pageContext.request.contextPath}/clientes?acao=editar&cpf=<%= c.getCpf_cliente() %>"
                                   class="btn btn-warning btn-sm">✏️ Editar</a>
                                <form action="${pageContext.request.contextPath}/clientes" method="post" style="display:inline"
                                      onsubmit="return confirm('Remover este cliente?')">
                                    <input type="hidden" name="acao" value="remover">
                                    <input type="hidden" name="cpf" value="<%= c.getCpf_cliente() %>">
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
