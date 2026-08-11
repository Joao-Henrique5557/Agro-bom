package br.com.agrobombackend.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import br.com.agrobombackend.dao.ClienteDAO;
import br.com.agrobombackend.model.Cliente;

@WebServlet("/clientes")
public class ClienteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ClienteDAO dao = new ClienteDAO();
        String acao = request.getParameter("acao");

        if ("editar".equals(acao)) {
            String cpf = request.getParameter("cpf");
            request.setAttribute("cliente", dao.buscarPorCpf(cpf));
        }

        request.setAttribute("clientes", dao.listarTodos());
        request.getRequestDispatcher("/paginas/clientes.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.setCharacterEncoding("UTF-8");
        ClienteDAO dao = new ClienteDAO();
        String acao = request.getParameter("acao");

        if (acao == null || acao.equals("salvar")) {

            Cliente cliente = new Cliente();
            cliente.setCpf_cliente(request.getParameter("cpf"));
            cliente.setNome_cliente(request.getParameter("nome"));
            cliente.setTelefone_cliente(request.getParameter("telefone"));
            cliente.setEndereco_cliente(request.getParameter("endereco"));

            Cliente existente = dao.buscarPorCpf(cliente.getCpf_cliente());

            if (existente == null) {
                dao.salvar(cliente);
            } else {
                dao.atualizar(cliente);
            }

            response.sendRedirect("clientes");
            return;
        }

        if ("remover".equals(acao)) {
            dao.remover(request.getParameter("cpf"));
            response.sendRedirect("clientes");
        }
    }
}
