package br.com.ufcg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.ufcg.domain.Especialidade;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, String> {

	@Query("SELECT e FROM Especialidade e WHERE e.id=:id")
	Especialidade findById(@Param("id") Long id);

	@Query("SELECT e FROM Especialidade e WHERE e.nome=:nome")
	Especialidade findByNome(@Param("nome") String nome);
}
