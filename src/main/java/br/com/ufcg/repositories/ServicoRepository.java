package br.com.ufcg.repositories;

import br.com.ufcg.domain.Cliente;
import br.com.ufcg.domain.Fornecedor;
import br.com.ufcg.domain.Servico;
import br.com.ufcg.domain.enums.TipoStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {

    // Testar essa query quando fizer listagem
    //@Query(value="SELECT * FROM TB_SERVICO s WHERE s.id_cliente=:c_id ORDER BY s.data ASC s.horario ASC", nativeQuery = true)
    //List<Servico> getServicosCliente(@Param("c_id") Integer id_cliente);

    @Query(value="SELECT s FROM Servico s WHERE s.data=:dt AND s.horario=:hr AND s.cliente=:user AND s.tipo=:tipo")
    Servico findServico(@Param("dt") LocalDate data, @Param("hr") LocalTime hora, @Param("user") Cliente user, @Param("tipo") String tipo);
    
    @Query(value="SELECT s FROM Servico s WHERE s.cliente=:user")
	List<Servico> findServicosCliente(@Param("user") Cliente user);
    
    @Query(value="SELECT s FROM Servico s WHERE s.data=:dt AND s.horario=:hr AND s.tipo=:tipo")
    Servico findServico(@Param("dt") LocalDate data, @Param("hr") LocalTime hora, @Param("tipo") String tipo);
    
    @Query(value="SELECT s FROM Servico s WHERE s.id=:id")
    Servico findByID(@Param("id") long id);
    
    @Query(value="SELECT s FROM Servico s WHERE s.fornecedor=:user")
	List<Servico> findServicosFornecedor(@Param("user") Fornecedor user);
    
    @Query(value="SELECT s FROM Servico s WHERE s.fornecedor=:user and s.status=:tipostatus")
    List<Servico> findServicoFornecedorStatus(@Param("user") Fornecedor user, @Param("tipostatus") TipoStatus tipostatus);
    
    @Query(value="SELECT s FROM Servico s WHERE s.cliente=:user and s.status=:tipostatus")
    List<Servico> findServicoClienteStatus(@Param("user") Cliente user, @Param("tipostatus") TipoStatus tipostatus);

}
