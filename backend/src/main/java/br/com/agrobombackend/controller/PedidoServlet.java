package br.com.agrobombackend.controller;

import java.io.IOException;
import java.time.LocalDate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import br.com.agrobombackend.dao.ClienteDAO;
import br.com.agrobombackend.dao.PedidoDAO;
import br.com.agrobombackend.model.Pedido;

@WebServlet("/pedidos")
public class PedidoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PedidoDAO dao = new PedidoDAO();
        ClienteDAO clienteDAO = new ClienteDAO();
        String acao = request.getParameter("acao");

        if ("editar".equals(acao)) {
            int id = Integer.parseInt(request.getParameter("id"));
            request.setAttribute("pedido", dao.buscarPorId(id));
        }

        request.setAttribute("pedidos",  dao.listarTodos());
        request.setAttribute("clientes", clienteDAO.listarTodos());
        request.getRequestDispatcher("/paginas/pedidos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        PedidoDAO dao = new PedidoDAO();
        String acao = request.getParameter("acao");

        if ("remover".equals(acao)) {
            dao.remover(Integer.parseInt(request.getParameter("id")));
        } else {
            Pedido p = new Pedido();
            p.setNumero_pedido(Integer.parseInt(request.getParameter("numero_pedido")));
            p.setData_pedido(LocalDate.parse(request.getParameter("data_pedido")));
            p.setDesconto(Double.parseDouble(request.getParameter("desconto")));
            p.setValor_total(Double.parseDouble(request.getParameter("valor_total")));
            p.setCpf_cliente(request.getParameter("cpf_cliente"));

            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                p.setId_pedido(Integer.parseInt(idParam));
                dao.atualizar(p);
            } else {
                dao.salvar(p);
            }
        }
        response.sendRedirect("pedidos");
    }
}
