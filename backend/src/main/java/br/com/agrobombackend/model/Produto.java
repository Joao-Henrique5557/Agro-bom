package br.com.agrobombackend.model;

public class Produto {

	private int id_produto;
	private String nome;
	private String descricao;
	private double preco;
	private String unidade_medida;
	private int quantidade_estoque;
	private int quantidade_ideal;

	public int getId_produto() {
		return id_produto;
	}

	public void setId_produto(int id_produto) {
		this.id_produto = id_produto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public String getUnidade_medida() {
		return unidade_medida;
	}

	public void setUnidade_medida(String unidade_medida) {
		this.unidade_medida = unidade_medida;
	}

	public int getQuantidade_estoque() {
		return quantidade_estoque;
	}

	public void setQuantidade_estoque(int quantidade_estoque) {
		this.quantidade_estoque = quantidade_estoque;
	}

	public int getQuantidade_ideal() {
		return quantidade_ideal;
	}

	public void setQuantidade_ideal(int quantidade_ideal) {
		this.quantidade_ideal = quantidade_ideal;
	}
}