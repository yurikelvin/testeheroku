package br.com.ufcg.dao;

import br.com.ufcg.domain.enums.TipoUsuario;

public class ClienteDAO extends UsuarioDAO {

	public ClienteDAO(Long id, String login, String nomeCompleto, String email, String fotoPerfil, TipoUsuario tipoUsuario) {
		super(id, login, nomeCompleto, email, fotoPerfil, tipoUsuario);
	}

}
