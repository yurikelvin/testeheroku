package br.com.ufcg.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ufcg.dao.UsuarioDAO;
import br.com.ufcg.domain.Especialidade;
import br.com.ufcg.domain.Fornecedor;
import br.com.ufcg.domain.Usuario;
import br.com.ufcg.domain.enums.TipoUsuario;
import br.com.ufcg.domain.vo.AlterarDadosForm;
import br.com.ufcg.domain.vo.NovaSenhaForm;
import br.com.ufcg.repositories.UsuarioRepository;
import br.com.ufcg.util.validadores.SenhaFormValidador;
import br.com.ufcg.util.validadores.UsuarioValidador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UsuarioService {

	// CONSTANTES NECESSÁRIAS AO SERVICE

	private static final String USUARIO_NAO_ENCONTRADO_EXCEPTION = "Usuario nao encontrado";
	private static final String EMAIL_LOGIN_JA_EXISTENTE_EXCEPTION = "Email e/ou login já estão sendo usados. Tente outros, por favor.";
	private static final String FORNECEDOR_SEM_ESPECIALIDADE = "O fornecedor tem que ter ao menos 1 especialidade";
	private static final String SENHA_ATUAL_IGUAL_NOVA = "A nova senha tem que ser diferente do antigo!";
	private static final String EMAIL_ATUAL_IGUAL_NOVO = "O novo email tem que ser diferente do antigo!";
	private static final String LOGIN_ATUAL_IGUAL_NOVO = "O novo login tem que ser diferente do antigo!";

	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	EmailService emailService;

	@Autowired
	EspecialidadeService especialidadeService;
	
	public Usuario getById(Long id) throws Exception {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new Exception(USUARIO_NAO_ENCONTRADO_EXCEPTION));
	}

	public Usuario getByLogin(String login) throws Exception {
		Usuario usuario = usuarioRepository.findByLogin(login);

		if (usuario == null) {
			throw new Exception(USUARIO_NAO_ENCONTRADO_EXCEPTION);
		}

		return usuario;
	}
	
	public Usuario getByEmail(String email) throws Exception {
		Usuario usuario = usuarioRepository.findByEmail(email);

		if (usuario == null) {
			throw new Exception(USUARIO_NAO_ENCONTRADO_EXCEPTION);
		}

		return usuario;
	}

	public boolean checkUser(String login, String senha) {
		Usuario userToCheck = usuarioRepository.findByLoginAndSenha(login, senha);

		return (userToCheck != null);
	}

	public Usuario criarUsuario(Usuario usuario) throws Exception {
		if (UsuarioValidador.validaUsuario(usuario) && usuarioEhUnico(usuario)) {
			if (usuario.getTipo().equals(TipoUsuario.FORNECEDOR)) {
				
				usuario = criarFornecedor(usuario, ((Fornecedor) usuario).getListaEspecialidades());
			}
			usuario.setLogin(usuario.getLogin().toLowerCase());
			return usuarioRepository.save(usuario);
		}

		throw new Exception("Problemas ao cadastrar usuario! Campos invalidos!");
	}

	private Usuario criarFornecedor(Usuario usuario, List<Especialidade> especialidades) throws Exception {
		List<Especialidade> especialidadesValidas = setEspecialidadesFornecedor(
				((Fornecedor) usuario).getListaEspecialidades());

		if (especialidadesValidas.size() > 0) {
			((Fornecedor) usuario).setListaEspecialidades(especialidadesValidas);

			return usuario;
		}

		throw new Exception(FORNECEDOR_SEM_ESPECIALIDADE);
	}

	private List<Especialidade> setEspecialidadesFornecedor(List<Especialidade> especialidades) {
		return especialidadeService.getEspecialidadesValidas(especialidades);
	}

	private boolean usuarioEhUnico(Usuario usuario) throws Exception {

		boolean ehUnico = true;
		String email = usuario.getEmail();
		String login = usuario.getLogin();

		Iterable<Usuario> usuarios = usuarioRepository.findAll();
		for (Usuario user : usuarios) {
			if (user.getLogin().equalsIgnoreCase(login) || user.getEmail().equalsIgnoreCase(email)) {
				ehUnico = false;
			}
		}

		if (!ehUnico) {
			throw new Exception(EMAIL_LOGIN_JA_EXISTENTE_EXCEPTION);
		}

		return true;
	}

	public List<UsuarioDAO> getClientes() {
		Iterable<Usuario> listaUsuarios = usuarioRepository.findAll();
		ArrayList<UsuarioDAO> clientes = new ArrayList<UsuarioDAO>();

		for (Usuario usuario : listaUsuarios) {
			if (TipoUsuario.CLIENTE.equals(usuario.getTipo())) {
				clientes.add(usuario.toDAO());
			}
		}

		return clientes;
	}

	public List<UsuarioDAO> getFornecedores() {
		Iterable<Usuario> listaUsuarios = usuarioRepository.findAll();
		ArrayList<UsuarioDAO> fornecedores = new ArrayList<UsuarioDAO>();

		for (Usuario usuario : listaUsuarios) {
			if (TipoUsuario.FORNECEDOR.equals(usuario.getTipo())) {
				fornecedores.add(usuario.toDAO());
			}
		}

		return fornecedores;
	}

	public void atualizarSenha(Usuario usuario, String senha) throws Exception {
		String senhaNova = senha;
		UsuarioValidador.validaSenha(senhaNova);
		String senhaAntiga = usuario.getSenha();

		if (senhaNova.equals(senhaAntiga)) {
			throw new Exception(SENHA_ATUAL_IGUAL_NOVA);
		}

		usuario.setSenha(senhaNova);
		usuarioRepository.saveAndFlush(usuario);
	}

	public void atualizarNome(Usuario usuario, String nomeCompleto) throws Exception {
		if (UsuarioValidador.validaNome(nomeCompleto.trim())) {
			usuario.setNomeCompleto(nomeCompleto.trim());
			usuarioRepository.saveAndFlush(usuario);
		}
	}

	public void atualizarFotoDoPerfil(Usuario usuario, String fotoPerfil) throws Exception {
		if (fotoPerfil.contains(" ") || fotoPerfil.equals("")) {
			throw new Exception("É obrigatório uma foto para o perfil!");
		}

		usuario.setFotoPerfil(fotoPerfil);
		usuarioRepository.saveAndFlush(usuario);
	}

	public void atualizarSenha(Usuario usuario, NovaSenhaForm form) throws Exception {
		if (form.getSenhaNova() == null || form.getConfirmacao() == null || form.getSenhaAntiga() == null) {
			throw new Exception("Preencha todos os campos!");
		}
		
		SenhaFormValidador.valida(usuario, form);
		Usuario usuarioAtualizado = usuario;
		usuarioAtualizado.setSenha(form.getSenhaNova());
		usuarioRepository.saveAndFlush(usuarioAtualizado);
	}

	public void atualizarDados(Usuario usuario, AlterarDadosForm form) throws Exception {
		if (form.getNovaFotoPerfil() == null || form.getNovoEmail() == null || form.getNovoLogin() == null
				|| form.getNovoNomeCompleto() == null
				|| (usuario instanceof Fornecedor && form.getNovaEspecialidades() == null)) {
			throw new Exception("Problemas no formulario! Preencha corretamente.");
		}

		if (!form.getNovoLogin().trim().equalsIgnoreCase(usuario.getLogin())) {
			atualizarLogin(usuario, form.getNovoLogin());
		}

		if (!form.getNovoNomeCompleto().equalsIgnoreCase(usuario.getNomeCompleto())) {
			atualizarNome(usuario, form.getNovoNomeCompleto());
		}

		if (usuario instanceof Fornecedor) {
			atualizarEspecialidades(usuario, form.getNovaEspecialidades());
		}

		if (!form.getNovoEmail().equalsIgnoreCase(usuario.getEmail())) {
			atualizarEmail(usuario, form.getNovoEmail());
		}

		if (!form.getNovaFotoPerfil().equals(usuario.getFotoPerfil())) {
			atualizarFotoDoPerfil(usuario, form.getNovaFotoPerfil());
		}
	}

	private void atualizarEmail(Usuario usuario, String novoEmail) throws Exception {
		String emailAntigo = usuario.getEmail();
		Usuario usuarioEmail = usuarioRepository.findByEmail(novoEmail);
		UsuarioValidador.validaEmail(novoEmail.toLowerCase());

		if (emailAntigo.equalsIgnoreCase(novoEmail)) {
			throw new Exception(EMAIL_ATUAL_IGUAL_NOVO);
		}

		if (usuarioEmail != null) {
			throw new Exception(EMAIL_LOGIN_JA_EXISTENTE_EXCEPTION);
		}

		usuario.setEmail(novoEmail.toLowerCase());
		usuarioRepository.saveAndFlush(usuario);
	}

	private void atualizarEspecialidades(Usuario usuario, List<Especialidade> novaEspecialidades) throws Exception {
		if (usuario.getTipo().equals(TipoUsuario.FORNECEDOR)) {
			List<Especialidade> especialidades = especialidadeService.getEspecialidadesValidas(novaEspecialidades);
			if (especialidades.size() > 0) {
				((Fornecedor) usuario).setListaEspecialidades(especialidades);
				usuarioRepository.saveAndFlush(usuario);
			} else {
				throw new Exception("Forneca ao menos uma especialidade valida!");
			}

		} else {
			throw new Exception("Apenas fornecedores podem atualizar as especialidades!");
		}
	}

	private void atualizarLogin(Usuario usuario, String novoLogin) throws Exception {
		String loginAntigo = usuario.getLogin();
		UsuarioValidador.validaLogin(novoLogin);
		Usuario usuarioLogin = usuarioRepository.findByLogin(novoLogin);

		if (loginAntigo.equals(novoLogin)) {
			throw new Exception(LOGIN_ATUAL_IGUAL_NOVO);
		}

		if (usuarioLogin != null) {
			throw new Exception(EMAIL_LOGIN_JA_EXISTENTE_EXCEPTION);
		}

		usuario.setLogin(novoLogin.toLowerCase());
		usuarioRepository.saveAndFlush(usuario);
	}

	public Usuario atualizarUsuario(Usuario usuario) throws Exception {
		if (usuarioRepository.findById(usuario.getId()) == null) {
			throw new Exception("Usuário não cadastrado no sistema.");
		}

		return usuarioRepository.saveAndFlush(usuario);
	}

	public void solicitaRecuperacaoSenha(String email) throws Exception {
		UsuarioValidador.validaEmail(email);
		Usuario usuario = getByEmail(email.toLowerCase());
		String novaSenha = gerarSenha();
		usuario.setSenha(novaSenha);
		
		usuarioRepository.saveAndFlush(usuario);	
		
		emailService.recuperarSenha(usuario, novaSenha);
		
	}
	
	private String gerarSenha() {
		String senha = "";
		String[] caracteres = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		Random random = new Random();
		
		for (int i = 0; i < 10; i++){
			int a = random.nextInt(caracteres.length);
			senha += caracteres[a];
		}
		
		return senha;
	}
	
}

	
