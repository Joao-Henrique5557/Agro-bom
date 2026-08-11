package br.com.agrobombackend.model;

public class Item_pedido {

	private int id_item_pedido;
	private int quantidade_pedida;
	private double preco_unitario;
	private int id_pedido;
	private int id_produto;

	public Item_pedido() {
		super();
	}

	public Item_pedido(int id_item_pedido, int quantidade_pedida, double preco_unitario, int id_pedido,
			int id_produto) {
		super();
		this.id_item_pedido = id_item_pedido;
		this.quantidade_pedida = quantidade_pedida;
		this.preco_unitario = preco_unitario;
		this.id_pedido = id_pedido;
		this.id_produto = id_produto;
	}

	public int getId_item_pedido() {
		return id_item_pedido;
	}

	public void setId_item_pedido(int id_item_pedido) {
		this.id_item_pedido = id_item_pedido;
	}

	public int getQuantidade_pedida() {
		return quantidade_pedida;
	}

	public void setQuantidade_pedida(int quantidade_pedida) {
		this.quantidade_pedida = quantidade_pedida;
	}

	public double getPreco_unitario() {
		return preco_unitario;
	}

	public void setPreco_unitario(double preco_unitario) {
		this.preco_unitario = preco_unitario;
	}

	public int getId_pedido() {
		return id_pedido;
	}

	public void setId_pedido(int id_pedido) {
		this.id_pedido = id_pedido;
	}

	public int getId_produto() {
		return id_produto;
	}

	public void setId_produto(int id_produto) {
		this.id_produto = id_produto;
	}
}