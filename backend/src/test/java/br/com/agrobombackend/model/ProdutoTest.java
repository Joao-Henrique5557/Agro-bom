package br.com.agrobombackend.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ProdutoTest {

    @Test
    void produtoComEstoqueAbaixoDoIdeal_deveSerConsideradoCritico() {
        Produto produto = new Produto();
        produto.setQuantidade_estoque(5);
        produto.setQuantidade_ideal(20);

        boolean critico = produto.getQuantidade_estoque() <= produto.getQuantidade_ideal();

        assertTrue(critico);
    }

    @Test
    void produtoComEstoqueAcimaDoIdeal_naoDeveSerConsideradoCritico() {
        Produto produto = new Produto();
        produto.setQuantidade_estoque(100);
        produto.setQuantidade_ideal(20);

        boolean critico = produto.getQuantidade_estoque() <= produto.getQuantidade_ideal();

        assertFalse(critico);
    }
}
