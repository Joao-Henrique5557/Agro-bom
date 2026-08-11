package br.com.agrobombackend.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import br.com.agrobombackend.dao.FornecedorDAO;
import br.com.agrobombackend.model.Fornecedor;

@WebServlet("/fornecedores")
public class FornecedorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        FornecedorDAO dao = new FornecedorDAO();
        String acao = request.getParameter("acao");

        if ("editar".equals(acao)) {
            int id = Integer.parseInt(request.getParameter("id"));
            request.setAttribute("fornecedor", dao.buscarPorId(id));
        }

        request.setAttribute("fornecedores", dao.listarTodos());
        request.getRequestDispatcher("/paginas/fornecedores.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        FornecedorDAO dao = new FornecedorDAO();
        String acao = request.getParameter("acao");

        if ("remover".equals(acao)) {
            dao.remover(Integer.parseInt(request.getParameter("id")));
        } else {
            Fornecedor f = new Fornecedor();
            f.setNome(request.getParameter("nome"));
            f.setCnpj(request.getParameter("cnpj"));
            f.setTelefone(request.getParameter("telefone"));

            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                f.setId_fornecedor(Integer.parseInt(idParam));
                dao.atualizar(f);
            } else {
                dao.salvar(f);
            }
        }
        response.sendRedirect("fornecedores");
    }
}
