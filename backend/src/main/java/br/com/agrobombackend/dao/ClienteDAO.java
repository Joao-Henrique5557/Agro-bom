package br.com.agrobombackend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.com.agrobombackend.connection.ConnectionFactory;
import br.com.agrobombackend.model.Cliente;

public class ClienteDAO {

    // SALVAR
    public void salvar(Cliente cliente) {

        String sql = "INSERT INTO CLIENTE (cpf, nome, telefone, endereco) VALUES (?, ?, ?, ?)";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, cliente.getCpf_cliente());
            stmt.setString(2, cliente.getNome_cliente());
            stmt.setString(3, cliente.getTelefone_cliente());
            stmt.setString(4, cliente.getEndereco_cliente());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LISTAR TODOS
    public List<Cliente> listarTodos() {

        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT * FROM CLIENTE";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapear(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return clientes;
    }

    // BUSCAR POR CPF
    public Cliente buscarPorCpf(String cpf) {

        Cliente cliente = null;

        String sql = "SELECT * FROM CLIENTE WHERE cpf = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cliente = mapear(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cliente;
    }

    // ATUALIZAR
    public void atualizar(Cliente cliente) {

        String sql = "UPDATE CLIENTE SET nome = ?, telefone = ?, endereco = ? WHERE cpf = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome_cliente());
            stmt.setString(2, cliente.getTelefone_cliente());
            stmt.setString(3, cliente.getEndereco_cliente());
            stmt.setString(4, cliente.getCpf_cliente());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // REMOVER
    public void remover(String cpf) {

        String sql = "DELETE FROM CLIENTE WHERE cpf = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reduz duplicação de mapeamento ResultSet -> Cliente
    private Cliente mapear(ResultSet rs) throws java.sql.SQLException {
        Cliente cliente = new Cliente();
        cliente.setCpf_cliente(rs.getString("cpf"));
        cliente.setNome_cliente(rs.getString("nome"));
        cliente.setTelefone_cliente(rs.getString("telefone"));
        cliente.setEndereco_cliente(rs.getString("endereco"));
        return cliente;
    }
}
