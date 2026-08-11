package br.com.agrobombackend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.agrobombackend.connection.ConnectionFactory;

/**
 * DAO responsavel pelos 6 relatorios gerenciais exigidos pelo enunciado.
 */
public class RelatorioDAO {

    // -------------------------------------------------------
    // RELATORIO 1: Posicao de estoque de todos os produtos
    // -------------------------------------------------------
    public List<Map<String, Object>> relatorio1_estoque() {

        List<Map<String, Object>> resultado = new ArrayList<>();

        String sql = "SELECT nome, descricao, quantidade_estoque, unidade_medida, quantidade_ideal "
                   + "FROM PRODUTO "
                   + "ORDER BY nome";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> linha = new HashMap<>();
                linha.put("nome", rs.getString("nome"));
                linha.put("descricao", rs.getString("descricao"));
                linha.put("quantidade_estoque", rs.getInt("quantidade_estoque"));
                linha.put("unidade_medida", rs.getString("unidade_medida"));
                linha.put("quantidade_ideal", rs.getInt("quantidade_ideal"));
                resultado.add(linha);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    // -------------------------------------------------------
    // RELATORIO 2: Pedidos recebidos em cada mes do ano
    // -------------------------------------------------------
    public List<Map<String, Object>> relatorio2_pedidosPorMes(int mes, int ano) {

        List<Map<String, Object>> resultado = new ArrayList<>();

        String sql = "SELECT p.numero_pedido, p.data_pedido, "
                   + "       c.nome AS nome_cliente, c.telefone, c.endereco, "
                   + "       pr.descricao AS produto_descricao, pr.unidade_medida, "
                   + "       ip.quantidade_pedida "
                   + "FROM PEDIDO p "
                   + "JOIN CLIENTE c ON p.cpf_cliente = c.cpf "
                   + "JOIN ITEM_PEDIDO ip ON p.id_pedido = ip.id_pedido "
                   + "JOIN PRODUTO pr ON ip.id_produto = pr.id_produto "
                   + "WHERE MONTH(p.data_pedido) = ? AND YEAR(p.data_pedido) = ? "
                   + "ORDER BY p.numero_pedido";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, mes);
            stmt.setInt(2, ano);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> linha = new HashMap<>();
                    linha.put("numero_pedido", rs.getInt("numero_pedido"));
                    linha.put("data_pedido", rs.getDate("data_pedido"));
                    linha.put("nome_cliente", rs.getString("nome_cliente"));
                    linha.put("telefone", rs.getString("telefone"));
                    linha.put("endereco", rs.getString("endereco"));
                    linha.put("produto_descricao", rs.getString("produto_descricao"));
                    linha.put("unidade_medida", rs.getString("unidade_medida"));
                    linha.put("quantidade_pedida", rs.getInt("quantidade_pedida"));
                    resultado.add(linha);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    // -------------------------------------------------------
    // RELATORIO 3: Pedidos em intervalo de datas
    // -------------------------------------------------------
    public List<Map<String, Object>> relatorio3_pedidosPorIntervalo(
            java.time.LocalDate dataInicio, java.time.LocalDate dataFim) {

        List<Map<String, Object>> resultado = new ArrayList<>();

        String sql = "SELECT numero_pedido, data_pedido, desconto, valor_total "
                   + "FROM PEDIDO "
                   + "WHERE data_pedido BETWEEN ? AND ? "
                   + "ORDER BY data_pedido";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(dataInicio));
            stmt.setDate(2, java.sql.Date.valueOf(dataFim));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> linha = new HashMap<>();
                    linha.put("numero_pedido", rs.getInt("numero_pedido"));
                    linha.put("data_pedido", rs.getDate("data_pedido"));
                    linha.put("desconto", rs.getDouble("desconto"));
                    linha.put("valor_total", rs.getDouble("valor_total"));
                    resultado.add(linha);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    // -------------------------------------------------------
    // RELATORIO 4: Fornecedores de cada produto
    // -------------------------------------------------------
    public List<Map<String, Object>> relatorio4_fornecedoresPorProduto() {

        List<Map<String, Object>> resultado = new ArrayList<>();

        String sql = "SELECT pr.nome AS produto, f.nome AS fornecedor, f.cnpj, f.telefone "
                   + "FROM PRODUTO_FORNECEDOR pf "
                   + "JOIN PRODUTO pr ON pf.id_produto = pr.id_produto "
                   + "JOIN FORNECEDOR f ON pf.id_fornecedor = f.id_fornecedor "
                   + "ORDER BY pr.nome, f.nome";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> linha = new HashMap<>();
                linha.put("produto", rs.getString("produto"));
                linha.put("fornecedor", rs.getString("fornecedor"));
                linha.put("cnpj", rs.getString("cnpj"));
                linha.put("telefone", rs.getString("telefone"));
                resultado.add(linha);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    // -------------------------------------------------------
    // RELATORIO 5: Solicitacoes de compra mes a mes
    // -------------------------------------------------------
    public List<Map<String, Object>> relatorio5_solicitacoesPorMes(int mes, int ano) {

        List<Map<String, Object>> resultado = new ArrayList<>();

        String sql = "SELECT sc.numero_solicitacao, sc.data_solicitacao, sc.situacao, "
                   + "       f.nome AS fornecedor, f.cnpj, "
                   + "       pr.nome AS produto, pr.descricao, "
                   + "       its.quantidade_solicitada "
                   + "FROM SOLICITACAO_COMPRA sc "
                   + "JOIN FORNECEDOR f ON sc.id_fornecedor = f.id_fornecedor "
                   + "JOIN ITEM_SOLICITACAO its ON sc.id_solicitacao = its.id_solicitacao "
                   + "JOIN PRODUTO pr ON its.id_produto = pr.id_produto "
                   + "WHERE MONTH(sc.data_solicitacao) = ? AND YEAR(sc.data_solicitacao) = ? "
                   + "ORDER BY sc.numero_solicitacao";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, mes);
            stmt.setInt(2, ano);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> linha = new HashMap<>();
                    linha.put("numero_solicitacao", rs.getInt("numero_solicitacao"));
                    linha.put("data_solicitacao", rs.getDate("data_solicitacao"));
                    linha.put("situacao", rs.getString("situacao"));
                    linha.put("fornecedor", rs.getString("fornecedor"));
                    linha.put("cnpj", rs.getString("cnpj"));
                    linha.put("produto", rs.getString("produto"));
                    linha.put("descricao", rs.getString("descricao"));
                    linha.put("quantidade_solicitada", rs.getInt("quantidade_solicitada"));
                    resultado.add(linha);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    // -------------------------------------------------------
    // RELATORIO 6: Volume financeiro de solicitacoes e pedidos
    //              nos ultimos 12 meses, mes a mes
    // -------------------------------------------------------
    public List<Map<String, Object>> relatorio6_volumeFinanceiro12Meses() {

        List<Map<String, Object>> resultado = new ArrayList<>();

        String sql = "SELECT mes, ano, "
                   + "       SUM(total_pedidos)      AS total_pedidos, "
                   + "       SUM(total_solicitacoes) AS total_solicitacoes "
                   + "FROM ( "
                   + "    SELECT MONTH(data_pedido) AS mes, YEAR(data_pedido) AS ano, "
                   + "           SUM(valor_total) AS total_pedidos, 0 AS total_solicitacoes "
                   + "    FROM PEDIDO "
                   + "    WHERE data_pedido >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH) "
                   + "    GROUP BY YEAR(data_pedido), MONTH(data_pedido) "
                   + "    UNION ALL "
                   + "    SELECT MONTH(data_solicitacao) AS mes, YEAR(data_solicitacao) AS ano, "
                   + "           0 AS total_pedidos, SUM(valor_total) AS total_solicitacoes "
                   + "    FROM SOLICITACAO_COMPRA "
                   + "    WHERE data_solicitacao >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH) "
                   + "    GROUP BY YEAR(data_solicitacao), MONTH(data_solicitacao) "
                   + ") AS sub "
                   + "GROUP BY ano, mes "
                   + "ORDER BY ano, mes";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> linha = new HashMap<>();
                linha.put("mes", rs.getInt("mes"));
                linha.put("ano", rs.getInt("ano"));
                linha.put("total_pedidos", rs.getDouble("total_pedidos"));
                linha.put("total_solicitacoes", rs.getDouble("total_solicitacoes"));
                resultado.add(linha);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }
}
