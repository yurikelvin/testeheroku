package br.com.ufcg.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "TAB_ESPECIALIDADE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Especialidade {
	
	@Id
	@GeneratedValue
	@Column(name = "ID_ESPECIALIDADE")
	private Long id;
	
	@Column(name = "TX_NOME", nullable = false)
	private String nome;

	public Especialidade(String nome) {
		super();
		this.nome = nome;
	}
	
	public Especialidade() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public String toString() {
		return getNome();
	}
}
