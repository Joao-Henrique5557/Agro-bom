package br.com.agrobombackend.model;

public class Produto_fornecedor {

	private int id_produto_fornecedor;
	private int id_produto;
	private int id_fornecedor;

	public Produto_fornecedor() {
		super();
	}

	public Produto_fornecedor(int id_produto_fornecedor, int id_produto, int id_fornecedor) {
		super();
		this.id_produto_fornecedor = id_produto_fornecedor;
		this.id_produto = id_produto;
		this.id_fornecedor = id_fornecedor;
	}

	public int getId_produto_fornecedor() {
		return id_produto_fornecedor;
	}

	public void setId_produto_fornecedor(int id_produto_fornecedor) {
		this.id_produto_fornecedor = id_produto_fornecedor;
	}

	public int getId_produto() {
		return id_produto;
	}

	public void setId_produto(int id_produto) {
		this.id_produto = id_produto;
	}

	public int getId_fornecedor() {
		return id_fornecedor;
	}

	public void setId_fornecedor(int id_fornecedor) {
		this.id_fornecedor = id_fornecedor;
	}
}