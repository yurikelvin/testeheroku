package br.com.ufcg.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ufcg.domain.Especialidade;
import br.com.ufcg.repositories.EspecialidadeRepository;

@Service
public class EspecialidadeService {
	
	private static final String ESPACO_EM_BRANCO = " ";
	private static final String VAZIO = "";
	
	@Autowired
	EspecialidadeRepository especialidadeRepository;
	
	private boolean validarEspecialidade(Especialidade especialidade) throws Exception {
		String nome = especialidade.getNome();
		boolean ehUnica = especialidadeEhUnica(nome);
		
		if (nome.equals(ESPACO_EM_BRANCO) || nome.equals(VAZIO) || !ehUnica) {
			throw new Exception("Insira uma especialidade valida");
		}
		
		return true;
	}
	
	private boolean especialidadeEhUnica(String nomeEspecialidade) throws Exception {
		Especialidade especialidade = especialidadeRepository.findByNome(nomeEspecialidade);
		
		if (especialidade != null) {
			throw new Exception("Ja existe uma especialidade com esse nome!");
		}
		
		return true;
	}
	
	public Especialidade criarEspecialidade(Especialidade especialidade) throws Exception {
		if (validarEspecialidade(especialidade)) {
			Especialidade esp = especialidade;
			return especialidadeRepository.save(esp);
		}
		
		return null;
	}

	public List<Especialidade> getEspecialidades() {
		return especialidadeRepository.findAll();
	}
	
	public List<String> getNomesEspecialidades() {
		Iterable<Especialidade> especialidades = getEspecialidades();
		List<String> nomes = new ArrayList<>();
		
		for (Especialidade especialidade: especialidades) {
			nomes.add(especialidade.getNome());
		}
		
		return nomes;
	}
	
	public List<Especialidade> getEspecialidadesValidas(List<Especialidade> especialidades) {
		ArrayList<Especialidade> especialidadesValidas = new ArrayList<>();
		List<Especialidade> especialidadesCadastradas = getEspecialidades();
		for(Especialidade especialidade: especialidades) {
			for(Especialidade especialidadeC: especialidadesCadastradas) {
				if(especialidadeC.getNome().equalsIgnoreCase(especialidade.getNome())) {
					especialidadesValidas.add(especialidadeC);
					}
			}
		}
		
		return especialidadesValidas;
	}
}
