package br.com.ufcg.services;

import br.com.ufcg.domain.Especialidade;
import br.com.ufcg.domain.Fornecedor;
import br.com.ufcg.dao.ServicoDAO;
import br.com.ufcg.domain.Cliente;
import br.com.ufcg.domain.Servico;
import br.com.ufcg.domain.Usuario;
import br.com.ufcg.domain.enums.TipoStatus;
import br.com.ufcg.repositories.ServicoRepository;
import br.com.ufcg.util.validadores.ServicoValidador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicoService {

    private static final String FORNECEDOR_SEM_SERVICOS = "O fornecedor nao possui nenhum servico aceito.";
    
	@Autowired
    ServicoRepository servicoRepository;

    public Servico criarServico(Usuario cliente, Servico servico) throws Exception {
    	if(!(cliente instanceof Cliente)) {
    		throw new Exception("Apenas clientes podem criar serviços!");
    	}
    	
    	servico.setCliente((Cliente) cliente);
    	servico.setStatus(TipoStatus.EM_ABERTO);
        ServicoValidador.valida(servico);
        servico.setFornecedor(null);
        servico.setTipo(servico.getTipo().toLowerCase());
        Servico hasServico = servicoRepository.findServico(servico.getData(), servico.getHorario(), servico.getCliente(), servico.getTipo());
		
		if (hasServico != null) {
            throw new Exception("Serviço já cadastrado no banco de dados.");
        }
		
		Servico servicoCriado = servicoRepository.save(servico);
		return servicoCriado;
    }
   
	public List<Servico> getServicosDisponiveisFornecedor(Fornecedor fornecedor){
    	
    	List<Servico> servicosDisponiveisFornecedor = new ArrayList<>();
    	List<Servico> servicos = this.getAll();
    	List<Especialidade> especialidadeFornecedor = fornecedor.getListaEspecialidades();
    	
    	for(Servico servico : servicos ) {
    		for(Especialidade especialidade : especialidadeFornecedor) {
    			if(servico.getTipo().equalsIgnoreCase(especialidade.getNome()) && servico.getStatus().equals(TipoStatus.EM_ABERTO)) {
    				servicosDisponiveisFornecedor.add(servico);
    			}
    		}
    	}
    	
    	return servicosDisponiveisFornecedor;
    	
    }

	public List<Servico> getAll() {
		return servicoRepository.findAll();
	}

	public List<Servico> getServicosClienteEmProgresso(Cliente cliente) {
		List<Servico> todosServicos = new ArrayList<>();
		List<Servico> servicosEmAberto = servicoRepository.findServicoClienteStatus(cliente, TipoStatus.EM_ABERTO);
		List<Servico> servicosAceitos = servicoRepository.findServicoClienteStatus(cliente, TipoStatus.ACEITO);
		todosServicos.addAll(servicosAceitos);
		todosServicos.addAll(servicosEmAberto);
		
		return todosServicos;
	}
	
	public List<Servico> getServicosCliente(Cliente cliente) throws Exception {
		return servicoRepository.findServicosCliente(cliente);
	}
	
	public List<ServicoDAO> setServicosToDAO(List<Servico> servicos) {
		List<ServicoDAO> servicosDAO = new ArrayList<ServicoDAO>();
		for(Servico servico: servicos) {
			servicosDAO.add(servico.toDAO());
		}
		
		return servicosDAO;
	}
	
	public List<ServicoDAO> ordenaServicosPorData(List<Servico> servicos) {
		List<Servico> servicosOrdenados = servicos;
		Collections.sort(servicosOrdenados, new Comparator<Servico>(){

			  public int compare(Servico servico1, Servico servico2)
			  {
				  if(servico1.getData().isAfter(servico2.getData())) {
						return 1;
					} else if(servico1.getData().isBefore(servico2.getData())) {
						return -1;
					} else if(servico1.getHorario().isAfter(servico2.getHorario())) {
					  return 1;
				  } else if(servico1.getHorario().isBefore(servico2.getHorario())) {
					  return -1;
				  }
					return 0;
			  }
			});
		
		return setServicosToDAO(servicosOrdenados);
	}
	
	public Servico setServicoParaFornecedor(Servico servico, Usuario fornecedor) throws Exception {
		if(!(fornecedor instanceof Fornecedor)) {
			throw new Exception("Apenas fornecedores podem aceitar serviços!");
		}
		
		if(!servicoEhValidoParaFornecedor(servico, (Fornecedor) fornecedor)) {
			throw new Exception("Você não possui a especialidade requerida para o serviço");
		}
		
		if(!servico.getStatus().equals(TipoStatus.EM_ABERTO)) {
			throw new Exception("Você só pode aceitar serviços que estão em aberto!");
		}
		
		Servico servicoAtualizado = servico;
		servicoAtualizado.setStatus(TipoStatus.ACEITO);
		servicoAtualizado.setFornecedor((Fornecedor) fornecedor);
	
		return servicoRepository.saveAndFlush(servicoAtualizado);
		
		
	}
	
	public boolean checarCliente(Servico servico, Cliente cliente){
		if(servico.getCliente().equals(cliente)) {
			return true;
		}
		return false;
}


	public boolean checarFornecedor(Servico servico, Fornecedor fornecedor){
		if(servico.getFornecedor() != null) {
			if(servico.getFornecedor().equals(fornecedor)) {
				return true;
			}
		}
		return false;
}

	public Servico cancelarServicoCliente(Servico servico, Cliente cliente) throws Exception {
		if(!checarCliente(servico, cliente)) {
			throw new Exception("Você só pode cancelar serviços que foram solicitados por você!");
		}
		
		if(!checarStatus(servico)) {
			throw new Exception("Não é possivel cancelar esse serviço, pois ele já foi cancelado ou concluido!");
		}
		
		Servico servicoAtualizado = servico;
		servicoAtualizado.setStatus(TipoStatus.CANCELADO);
		servicoAtualizado.setFornecedor(null);
		return servicoRepository.saveAndFlush(servicoAtualizado);
		
	}		

	public Servico concluirServico(Servico servico, Fornecedor fornecedor) throws Exception {
		if(!checarFornecedor(servico, fornecedor)) {
			throw new Exception("Você só pode concluir serviços que você mesmo aceitou!");
		}
		
		if(!checarStatus(servico)) {
			throw new Exception("Só é possível concluir serviços que possuem status ACEITO!");
		}
		Servico servicoAtualizado = servico;
		servicoAtualizado.setStatus(TipoStatus.CONCLUIDO);
		return servicoRepository.saveAndFlush(servicoAtualizado);
		
	}
	
	public ServicoDAO getServico(Servico servico) {
		Servico foundServico = servicoRepository.findServico(servico.getData(), servico.getHorario(), servico.getCliente(), servico.getTipo().toLowerCase());
		return foundServico.toDAO();
	}
	
	public Servico getServicoByID(long id) throws Exception {
		Servico servico = servicoRepository.findByID(id);
		if(servico == null) {
			throw new Exception("Serviço não cadastrado no banco de dados.");
		}
		
		return servico;
	}

	public boolean servicoEhValidoParaFornecedor(Servico servico, Fornecedor fornecedor) throws Exception {
		List<Especialidade> especialidadesDoFornecedor = fornecedor.getListaEspecialidades();
		
		for(Especialidade esp: especialidadesDoFornecedor) {
			if(esp.getNome().equalsIgnoreCase(servico.getTipo())) {
				return true;
			}
		}
		
		return false;
	}

	public List<Servico> getServicosDoFornecedor(Fornecedor fornecedor) throws Exception {
		List<Servico> servicos = servicoRepository.findServicosFornecedor(fornecedor);
		
		if(servicos.size() == 0) {
			throw new Exception(FORNECEDOR_SEM_SERVICOS);
		}
		
		return servicos;
	}

	public boolean checarStatus(Servico servicoAtualizado) {
		boolean cancelado = servicoAtualizado.getStatus().equals(TipoStatus.CANCELADO);
		boolean concluido = servicoAtualizado.getStatus().equals(TipoStatus.CONCLUIDO);
		if(cancelado || concluido) {
			return false;
		}
		
		return true;
	}
	
	public boolean checarServicoFornecedor(Servico servico, Fornecedor fornecedor) throws Exception {
		boolean servicoEhDoFornecedor = getServicosDoFornecedor(fornecedor).contains(servico);
		boolean servicoEstaAceito = servicoEstaAceito(servico);
		
		return servicoEhDoFornecedor && servicoEstaAceito;
	}
	
	public boolean servicoEstaAceito(Servico servico) throws Exception {
		
		if(servico.getStatus().equals(TipoStatus.ACEITO)) {
			return true;
		}
		
		throw new Exception("Esse servico nao foi aceito!");
	}

	public Servico cancelarServicoFornecedor(Servico servico, Fornecedor fornecedor) throws Exception {
		if(!checarServicoFornecedor(servico, fornecedor)) {
			throw new Exception("Você só pode cancelar serviços aceitos por você!");
		}
		Servico servicoCancelado = servico;
		servicoCancelado.setStatus(TipoStatus.EM_ABERTO);
		servicoCancelado.setFornecedor(null);
		
		return servicoRepository.saveAndFlush(servicoCancelado);
	}

	public Servico atualizarServico(Servico servico) {
		return servicoRepository.saveAndFlush(servico);
		
	}

	public List<Servico> getServicosAceitosDoFornecedor(Fornecedor fornecedor) throws Exception {
		return servicoRepository.findServicoFornecedorStatus(fornecedor, TipoStatus.ACEITO);
	}
}
