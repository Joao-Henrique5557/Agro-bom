package br.com.agrobombackend.api;

import java.io.IOException;
import java.time.LocalDate;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import br.com.agrobombackend.dao.RelatorioDAO;

/**
 * API REST (JSON) para os 6 relatorios gerenciais, consumida pela tela de
 * Relatorios do app React Native.
 *
 *   GET /api/relatorios?rel=1
 *   GET /api/relatorios?rel=2&mes=5&ano=2026
 *   GET /api/relatorios?rel=3&data_inicio=2026-01-01&data_fim=2026-05-31
 *   GET /api/relatorios?rel=4
 *   GET /api/relatorios?rel=5&mes=5&ano=2026
 *   GET /api/relatorios?rel=6
 */
@WebServlet("/api/relatorios")
public class RelatorioApiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        RelatorioDAO dao = new RelatorioDAO();
        String rel = request.getParameter("rel");

        if (rel == null) {
            ApiUtils.writeError(response, 400, "Parametro rel (1-6) e obrigatorio");
            return;
        }

        switch (rel) {
            case "1":
                ApiUtils.writeJson(response, dao.relatorio1_estoque());
                break;

            case "2": {
                int mes = intParam(request, "mes", LocalDate.now().getMonthValue());
                int ano = intParam(request, "ano", LocalDate.now().getYear());
                ApiUtils.writeJson(response, dao.relatorio2_pedidosPorMes(mes, ano));
                break;
            }

            case "3": {
                String di = request.getParameter("data_inicio");
                String df = request.getParameter("data_fim");
                if (di == null || di.isEmpty() || df == null || df.isEmpty()) {
                    ApiUtils.writeError(response, 400, "data_inicio e data_fim sao obrigatorios");
                    return;
                }
                ApiUtils.writeJson(response,
                        dao.relatorio3_pedidosPorIntervalo(LocalDate.parse(di), LocalDate.parse(df)));
                break;
            }

            case "4":
                ApiUtils.writeJson(response, dao.relatorio4_fornecedoresPorProduto());
                break;

            case "5": {
                int mes = intParam(request, "mes", LocalDate.now().getMonthValue());
                int ano = intParam(request, "ano", LocalDate.now().getYear());
                ApiUtils.writeJson(response, dao.relatorio5_solicitacoesPorMes(mes, ano));
                break;
            }

            case "6":
                ApiUtils.writeJson(response, dao.relatorio6_volumeFinanceiro12Meses());
                break;

            default:
                ApiUtils.writeError(response, 400, "rel deve estar entre 1 e 6");
        }
    }

    private int intParam(HttpServletRequest req, String name, int defaultVal) {
        String v = req.getParameter(name);
        if (v == null || v.isEmpty()) return defaultVal;
        try { return Integer.parseInt(v); } catch (NumberFormatException e) { return defaultVal; }
    }
}
