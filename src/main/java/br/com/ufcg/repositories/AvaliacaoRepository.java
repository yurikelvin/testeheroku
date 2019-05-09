package br.com.ufcg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.ufcg.domain.Avaliacao;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

	@Query("SELECT a FROM Avaliacao a WHERE a.id=:id")
	Avaliacao find(@Param("id") Long id);

	/*
	 * @Query("Select a FROM Avaliacao a INNER JOIN usuario_avaliacoes b ON a.id=:b.avaliacao_id WHERE b.usuario_id=:id_u"
	 * ) List<Avaliacao> findByUsuario(@Param("id_u") Long id_u);
	 */
}
