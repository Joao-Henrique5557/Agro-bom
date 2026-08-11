package br.com.agrobombackend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.com.agrobombackend.connection.ConnectionFactory;
import br.com.agrobombackend.model.Item_solicitacao;

public class ItemSolicitacaoDAO {

    public void salvar(Item_solicitacao item) {

        String sql = "INSERT INTO ITEM_SOLICITACAO (quantidade_solicitada, preco_unitario, id_solicitacao, id_produto) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, item.getQuantidade_solicitada());
            stmt.setDouble(2, item.getPreco_unitario());
            stmt.setInt(3, item.getId_solicitacao());
            stmt.setInt(4, item.getId_produto());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Item_solicitacao> listarTodos() {

        List<Item_solicitacao> lista = new ArrayList<>();

        String sql = "SELECT * FROM ITEM_SOLICITACAO";

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

    public List<Item_solicitacao> listarPorSolicitacao(int idSolicitacao) {

        List<Item_solicitacao> lista = new ArrayList<>();

        String sql = "SELECT * FROM ITEM_SOLICITACAO WHERE id_solicitacao = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idSolicitacao);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Item_solicitacao buscarPorId(int id) {

        Item_solicitacao item = null;

        String sql = "SELECT * FROM ITEM_SOLICITACAO WHERE id_item_solicitacao = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    item = mapear(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return item;
    }

    public void atualizar(Item_solicitacao item) {

        String sql = "UPDATE ITEM_SOLICITACAO SET quantidade_solicitada = ?, preco_unitario = ?, id_solicitacao = ?, id_produto = ? WHERE id_item_solicitacao = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, item.getQuantidade_solicitada());
            stmt.setDouble(2, item.getPreco_unitario());
            stmt.setInt(3, item.getId_solicitacao());
            stmt.setInt(4, item.getId_produto());
            stmt.setInt(5, item.getId_item_solicitacao());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remover(int id) {

        String sql = "DELETE FROM ITEM_SOLICITACAO WHERE id_item_solicitacao = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Item_solicitacao mapear(ResultSet rs) throws java.sql.SQLException {
        Item_solicitacao item = new Item_solicitacao();
        item.setId_item_solicitacao(rs.getInt("id_item_solicitacao"));
        item.setQuantidade_solicitada(rs.getInt("quantidade_solicitada"));
        item.setPreco_unitario(rs.getDouble("preco_unitario"));
        item.setId_solicitacao(rs.getInt("id_solicitacao"));
        item.setId_produto(rs.getInt("id_produto"));
        return item;
    }
}
