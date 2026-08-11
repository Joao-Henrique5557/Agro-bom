package br.com.agrobombackend.api;

import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import br.com.agrobombackend.dao.PedidoDAO;
import br.com.agrobombackend.model.Pedido;

/**
 * API REST (JSON) de Pedidos.
 *   GET    /api/pedidos                         -> lista todos
 *   GET    /api/pedidos?mes=5&ano=2026           -> lista por mes/ano
 *   GET    /api/pedidos?id=1                     -> busca um pedido
 *   POST   /api/pedidos                          -> cria (sem id) ou atualiza (com id)
 *   DELETE /api/pedidos?id=1                     -> remove
 */
@WebServlet("/api/pedidos")
public class PedidoApiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PedidoDAO dao = new PedidoDAO();

        String id = request.getParameter("id");
        if (id != null && !id.isEmpty()) {
            Pedido pedido = dao.buscarPorId(Integer.parseInt(id));
            if (pedido == null) {
                ApiUtils.writeError(response, 404, "Pedido nao encontrado");
            } else {
                ApiUtils.writeJson(response, pedido);
            }
            return;
        }

        String mes = request.getParameter("mes");
        String ano = request.getParameter("ano");
        if (mes != null && ano != null) {
            ApiUtils.writeJson(response, dao.listarPorMes(Integer.parseInt(mes), Integer.parseInt(ano)));
            return;
        }

        ApiUtils.writeJson(response, dao.listarTodos());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        PedidoDAO dao = new PedidoDAO();

        Pedido pedido = ApiUtils.readJson(request, Pedido.class);
        if (pedido == null || pedido.getCpf_cliente() == null || pedido.getData_pedido() == null) {
            ApiUtils.writeError(response, 400, "cpf_cliente e data_pedido sao obrigatorios");
            return;
        }

        if (pedido.getId_pedido() > 0) {
            dao.atualizar(pedido);
        } else {
            dao.salvar(pedido);
        }

        ApiUtils.writeJson(response, pedido);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            ApiUtils.writeError(response, 400, "Parametro id e obrigatorio");
            return;
        }
        new PedidoDAO().remover(Integer.parseInt(id));
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
