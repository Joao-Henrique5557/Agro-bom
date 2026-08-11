package br.com.agrobombackend.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import br.com.agrobombackend.dao.SolicitacaoCompraDAO;
import br.com.agrobombackend.model.SolicitacaoCompra;

/**
 * API REST (JSON) de Solicitacoes de Compra aos fornecedores.
 *   GET    /api/solicitacoes           -> lista todas
 *   GET    /api/solicitacoes?id=1      -> busca uma solicitacao
 *   POST   /api/solicitacoes           -> cria (sem id) ou atualiza (com id)
 *   DELETE /api/solicitacoes?id=1      -> remove
 *
 * O JSON de entrada usa data_solicitacao no formato "yyyy-MM-dd".
 */
@WebServlet("/api/solicitacoes")
public class SolicitacaoApiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static class SolicitacaoPayload {
        int id;
        int numero_solicitacao;
        String data_solicitacao;
        String situacao;
        double valor_total;
        int id_fornecedor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SolicitacaoCompraDAO dao = new SolicitacaoCompraDAO();
        String id = request.getParameter("id");

        if (id != null && !id.isEmpty()) {
            SolicitacaoCompra s = dao.buscarPorId(Integer.parseInt(id));
            if (s == null) {
                ApiUtils.writeError(response, 404, "Solicitacao nao encontrada");
            } else {
                ApiUtils.writeJson(response, s);
            }
            return;
        }

        ApiUtils.writeJson(response, dao.listarTodos());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        SolicitacaoCompraDAO dao = new SolicitacaoCompraDAO();

        SolicitacaoPayload payload = ApiUtils.readJson(request, SolicitacaoPayload.class);
        if (payload == null || payload.data_solicitacao == null || payload.id_fornecedor <= 0) {
            ApiUtils.writeError(response, 400, "data_solicitacao e id_fornecedor sao obrigatorios");
            return;
        }

        try {
            SolicitacaoCompra s = new SolicitacaoCompra();
            s.setNumero_solicitacao(payload.numero_solicitacao);
            s.setData_solicitacao(new SimpleDateFormat("yyyy-MM-dd").parse(payload.data_solicitacao));
            s.setSituacao(payload.situacao == null ? "ABERTA" : payload.situacao);
            s.setValor_total(payload.valor_total);
            s.setId_fornecedor(payload.id_fornecedor);

            if (payload.id > 0) {
                s.setId_solicitacao(payload.id);
                dao.atualizar(s);
            } else {
                dao.salvar(s);
            }

            ApiUtils.writeJson(response, s);
        } catch (Exception e) {
            ApiUtils.writeError(response, 400, "Data invalida, use yyyy-MM-dd");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            ApiUtils.writeError(response, 400, "Parametro id e obrigatorio");
            return;
        }
        new SolicitacaoCompraDAO().remover(Integer.parseInt(id));
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
