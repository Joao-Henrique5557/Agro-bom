package br.com.agrobombackend.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import br.com.agrobombackend.dao.FornecedorDAO;
import br.com.agrobombackend.dao.SolicitacaoCompraDAO;
import br.com.agrobombackend.model.SolicitacaoCompra;

@WebServlet("/solicitacoes")
public class SolicitacaoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SolicitacaoCompraDAO dao = new SolicitacaoCompraDAO();
        FornecedorDAO fornecedorDAO = new FornecedorDAO();
        String acao = request.getParameter("acao");

        if ("editar".equals(acao)) {
            int id = Integer.parseInt(request.getParameter("id"));
            request.setAttribute("solicitacao", dao.buscarPorId(id));
        }

        request.setAttribute("solicitacoes", dao.listarTodos());
        request.setAttribute("fornecedores", fornecedorDAO.listarTodos());
        request.getRequestDispatcher("/paginas/solicitacoes.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        SolicitacaoCompraDAO dao = new SolicitacaoCompraDAO();
        String acao = request.getParameter("acao");

        if ("remover".equals(acao)) {
            dao.remover(Integer.parseInt(request.getParameter("id")));
        } else {
            try {
                SolicitacaoCompra s = new SolicitacaoCompra();
                s.setNumero_solicitacao(Integer.parseInt(request.getParameter("numero_solicitacao")));
                s.setData_solicitacao(new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("data_solicitacao")));
                s.setSituacao(request.getParameter("situacao"));
                s.setValor_total(Double.parseDouble(request.getParameter("valor_total")));
                s.setId_fornecedor(Integer.parseInt(request.getParameter("id_fornecedor")));

                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    s.setId_solicitacao(Integer.parseInt(idParam));
                    dao.atualizar(s);
                } else {
                    dao.salvar(s);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        response.sendRedirect("solicitacoes");
    }
}
