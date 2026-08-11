package br.com.agrobombackend.model;

public class Item_solicitacao {

    private int id_item_solicitacao;
    private int quantidade_solicitada;
    private double preco_unitario;
    private int id_solicitacao;
    private int id_produto;

    public Item_solicitacao() {
        super();
    }

    public Item_solicitacao(int id_item_solicitacao, int quantidade_solicitada, double preco_unitario,
            int id_solicitacao, int id_produto) {
        super();
        this.id_item_solicitacao = id_item_solicitacao;
        this.quantidade_solicitada = quantidade_solicitada;
        this.preco_unitario = preco_unitario;
        this.id_solicitacao = id_solicitacao;
        this.id_produto = id_produto;
    }

    public int getId_item_solicitacao() {
        return id_item_solicitacao;
    }

    public void setId_item_solicitacao(int id_item_solicitacao) {
        this.id_item_solicitacao = id_item_solicitacao;
    }

    // CORRIGIDO: getter estava como getQuant_solicitada() — padronizado
    public int getQuantidade_solicitada() {
        return quantidade_solicitada;
    }

    public void setQuantidade_solicitada(int quantidade_solicitada) {
        this.quantidade_solicitada = quantidade_solicitada;
    }

    public double getPreco_unitario() {
        return preco_unitario;
    }

    public void setPreco_unitario(double preco_unitario) {
        this.preco_unitario = preco_unitario;
    }

    public int getId_solicitacao() {
        return id_solicitacao;
    }

    public void setId_solicitacao(int id_solicitacao) {
        this.id_solicitacao = id_solicitacao;
    }

    public int getId_produto() {
        return id_produto;
    }

    public void setId_produto(int id_produto) {
        this.id_produto = id_produto;
    }
}
