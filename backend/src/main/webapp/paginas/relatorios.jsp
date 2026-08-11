<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Relatórios - AgroBom</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/paginas/menu.jsp"/>
    <div class="container">
        <div class="page-title">📊 Relatórios Gerenciais</div>

        <!-- ============================================================
             RELATÓRIO 1 — Posição de Estoque
        ============================================================ -->
        <div class="card">
            <h2>📦 Rel. 1 — Posição de Estoque</h2>
            <form action="${pageContext.request.contextPath}/relatorios" method="get" class="relatorio-filtro">
                <input type="hidden" name="rel" value="1">
                <button type="submit" class="btn btn-info">🔍 Gerar Relatório</button>
            </form>
            <%
                List<Map<String,Object>> r1 = (List<Map<String,Object>>) request.getAttribute("rel1");
                if(r1 != null) {
                    if(r1.isEmpty()) { %><p class="empty-msg">Sem dados.</p><% } else { %>
                <div class="table-wrap"><table>
                    <thead><tr><th>Nome</th><th>Descrição</th><th>Estoque</th><th>Unidade</th><th>Qtd Ideal</th><th>Status</th></tr></thead>
                    <tbody>
                    <% for(Map<String,Object> row : r1) {
                           int est  = (int) row.get("quantidade_estoque");
                           int ideal = (int) row.get("quantidade_ideal"); %>
                        <tr>
                            <td><%= row.get("nome") %></td>
                            <td><%= row.get("descricao") %></td>
                            <td><strong><%= est %></strong></td>
                            <td><%= row.get("unidade_medida") %></td>
                            <td><%= ideal %></td>
                            <td><% if(est <= ideal) { %><span class="badge badge-aberta">⚠️ Crítico</span>
                                <% } else { %><span class="badge badge-encerrada">✔ Normal</span><% } %></td>
                        </tr>
                    <% } %>
                    </tbody>
                </table></div>
            <% } } %>
        </div>

        <!-- ============================================================
             RELATÓRIO 2 — Pedidos por Mês
        ============================================================ -->
        <div class="card">
            <h2>🛒 Rel. 2 — Pedidos por Mês</h2>
            <form action="${pageContext.request.contextPath}/relatorios" method="get" class="relatorio-filtro">
                <input type="hidden" name="rel" value="2">
                <div class="form-group">
                    <label>Mês</label>
                    <select name="mes">
                        <% String[] meses = {"Janeiro","Fevereiro","Março","Abril","Maio","Junho",
                                             "Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};
                           String mesSel = request.getParameter("mes");
                           for(int i=1;i<=12;i++) { %>
                        <option value="<%= i %>" <%= String.valueOf(i).equals(mesSel) ? "selected":"" %>><%= meses[i-1] %></option>
                        <% } %>
                    </select>
                </div>
                <div class="form-group">
                    <label>Ano</label>
                    <input type="number" name="ano" min="2020" max="2099"
                           value="<%= request.getParameter("ano") != null ? request.getParameter("ano") : "2026" %>" style="width:90px">
                </div>
                <button type="submit" class="btn btn-info">🔍 Gerar</button>
            </form>
            <%
                List<Map<String,Object>> r2 = (List<Map<String,Object>>) request.getAttribute("rel2");
                if(r2 != null) {
                    if(r2.isEmpty()) { %><p class="empty-msg">Sem pedidos no período.</p><% } else { %>
                <div class="table-wrap"><table>
                    <thead><tr><th>Nº Pedido</th><th>Data</th><th>Cliente</th><th>Telefone</th>
                               <th>Endereço</th><th>Produto</th><th>Unidade</th><th>Qtd Pedida</th></tr></thead>
                    <tbody>
                    <% for(Map<String,Object> row : r2) { %>
                        <tr>
                            <td><%= row.get("numero_pedido") %></td>
                            <td><%= row.get("data_pedido") %></td>
                            <td><%= row.get("nome_cliente") %></td>
                            <td><%= row.get("telefone") %></td>
                            <td><%= row.get("endereco") %></td>
                            <td><%= row.get("produto_descricao") %></td>
                            <td><%= row.get("unidade_medida") %></td>
                            <td><%= row.get("quantidade_pedida") %></td>
                        </tr>
                    <% } %>
                    </tbody>
                </table></div>
            <% } } %>
        </div>

        <!-- ============================================================
             RELATÓRIO 3 — Pedidos por Intervalo de Datas
        ============================================================ -->
        <div class="card">
            <h2>📅 Rel. 3 — Pedidos por Intervalo de Datas</h2>
            <form action="${pageContext.request.contextPath}/relatorios" method="get" class="relatorio-filtro">
                <input type="hidden" name="rel" value="3">
                <div class="form-group">
                    <label>Data Início</label>
                    <input type="date" name="data_inicio"
                           value="<%= request.getParameter("data_inicio") != null ? request.getParameter("data_inicio") : "" %>">
                </div>
                <div class="form-group">
                    <label>Data Fim</label>
                    <input type="date" name="data_fim"
                           value="<%= request.getParameter("data_fim") != null ? request.getParameter("data_fim") : "" %>">
                </div>
                <button type="submit" class="btn btn-info">🔍 Gerar</button>
            </form>
            <%
                List<Map<String,Object>> r3 = (List<Map<String,Object>>) request.getAttribute("rel3");
                if(r3 != null) {
                    if(r3.isEmpty()) { %><p class="empty-msg">Sem pedidos no intervalo.</p><% } else { %>
                <div class="table-wrap"><table>
                    <thead><tr><th>Nº Pedido</th><th>Data</th><th>Desconto (R$)</th><th>Valor Total (R$)</th></tr></thead>
                    <tbody>
                    <% double totalGeral = 0;
                       for(Map<String,Object> row : r3) {
                           double vt = (double) row.get("valor_total");
                           totalGeral += vt; %>
                        <tr>
                            <td><%= row.get("numero_pedido") %></td>
                            <td><%= row.get("data_pedido") %></td>
                            <td>R$ <%= String.format("%.2f", (double)row.get("desconto")) %></td>
                            <td>R$ <%= String.format("%.2f", vt) %></td>
                        </tr>
                    <% } %>
                    <tr style="background:#e8f5e9;font-weight:700;">
                        <td colspan="3" class="text-right">Total do Período:</td>
                        <td>R$ <%= String.format("%.2f", totalGeral) %></td>
                    </tr>
                    </tbody>
                </table></div>
            <% } } %>
        </div>

        <!-- ============================================================
             RELATÓRIO 4 — Fornecedores por Produto
        ============================================================ -->
        <div class="card">
            <h2>🏭 Rel. 4 — Fornecedores por Produto</h2>
            <form action="${pageContext.request.contextPath}/relatorios" method="get" class="relatorio-filtro">
                <input type="hidden" name="rel" value="4">
                <button type="submit" class="btn btn-info">🔍 Gerar Relatório</button>
            </form>
            <%
                List<Map<String,Object>> r4 = (List<Map<String,Object>>) request.getAttribute("rel4");
                if(r4 != null) {
                    if(r4.isEmpty()) { %><p class="empty-msg">Sem dados.</p><% } else { %>
                <div class="table-wrap"><table>
                    <thead><tr><th>Produto</th><th>Fornecedor</th><th>CNPJ</th><th>Telefone</th></tr></thead>
                    <tbody>
                    <% for(Map<String,Object> row : r4) { %>
                        <tr>
                            <td><%= row.get("produto") %></td>
                            <td><%= row.get("fornecedor") %></td>
                            <td><%= row.get("cnpj") %></td>
                            <td><%= row.get("telefone") %></td>
                        </tr>
                    <% } %>
                    </tbody>
                </table></div>
            <% } } %>
        </div>

        <!-- ============================================================
             RELATÓRIO 5 — Solicitações por Mês
        ============================================================ -->
        <div class="card">
            <h2>📋 Rel. 5 — Solicitações de Compra por Mês</h2>
            <form action="${pageContext.request.contextPath}/relatorios" method="get" class="relatorio-filtro">
                <input type="hidden" name="rel" value="5">
                <div class="form-group">
                    <label>Mês</label>
                    <select name="mes5">
                        <% String[] meses5 = {"Janeiro","Fevereiro","Março","Abril","Maio","Junho",
                                              "Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};
                           String mes5Sel = request.getParameter("mes5");
                           for(int i=1;i<=12;i++) { %>
                        <option value="<%= i %>" <%= String.valueOf(i).equals(mes5Sel) ? "selected":"" %>><%= meses5[i-1] %></option>
                        <% } %>
                    </select>
                </div>
                <div class="form-group">
                    <label>Ano</label>
                    <input type="number" name="ano5" min="2020" max="2099"
                           value="<%= request.getParameter("ano5") != null ? request.getParameter("ano5") : "2026" %>" style="width:90px">
                </div>
                <button type="submit" class="btn btn-info">🔍 Gerar</button>
            </form>
            <%
                List<Map<String,Object>> r5 = (List<Map<String,Object>>) request.getAttribute("rel5");
                if(r5 != null) {
                    if(r5.isEmpty()) { %><p class="empty-msg">Sem solicitações no período.</p><% } else { %>
                <div class="table-wrap"><table>
                    <thead><tr><th>Nº Sol.</th><th>Data</th><th>Situação</th><th>Fornecedor</th>
                               <th>CNPJ</th><th>Produto</th><th>Qtd Solicitada</th></tr></thead>
                    <tbody>
                    <% for(Map<String,Object> row : r5) { %>
                        <tr>
                            <td><%= row.get("numero_solicitacao") %></td>
                            <td><%= row.get("data_solicitacao") %></td>
                            <td>
                                <% if("ABERTA".equals(row.get("situacao"))) { %>
                                    <span class="badge badge-aberta">🔓 Aberta</span>
                                <% } else { %>
                                    <span class="badge badge-encerrada">✅ Encerrada</span>
                                <% } %>
                            </td>
                            <td><%= row.get("fornecedor") %></td>
                            <td><%= row.get("cnpj") %></td>
                            <td><%= row.get("produto") %></td>
                            <td><%= row.get("quantidade_solicitada") %></td>
                        </tr>
                    <% } %>
                    </tbody>
                </table></div>
            <% } } %>
        </div>

        <!-- ============================================================
             RELATÓRIO 6 — Volume Financeiro 12 Meses
        ============================================================ -->
        <div class="card">
            <h2>💰 Rel. 6 — Volume Financeiro (últimos 12 meses)</h2>
            <form action="${pageContext.request.contextPath}/relatorios" method="get" class="relatorio-filtro">
                <input type="hidden" name="rel" value="6">
                <button type="submit" class="btn btn-info">🔍 Gerar Relatório</button>
            </form>
            <%
                List<Map<String,Object>> r6 = (List<Map<String,Object>>) request.getAttribute("rel6");
                if(r6 != null) {
                    if(r6.isEmpty()) { %><p class="empty-msg">Sem dados financeiros.</p><% } else {
                        String[] nomMeses = {"","Jan","Fev","Mar","Abr","Mai","Jun","Jul","Ago","Set","Out","Nov","Dez"};
                        double totalPed = 0, totalSol = 0; %>
                <div class="table-wrap"><table>
                    <thead><tr><th>Mês/Ano</th><th>Volume de Pedidos (R$)</th><th>Volume de Solicitações (R$)</th><th>Total (R$)</th></tr></thead>
                    <tbody>
                    <% for(Map<String,Object> row : r6) {
                           int m = (int) row.get("mes");
                           int a = (int) row.get("ano");
                           double tp = (double) row.get("total_pedidos");
                           double ts = (double) row.get("total_solicitacoes");
                           totalPed += tp; totalSol += ts; %>
                        <tr>
                            <td><strong><%= nomMeses[m] %>/<%= a %></strong></td>
                            <td>R$ <%= String.format("%.2f", tp) %></td>
                            <td>R$ <%= String.format("%.2f", ts) %></td>
                            <td><strong>R$ <%= String.format("%.2f", tp+ts) %></strong></td>
                        </tr>
                    <% } %>
                    <tr style="background:#e8f5e9;font-weight:700;">
                        <td>TOTAL</td>
                        <td>R$ <%= String.format("%.2f", totalPed) %></td>
                        <td>R$ <%= String.format("%.2f", totalSol) %></td>
                        <td>R$ <%= String.format("%.2f", totalPed+totalSol) %></td>
                    </tr>
                    </tbody>
                </table></div>
            <% } } %>
        </div>

    </div>
    <footer>AgroBom &copy; 2026</footer>
</body>
</html>
