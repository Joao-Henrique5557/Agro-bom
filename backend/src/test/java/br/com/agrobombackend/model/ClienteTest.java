package br.com.agrobombackend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ClienteTest {

    @Test
    void getterSetter_preservamOsValoresAtribuidos() {
        Cliente cliente = new Cliente();
        cliente.setCpf_cliente("123.456.789-00");
        cliente.setNome_cliente("Maria da Silva");
        cliente.setTelefone_cliente("82912345678");
        cliente.setEndereco_cliente("Av. Brasil, 500");

        assertEquals("123.456.789-00", cliente.getCpf_cliente());
        assertEquals("Maria da Silva", cliente.getNome_cliente());
        assertEquals("82912345678", cliente.getTelefone_cliente());
        assertEquals("Av. Brasil, 500", cliente.getEndereco_cliente());
    }
}
