package br.com.agrobombackend.model;

import java.util.Date;

public class SolicitacaoCompra {

	private int id_solicitacao;
	private int numero_solicitacao;
	private Date data_solicitacao;
	private String situacao;
	private double valor_total;
	private int id_fornecedor;

	public int getId_solicitacao() {
		return id_solicitacao;
	}

	public void setId_solicitacao(int id_solicitacao) {
		this.id_solicitacao = id_solicitacao;
	}

	public int getNumero_solicitacao() {
		return numero_solicitacao;
	}

	public void setNumero_solicitacao(int numero_solicitacao) {
		this.numero_solicitacao = numero_solicitacao;
	}

	public Date getData_solicitacao() {
		return data_solicitacao;
	}

	public void setData_solicitacao(Date data_solicitacao) {
		this.data_solicitacao = data_solicitacao;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public double getValor_total() {
		return valor_total;
	}

	public void setValor_total(double valor_total) {
		this.valor_total = valor_total;
	}

	public int getId_fornecedor() {
		return id_fornecedor;
	}

	public void setId_fornecedor(int id_fornecedor) {
		this.id_fornecedor = id_fornecedor;
	}
}