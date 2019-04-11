package br.com.ufcg.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.ufcg.dao.UsuarioDAO;
import br.com.ufcg.domain.enums.TipoUsuario;

@Entity
@Table(name = "TAB_USUARIO", uniqueConstraints = @UniqueConstraint(columnNames = "TX_LOGIN", name = "login"))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public abstract class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_USUARIO")
	private Long id;

	@Column(name = "TX_NOME_COMPLETO")
	private String nomeCompleto;

	@Column(name = "TX_LOGIN")
	private String login;

	@Column(name = "CD_FOTO_PERFIL", nullable = false)
	private String fotoPerfil;

	@Column(name = "TX_EMAIL", nullable = false)
	private String email;

	@Column(name = "TX_SENHA", nullable = false)
	private String senha;

	@Column(name = "CD_TIPO", nullable = false, updatable = false)
	@Enumerated
	private TipoUsuario tipo;

	public Usuario(String nomeCompleto, String login, String fotoPerfil, String email, String senha, TipoUsuario tipo) {
		super();
		this.nomeCompleto = nomeCompleto;
		this.login = login;
		this.fotoPerfil = fotoPerfil;
		this.email = email;
		this.senha = senha;
		this.tipo = tipo;
	}

	public Usuario() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getFotoPerfil() {
		return fotoPerfil;
	}

	public void setFotoPerfil(String fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public TipoUsuario getTipo() {
		return tipo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public abstract UsuarioDAO toDAO();
}
