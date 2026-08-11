package br.com.agrobombackend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import br.com.agrobombackend.connection.ConnectionFactory;
import br.com.agrobombackend.model.Cliente;

/**
 * Testes unitarios do ClienteDAO. A conexao JDBC e totalmente mockada
 * (Connection / PreparedStatement / ResultSet) para que os testes rodem sem
 * depender de um banco de dados MySQL real.
 */
class ClienteDAOTest {

    private MockedStatic<ConnectionFactory> connectionFactoryMock;
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;

    private final ClienteDAO dao = new ClienteDAO();

    @BeforeEach
    void setUp() throws Exception {
        connection = Mockito.mock(Connection.class);
        statement = Mockito.mock(PreparedStatement.class);
        resultSet = Mockito.mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        connectionFactoryMock = mockStatic(ConnectionFactory.class);
        connectionFactoryMock.when(ConnectionFactory::getConnection).thenReturn(connection);
    }

    @AfterEach
    void tearDown() {
        connectionFactoryMock.close();
    }

    @Test
    void listarTodos_devolveListaMapeadaDoResultSet() throws Exception {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("cpf")).thenReturn("111.111.111-11");
        when(resultSet.getString("nome")).thenReturn("Fazenda Boa Vista");
        when(resultSet.getString("telefone")).thenReturn("82999990000");
        when(resultSet.getString("endereco")).thenReturn("Zona Rural, s/n");

        List<Cliente> clientes = dao.listarTodos();

        assertEquals(1, clientes.size());
        assertEquals("Fazenda Boa Vista", clientes.get(0).getNome_cliente());
        assertEquals("111.111.111-11", clientes.get(0).getCpf_cliente());
    }

    @Test
    void listarTodos_devolveListaVaziaQuandoNaoHaRegistros() throws Exception {
        when(resultSet.next()).thenReturn(false);

        List<Cliente> clientes = dao.listarTodos();

        assertEquals(0, clientes.size());
    }

    @Test
    void buscarPorCpf_devolveNuloQuandoNaoEncontrado() throws Exception {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Cliente cliente = dao.buscarPorCpf("000.000.000-00");

        assertNull(cliente);
    }

    @Test
    void salvar_executaInsertComOsParametrosCorretos() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setCpf_cliente("222.222.222-22");
        cliente.setNome_cliente("Joao Henrique");
        cliente.setTelefone_cliente("82988887777");
        cliente.setEndereco_cliente("Rua das Palmeiras, 10");

        dao.salvar(cliente);

        verify(statement).setString(1, "222.222.222-22");
        verify(statement).setString(2, "Joao Henrique");
        verify(statement).setString(3, "82988887777");
        verify(statement).setString(4, "Rua das Palmeiras, 10");
        verify(statement, times(1)).executeUpdate();
    }

    @Test
    void remover_executaDeleteComOCpfInformado() throws Exception {
        dao.remover("333.333.333-33");

        verify(statement).setString(1, "333.333.333-33");
        verify(statement, times(1)).executeUpdate();
    }
}
