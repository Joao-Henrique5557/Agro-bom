package br.com.agrobombackend.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import br.com.agrobombackend.dao.ProdutoDAO;
import br.com.agrobombackend.model.Produto;

@WebServlet("/produtos")
public class ProdutoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProdutoDAO dao = new ProdutoDAO();
        String acao = request.getParameter("acao");

        if ("editar".equals(acao)) {
            int id = Integer.parseInt(request.getParameter("id"));
            request.setAttribute("produto", dao.buscarPorId(id));
        }

        request.setAttribute("produtos", dao.listarTodos());
        request.getRequestDispatcher("/paginas/produtos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        ProdutoDAO dao = new ProdutoDAO();
        String acao = request.getParameter("acao");

        if ("remover".equals(acao)) {
            dao.remover(Integer.parseInt(request.getParameter("id")));
        } else {
            Produto p = new Produto();
            p.setNome(request.getParameter("nome"));
            p.setDescricao(request.getParameter("descricao"));
            p.setPreco(Double.parseDouble(request.getParameter("preco")));
            p.setUnidade_medida(request.getParameter("unidade_medida"));
            p.setQuantidade_estoque(Integer.parseInt(request.getParameter("quantidade_estoque")));
            p.setQuantidade_ideal(Integer.parseInt(request.getParameter("quantidade_ideal")));

            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                p.setId_produto(Integer.parseInt(idParam));
                dao.atualizar(p);
            } else {
                dao.salvar(p);
            }
        }
        response.sendRedirect("produtos");
    }
}
