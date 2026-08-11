package br.com.agrobombackend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.agrobombackend.connection.ConnectionFactory;
import br.com.agrobombackend.model.Pedido;

public class PedidoDAO {

    public void salvar(Pedido pedido) {

        String sql = "INSERT INTO PEDIDO (numero_pedido, data_pedido, desconto, valor_total, cpf_cliente) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, pedido.getNumero_pedido());
            stmt.setDate(2, java.sql.Date.valueOf(pedido.getData_pedido()));
            stmt.setDouble(3, pedido.getDesconto());
            stmt.setDouble(4, pedido.getValor_total());
            stmt.setString(5, pedido.getCpf_cliente());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Pedido> listarTodos() {

        List<Pedido> pedidos = new ArrayList<>();

        String sql = "SELECT * FROM PEDIDO";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pedidos.add(mapear(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return pedidos;
    }

    public List<Pedido> listarPorMes(int mes, int ano) {

        List<Pedido> pedidos = new ArrayList<>();

        String sql = "SELECT * FROM PEDIDO WHERE MONTH(data_pedido) = ? AND YEAR(data_pedido) = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, mes);
            stmt.setInt(2, ano);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapear(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return pedidos;
    }

    public List<Pedido> listarPorIntervalo(LocalDate dataInicio, LocalDate dataFim) {

        List<Pedido> pedidos = new ArrayList<>();

        String sql = "SELECT * FROM PEDIDO WHERE data_pedido BETWEEN ? AND ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(dataInicio));
            stmt.setDate(2, java.sql.Date.valueOf(dataFim));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapear(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return pedidos;
    }

    public Pedido buscarPorId(int id) {

        Pedido pedido = null;

        String sql = "SELECT * FROM PEDIDO WHERE id_pedido = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    pedido = mapear(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return pedido;
    }

    public void atualizar(Pedido pedido) {

        String sql = "UPDATE PEDIDO SET numero_pedido = ?, data_pedido = ?, desconto = ?, valor_total = ?, cpf_cliente = ? WHERE id_pedido = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, pedido.getNumero_pedido());
            stmt.setDate(2, java.sql.Date.valueOf(pedido.getData_pedido()));
            stmt.setDouble(3, pedido.getDesconto());
            stmt.setDouble(4, pedido.getValor_total());
            stmt.setString(5, pedido.getCpf_cliente());
            stmt.setInt(6, pedido.getId_pedido());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remover(int id) {

        String sql = "DELETE FROM PEDIDO WHERE id_pedido = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Pedido mapear(ResultSet rs) throws java.sql.SQLException {
        Pedido pedido = new Pedido();
        pedido.setId_pedido(rs.getInt("id_pedido"));
        pedido.setNumero_pedido(rs.getInt("numero_pedido"));
        pedido.setData_pedido(rs.getDate("data_pedido").toLocalDate());
        pedido.setDesconto(rs.getDouble("desconto"));
        pedido.setValor_total(rs.getDouble("valor_total"));
        pedido.setCpf_cliente(rs.getString("cpf_cliente"));
        return pedido;
    }
}
