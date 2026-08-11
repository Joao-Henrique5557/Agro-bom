package br.com.agrobombackend.api;

import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import br.com.agrobombackend.dao.FornecedorDAO;
import br.com.agrobombackend.model.Fornecedor;

/**
 * API REST (JSON) de Fornecedores.
 *   GET    /api/fornecedores        -> lista todos
 *   GET    /api/fornecedores?id=1   -> busca um fornecedor
 *   POST   /api/fornecedores        -> cria (sem id) ou atualiza (com id)
 *   DELETE /api/fornecedores?id=1   -> remove
 */
@WebServlet("/api/fornecedores")
public class FornecedorApiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        FornecedorDAO dao = new FornecedorDAO();
        String id = request.getParameter("id");

        if (id != null && !id.isEmpty()) {
            Fornecedor fornecedor = dao.buscarPorId(Integer.parseInt(id));
            if (fornecedor == null) {
                ApiUtils.writeError(response, 404, "Fornecedor nao encontrado");
            } else {
                ApiUtils.writeJson(response, fornecedor);
            }
            return;
        }

        ApiUtils.writeJson(response, dao.listarTodos());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        FornecedorDAO dao = new FornecedorDAO();

        Fornecedor fornecedor = ApiUtils.readJson(request, Fornecedor.class);
        if (fornecedor == null || fornecedor.getNome() == null || fornecedor.getNome().isBlank()) {
            ApiUtils.writeError(response, 400, "Nome do fornecedor e obrigatorio");
            return;
        }

        if (fornecedor.getId_fornecedor() > 0) {
            dao.atualizar(fornecedor);
        } else {
            dao.salvar(fornecedor);
        }

        ApiUtils.writeJson(response, fornecedor);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            ApiUtils.writeError(response, 400, "Parametro id e obrigatorio");
            return;
        }
        new FornecedorDAO().remover(Integer.parseInt(id));
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
