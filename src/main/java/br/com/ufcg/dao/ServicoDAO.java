package br.com.ufcg.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.ufcg.domain.Cliente;
import br.com.ufcg.domain.Endereco;
import br.com.ufcg.domain.Fornecedor;
import br.com.ufcg.domain.enums.TipoStatus;

public final class ServicoDAO {
	private Long id;
	private String tipo;
	private String descricao;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate data;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "kk:mm")
	private LocalTime horario;

	private BigDecimal valor;
	private Endereco endereco;
	private UsuarioDAO cliente;
	private UsuarioDAO fornecedor;
	private TipoStatus tipoStatus;
	private Boolean isAvaliadoCliente;
	private Boolean isAvaliadoFornecedor;

	public ServicoDAO(Long id, String tipo, String descricao, LocalDate data, LocalTime horario, BigDecimal valor,
			Endereco endereco, Cliente cliente, Fornecedor fornecedor, TipoStatus tipoStatus, Boolean isAvaliadoCliente,
			Boolean isAvaliadoFornecedor) {
		this.id = id;
		this.tipo = tipo;
		this.descricao = descricao;
		this.data = data;
		this.horario = horario;
		this.valor = valor;
		this.endereco = endereco;

		if (cliente == null) {
			this.cliente = null;
		} else {
			this.cliente = cliente.toDAO();
		}

		if (fornecedor == null) {
			this.fornecedor = null;
		} else {
			this.fornecedor = fornecedor.toDAO();
		}

		this.tipoStatus = tipoStatus;
		this.isAvaliadoCliente = isAvaliadoCliente;
		this.isAvaliadoFornecedor = isAvaliadoFornecedor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public LocalTime getHorario() {
		return horario;
	}

	public void setHorario(LocalTime hora) {
		this.horario = hora;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public UsuarioDAO getCliente() {
		return cliente;
	}

	public void setCliente(UsuarioDAO cliente) {
		this.cliente = cliente;
	}

	public UsuarioDAO getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(UsuarioDAO fornecedor) {
		this.fornecedor = fornecedor;
	}

	public TipoStatus getTipoStatus() {
		return tipoStatus;
	}

	public void setTipoStatus(TipoStatus tipoStatus) {
		this.tipoStatus = tipoStatus;
	}

	public Boolean getIsAvaliadoCliente() {
		return isAvaliadoCliente;
	}

	public void setIsAvaliadoCliente(Boolean isAvaliadoCliente) {
		this.isAvaliadoCliente = isAvaliadoCliente;
	}

	public Boolean getIsAvaliadoFornecedor() {
		return isAvaliadoFornecedor;
	}

	public void setIsAvaliadoFornecedor(Boolean isAvaliadoFornecedor) {
		this.isAvaliadoFornecedor = isAvaliadoFornecedor;
	}
}
