package br.com.agrobombackend.api;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import br.com.agrobombackend.dao.ClienteDAO;
import br.com.agrobombackend.dao.FornecedorDAO;
import br.com.agrobombackend.dao.PedidoDAO;
import br.com.agrobombackend.dao.ProdutoDAO;
import br.com.agrobombackend.dao.SolicitacaoCompraDAO;

/**
 * Endpoint agregado usado pela tela inicial (Dashboard) do app React Native,
 * evitando 5 chamadas separadas so para exibir os contadores do painel.
 *
 *   GET /api/dashboard
 */
@WebServlet("/api/dashboard")
public class DashboardApiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Map<String, Object> resumo = new LinkedHashMap<>();
        resumo.put("clientes", new ClienteDAO().listarTodos().size());
        resumo.put("fornecedores", new FornecedorDAO().listarTodos().size());
        resumo.put("produtos", new ProdutoDAO().listarTodos().size());
        resumo.put("pedidos", new PedidoDAO().listarTodos().size());
        resumo.put("solicitacoes", new SolicitacaoCompraDAO().listarTodos().size());

        ApiUtils.writeJson(response, resumo);
    }
}
