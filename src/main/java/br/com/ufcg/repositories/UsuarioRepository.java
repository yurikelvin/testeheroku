package br.com.ufcg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.ufcg.domain.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Query("SELECT u FROM Usuario u WHERE u.login=:login AND u.senha=:senha")
	Usuario findByLoginAndSenha(@Param("login") String login, @Param("senha") String senha);

	@Query("SELECT u FROM Usuario u WHERE u.login=:login")
	Usuario findByLogin(@Param("login") String login);

	@Query("SELECT u FROM Usuario u WHERE u.email=:email")
	Usuario findByEmail(@Param("email") String email);
}
