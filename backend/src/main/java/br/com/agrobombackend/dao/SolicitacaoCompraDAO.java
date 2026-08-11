package br.com.agrobombackend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.com.agrobombackend.connection.ConnectionFactory;
import br.com.agrobombackend.model.SolicitacaoCompra;

public class SolicitacaoCompraDAO {

    public void salvar(SolicitacaoCompra solicitacao) {

        String sql = "INSERT INTO SOLICITACAO_COMPRA (numero_solicitacao, data_solicitacao, situacao, valor_total, id_fornecedor) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, solicitacao.getNumero_solicitacao());
            stmt.setDate(2, new java.sql.Date(solicitacao.getData_solicitacao().getTime()));
            stmt.setString(3, solicitacao.getSituacao());
            stmt.setDouble(4, solicitacao.getValor_total());
            stmt.setInt(5, solicitacao.getId_fornecedor());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SolicitacaoCompra> listarTodos() {

        List<SolicitacaoCompra> lista = new ArrayList<>();

        String sql = "SELECT * FROM SOLICITACAO_COMPRA";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public SolicitacaoCompra buscarPorId(int id) {

        SolicitacaoCompra s = null;

        String sql = "SELECT * FROM SOLICITACAO_COMPRA WHERE id_solicitacao = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    s = mapear(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return s;
    }

    public void atualizar(SolicitacaoCompra solicitacao) {

        String sql = "UPDATE SOLICITACAO_COMPRA SET numero_solicitacao = ?, data_solicitacao = ?, situacao = ?, valor_total = ?, id_fornecedor = ? WHERE id_solicitacao = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, solicitacao.getNumero_solicitacao());
            stmt.setDate(2, new java.sql.Date(solicitacao.getData_solicitacao().getTime()));
            stmt.setString(3, solicitacao.getSituacao());
            stmt.setDouble(4, solicitacao.getValor_total());
            stmt.setInt(5, solicitacao.getId_fornecedor());
            stmt.setInt(6, solicitacao.getId_solicitacao());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remover(int id) {

        String sql = "DELETE FROM SOLICITACAO_COMPRA WHERE id_solicitacao = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SolicitacaoCompra mapear(ResultSet rs) throws java.sql.SQLException {
        SolicitacaoCompra s = new SolicitacaoCompra();
        s.setId_solicitacao(rs.getInt("id_solicitacao"));
        s.setNumero_solicitacao(rs.getInt("numero_solicitacao"));
        s.setData_solicitacao(rs.getDate("data_solicitacao"));
        s.setSituacao(rs.getString("situacao"));
        s.setValor_total(rs.getDouble("valor_total"));
        s.setId_fornecedor(rs.getInt("id_fornecedor"));
        return s;
    }
}
