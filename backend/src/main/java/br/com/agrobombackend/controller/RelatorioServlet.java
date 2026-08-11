package br.com.agrobombackend.controller;

import java.io.IOException;
import java.time.LocalDate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import br.com.agrobombackend.dao.RelatorioDAO;

@WebServlet("/relatorios")
public class RelatorioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RelatorioDAO dao = new RelatorioDAO();
        String rel = request.getParameter("rel");

        if (rel == null) {
            request.getRequestDispatcher("/paginas/relatorios.jsp").forward(request, response);
            return;
        }

        switch (rel) {

            case "1":
                request.setAttribute("rel1", dao.relatorio1_estoque());
                break;

            case "2":
                int mes2 = intParam(request, "mes", LocalDate.now().getMonthValue());
                int ano2 = intParam(request, "ano", LocalDate.now().getYear());
                request.setAttribute("rel2", dao.relatorio2_pedidosPorMes(mes2, ano2));
                break;

            case "3":
                String di = request.getParameter("data_inicio");
                String df = request.getParameter("data_fim");
                if (di != null && !di.isEmpty() && df != null && !df.isEmpty()) {
                    request.setAttribute("rel3",
                        dao.relatorio3_pedidosPorIntervalo(LocalDate.parse(di), LocalDate.parse(df)));
                }
                break;

            case "4":
                request.setAttribute("rel4", dao.relatorio4_fornecedoresPorProduto());
                break;

            case "5":
                int mes5 = intParam(request, "mes5", LocalDate.now().getMonthValue());
                int ano5 = intParam(request, "ano5", LocalDate.now().getYear());
                request.setAttribute("rel5", dao.relatorio5_solicitacoesPorMes(mes5, ano5));
                break;

            case "6":
                request.setAttribute("rel6", dao.relatorio6_volumeFinanceiro12Meses());
                break;
        }

        request.getRequestDispatcher("/paginas/relatorios.jsp").forward(request, response);
    }

    // Pacote-privado para permitir cobertura via teste unitario.
    static int intParam(HttpServletRequest req, String name, int defaultVal) {
        String v = req.getParameter(name);
        if (v == null || v.isEmpty()) return defaultVal;
        try { return Integer.parseInt(v); } catch (NumberFormatException e) { return defaultVal; }
    }
}
