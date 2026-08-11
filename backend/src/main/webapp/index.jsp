<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.agrobombackend.dao.*"%>
<%@ page import="java.util.List"%>
<%
    int totalClientes     = new br.com.agrobombackend.dao.ClienteDAO().listarTodos().size();
    int totalFornecedores = new br.com.agrobombackend.dao.FornecedorDAO().listarTodos().size();
    int totalProdutos     = new br.com.agrobombackend.dao.ProdutoDAO().listarTodos().size();
    int totalPedidos      = new br.com.agrobombackend.dao.PedidoDAO().listarTodos().size();
    int totalSolicitacoes = new br.com.agrobombackend.dao.SolicitacaoCompraDAO().listarTodos().size();
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>AgroBom - Início</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/paginas/menu.jsp"/>

    <div class="container">
        <div class="page-title">🌿 Painel de Controle</div>

        <div class="dashboard-grid">
            <a class="dash-card" href="${pageContext.request.contextPath}/clientes">
                <span class="icon">👤</span>
                <span class="label">Clientes</span>
                <span class="value"><%= totalClientes %></span>
            </a>
            <a class="dash-card" href="${pageContext.request.contextPath}/fornecedores">
                <span class="icon">🏭</span>
                <span class="label">Fornecedores</span>
                <span class="value"><%= totalFornecedores %></span>
            </a>
            <a class="dash-card" href="${pageContext.request.contextPath}/produtos">
                <span class="icon">📦</span>
                <span class="label">Produtos</span>
                <span class="value"><%= totalProdutos %></span>
            </a>
            <a class="dash-card" href="${pageContext.request.contextPath}/pedidos">
                <span class="icon">🛒</span>
                <span class="label">Pedidos</span>
                <span class="value"><%= totalPedidos %></span>
            </a>
            <a class="dash-card" href="${pageContext.request.contextPath}/solicitacoes">
                <span class="icon">📋</span>
                <span class="label">Solicitações</span>
                <span class="value"><%= totalSolicitacoes %></span>
            </a>
            <a class="dash-card" href="${pageContext.request.contextPath}/relatorios">
                <span class="icon">📊</span>
                <span class="label">Relatórios</span>
                <span class="value">6</span>
            </a>
        </div>

        <div class="card">
            <h2>Acesso Rápido</h2>
            <div style="display:flex;gap:12px;flex-wrap:wrap;">
                <a href="${pageContext.request.contextPath}/clientes" class="btn btn-primary">+ Novo Cliente</a>
                <a href="${pageContext.request.contextPath}/fornecedores" class="btn btn-primary">+ Novo Fornecedor</a>
                <a href="${pageContext.request.contextPath}/produtos" class="btn btn-primary">+ Novo Produto</a>
                <a href="${pageContext.request.contextPath}/pedidos" class="btn btn-primary">+ Novo Pedido</a>
                <a href="${pageContext.request.contextPath}/solicitacoes" class="btn btn-primary">+ Nova Solicitação</a>
            </div>
        </div>
    </div>

    <footer>AgroBom &copy; 2026 — Sistema de Gestão Agrícola</footer>
</body>
</html>
