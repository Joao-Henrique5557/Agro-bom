package br.com.agrobombackend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
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
import br.com.agrobombackend.model.Produto;

class ProdutoDAOTest {

    private MockedStatic<ConnectionFactory> connectionFactoryMock;
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;

    private final ProdutoDAO dao = new ProdutoDAO();

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
    void listarTodos_mapeiaTodosOsCamposDoProduto() throws Exception {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id_produto")).thenReturn(1);
        when(resultSet.getString("nome")).thenReturn("Adubo NPK");
        when(resultSet.getString("descricao")).thenReturn("Adubo NPK 20-05-20");
        when(resultSet.getDouble("preco")).thenReturn(89.90);
        when(resultSet.getString("unidade_medida")).thenReturn("kg");
        when(resultSet.getInt("quantidade_estoque")).thenReturn(50);
        when(resultSet.getInt("quantidade_ideal")).thenReturn(20);

        List<Produto> produtos = dao.listarTodos();

        assertEquals(1, produtos.size());
        Produto p = produtos.get(0);
        assertEquals("Adubo NPK", p.getNome());
        assertEquals(50, p.getQuantidade_estoque());
        assertEquals(20, p.getQuantidade_ideal());
        assertTrue(p.getQuantidade_estoque() > p.getQuantidade_ideal(), "Estoque nao deveria estar critico neste caso");
    }

    @Test
    void salvar_executaInsertComOsParametrosCorretos() throws Exception {
        Produto produto = new Produto();
        produto.setNome("Semente de Milho");
        produto.setDescricao("Semente hibrida");
        produto.setPreco(120.0);
        produto.setUnidade_medida("kg");
        produto.setQuantidade_estoque(10);
        produto.setQuantidade_ideal(15);

        dao.salvar(produto);

        verify(statement).setString(1, "Semente de Milho");
        verify(statement).setString(2, "Semente hibrida");
        verify(statement).setDouble(3, 120.0);
        verify(statement).setString(4, "kg");
        verify(statement).setInt(5, 10);
        verify(statement).setInt(6, 15);
        verify(statement).executeUpdate();
    }

    @Test
    void buscarPorId_retornaProdutoQuandoEncontrado() throws Exception {
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id_produto")).thenReturn(7);
        when(resultSet.getString("nome")).thenReturn("Calcario");
        when(resultSet.getString("descricao")).thenReturn("Calcario dolomitico");
        when(resultSet.getDouble("preco")).thenReturn(45.5);
        when(resultSet.getString("unidade_medida")).thenReturn("t");
        when(resultSet.getInt("quantidade_estoque")).thenReturn(3);
        when(resultSet.getInt("quantidade_ideal")).thenReturn(10);

        Produto produto = dao.buscarPorId(7);

        assertEquals("Calcario", produto.getNome());
        assertTrue(produto.getQuantidade_estoque() <= produto.getQuantidade_ideal(),
                "Este produto deveria estar em nivel critico de estoque");
    }
}
