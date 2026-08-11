package br.com.agrobombackend.api;

import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import br.com.agrobombackend.dao.ProdutoDAO;
import br.com.agrobombackend.model.Produto;

/**
 * API REST (JSON) de Produtos / Estoque.
 *   GET    /api/produtos          -> lista todos
 *   GET    /api/produtos?id=1     -> busca um produto
 *   POST   /api/produtos          -> cria (sem id) ou atualiza (com id)
 *   DELETE /api/produtos?id=1     -> remove
 */
@WebServlet("/api/produtos")
public class ProdutoApiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ProdutoDAO dao = new ProdutoDAO();
        String id = request.getParameter("id");

        if (id != null && !id.isEmpty()) {
            Produto produto = dao.buscarPorId(Integer.parseInt(id));
            if (produto == null) {
                ApiUtils.writeError(response, 404, "Produto nao encontrado");
            } else {
                ApiUtils.writeJson(response, produto);
            }
            return;
        }

        ApiUtils.writeJson(response, dao.listarTodos());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        ProdutoDAO dao = new ProdutoDAO();

        Produto produto = ApiUtils.readJson(request, Produto.class);
        if (produto == null || produto.getNome() == null || produto.getNome().isBlank()) {
            ApiUtils.writeError(response, 400, "Nome do produto e obrigatorio");
            return;
        }

        if (produto.getId_produto() > 0) {
            dao.atualizar(produto);
        } else {
            dao.salvar(produto);
        }

        ApiUtils.writeJson(response, produto);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            ApiUtils.writeError(response, 400, "Parametro id e obrigatorio");
            return;
        }
        new ProdutoDAO().remover(Integer.parseInt(id));
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
