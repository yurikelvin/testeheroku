package br.com.ufcg.dao;

import java.util.List;

import br.com.ufcg.domain.Especialidade;
import br.com.ufcg.domain.enums.TipoUsuario;

public class FornecedorDAO extends UsuarioDAO {

	private List<Especialidade> listaEspecialidades;

	public FornecedorDAO(Long id, String login, String nomeCompleto, String email, String fotoPerfil,
			TipoUsuario tipoUsuario, List<Especialidade> listaEspecialidades, double avaliacao) {
		super(id, login, nomeCompleto, email, fotoPerfil, tipoUsuario, avaliacao);
		this.setListaEspecialidades(listaEspecialidades);
	}

	public List<Especialidade> getListaEspecialidades() {
		return listaEspecialidades;
	}

	public void setListaEspecialidades(List<Especialidade> listaEspecialidades) {
		this.listaEspecialidades = listaEspecialidades;
	}
}
