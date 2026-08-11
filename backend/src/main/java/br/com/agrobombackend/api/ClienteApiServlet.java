package br.com.agrobombackend.api;

import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import br.com.agrobombackend.dao.ClienteDAO;
import br.com.agrobombackend.model.Cliente;

/**
 * API REST (JSON) de Clientes, consumida pelo app React Native.
 *   GET    /api/clientes            -> lista todos
 *   GET    /api/clientes?cpf=...    -> busca um cliente
 *   POST   /api/clientes            -> cria ou atualiza (upsert por CPF)
 *   DELETE /api/clientes?cpf=...    -> remove
 */
@WebServlet("/api/clientes")
public class ClienteApiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ClienteDAO dao = new ClienteDAO();
        String cpf = request.getParameter("cpf");

        if (cpf != null && !cpf.isEmpty()) {
            Cliente cliente = dao.buscarPorCpf(cpf);
            if (cliente == null) {
                ApiUtils.writeError(response, 404, "Cliente nao encontrado");
            } else {
                ApiUtils.writeJson(response, cliente);
            }
            return;
        }

        ApiUtils.writeJson(response, dao.listarTodos());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        ClienteDAO dao = new ClienteDAO();

        Cliente cliente = ApiUtils.readJson(request, Cliente.class);
        if (cliente == null || cliente.getCpf_cliente() == null || cliente.getCpf_cliente().isBlank()) {
            ApiUtils.writeError(response, 400, "CPF e obrigatorio");
            return;
        }

        Cliente existente = dao.buscarPorCpf(cliente.getCpf_cliente());
        if (existente == null) {
            dao.salvar(cliente);
        } else {
            dao.atualizar(cliente);
        }

        ApiUtils.writeJson(response, cliente);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String cpf = request.getParameter("cpf");
        if (cpf == null || cpf.isEmpty()) {
            ApiUtils.writeError(response, 400, "Parametro cpf e obrigatorio");
            return;
        }
        new ClienteDAO().remover(cpf);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
