package br.com.ufcg.projectes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.ufcg.controllers.UsuarioController;
import br.com.ufcg.domain.Cliente;
import br.com.ufcg.domain.Especialidade;
import br.com.ufcg.domain.Fornecedor;
import br.com.ufcg.domain.Usuario;
import br.com.ufcg.domain.enums.TipoUsuario;
import br.com.ufcg.domain.vo.AlterarDadosForm;
import br.com.ufcg.domain.vo.LoginForm;
import br.com.ufcg.domain.vo.NovaSenhaForm;
import br.com.ufcg.repositories.EspecialidadeRepository;
import br.com.ufcg.repositories.UsuarioRepository;
import br.com.ufcg.services.EspecialidadeService;
import br.com.ufcg.services.UsuarioService;
import br.com.ufcg.util.response.Response;
import br.com.ufcg.util.validadores.UsuarioValidador;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioControllerTest {


	@Autowired
	UsuarioController usuarioController;

	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	EspecialidadeService especialidadeService;
	
	@Autowired
	EspecialidadeRepository er;
	
	@Autowired
	UsuarioRepository ur;
	
	private Usuario cliente1;
	private Usuario cliente2;
	private Usuario fornecedor1;
	private Usuario fornecedor2;
	private List<Especialidade> especialidades1;
	private List<Especialidade> especialidades2;
	private Especialidade especialidade1;
	private Especialidade especialidade2;
	private Especialidade especialidade3;
	private Especialidade especialidade4;
	
	
	
	@Before
	@Transactional
	public void setUp() {
		especialidade1 = new Especialidade("encanador");
		especialidade2 = new Especialidade("pintor");
		especialidade3 = new Especialidade("pedreiro");
		especialidade4 = new Especialidade("motorista");
		especialidades1 = new ArrayList<>();
		especialidades2 = new ArrayList<>();
		especialidades1.add(especialidade1);
		especialidades1.add(especialidade2);
		especialidades1.add(especialidade4);
		especialidades2.add(especialidade3);
		especialidades2.add(especialidade1);
		try {
			especialidadeService.criarEspecialidade(especialidade1);
			especialidadeService.criarEspecialidade(especialidade2);
			especialidadeService.criarEspecialidade(especialidade3);
			especialidadeService.criarEspecialidade(especialidade4);
		} catch(Exception e) {
			
		}
		cliente1 = new Cliente("Joao Da Silva", "joao", "foto.png", "joao@gmail.com", "123456789");
		cliente2 = new Cliente("Paulo Cesar", "pauloc","minha.png", "paulo@gmail.com", "abcdefgh");
		fornecedor1 = new Fornecedor("Vitor Hugo", "vitor", "afoto.png", "vivi@gmail.com", "vitor123456", especialidades1);
		fornecedor2 = new Fornecedor("Caio Otavio", "caio", "fotinha.png", "caio@gmail.com", "abc123456", especialidades2);
		
		usuarioController.cadastrarCliente((Cliente) cliente1);
		usuarioController.cadastrarCliente((Cliente) cliente2);
		usuarioController.cadastrarFornecedor((Fornecedor) fornecedor1);
		usuarioController.cadastrarFornecedor((Fornecedor) fornecedor2);
		
	}
	
	@After
	@Transactional
	public void limpeza() {
		ur.deleteAll();
		er.deleteAll();
	}
	
	
	@Test
	@Transactional
	public void testCriarClienteValido() {
		Cliente cliente1 = new Cliente("Tiberio Gadelha M", "tiberiogadelha", "/imgs/foto.png",
				"tiberio.gomes@ccc.ufcg.edu.br", "123456789");
		try {
			usuarioController.cadastrarCliente(cliente1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Usuario us1 = null;

		try {
			us1 = usuarioService.getByLogin(cliente1.getLogin());
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals("Tiberio Gadelha M", us1.getNomeCompleto());
		assertEquals("tiberiogadelha", us1.getLogin());
		assertEquals("tiberio.gomes@ccc.ufcg.edu.br", us1.getEmail());
		assertEquals(TipoUsuario.CLIENTE, us1.getTipo());
		assertTrue(usuarioService.getClientes().contains(us1.toDAO()));

		
	}

	@Test
	@Transactional
	public void testCriarClienteInValido() {

		// Testa cliente com login de tamanho inferior a 4.
		Cliente cliente1 = new Cliente("Gustavo Victor", "gu", "/imgs/foto.png", "gustavo.victor@ccc.ufcg.edu.br",
				"123456789");
		try {
			usuarioController.cadastrarCliente(cliente1);
		} catch (Exception e) {
			assertEquals("O login deve ter no minimo 4 digitos e nao pode conter espaco", e.getMessage());
		}

		// Testa cliente com nome de tamanho inferior a 8
		Cliente cliente2 = new Cliente("Joao", "joaohenrique", "/imgs/foto.png", "joao.henrique@ccc.ufcg.edu.br",
				"123456789");
		try {
			usuarioController.cadastrarCliente(cliente2);
		} catch (Exception e) {
			assertEquals("O nome completo deve ter no minimo 8 digitos", e.getMessage());
		}

		// Testa cliente com senha de tamanho inferior a 8
		Cliente cliente3 = new Cliente("Emanuel Brito ", "emaulbrito", "/imgs/foto.png",
				"emanuel.brito@ccc.ufcg.edu.br", "1289");
		try {
			usuarioController.cadastrarCliente(cliente3);
		} catch (Exception e) {
			assertEquals("A senha deve ter no minimo 8 digitos", e.getMessage());
		}
	}

	@Test
	@Transactional
	public void testCriarClienteDuplicado() {
		// Testa cadastrar dois clientes com mesmo email e login
		Cliente cliente4 = new Cliente("Tiberio Gadelha M", "tiberiogomes", "/imgs/foto.png", "tiberio@ccc.ufcg.edu.br",
				"123456789");
		try {
			usuarioController.cadastrarCliente(cliente4);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			usuarioController.cadastrarCliente(cliente4);
		} catch (Exception e) {
			assertEquals("Email e/ou login já estão sendo usandos. Tente outros, por favor.", e.getMessage());
		}
	}

	@Test
	@Transactional
	public void testCriarFornecedorValido() {
		List<Especialidade> listaEspecialidade = new ArrayList<>();
		listaEspecialidade.add(new Especialidade("Pintor"));
		listaEspecialidade.add(new Especialidade("Encanador"));
		listaEspecialidade.add(new Especialidade("Pedreiro"));

		Fornecedor fornecedor1 = new Fornecedor("Carlos Alberto dos Anjos", "carlosaba", "/imgs/foto.png",
				"carlos.alberto1@gmail.com", "123456789", listaEspecialidade);
		try {
			usuarioController.cadastrarFornecedor(fornecedor1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Usuario foundByLogin = null;
		try {
			foundByLogin = usuarioService.getByLogin(fornecedor1.getLogin());
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(foundByLogin.getNomeCompleto().equals(fornecedor1.getNomeCompleto()));
		assertEquals(3, ((Fornecedor) foundByLogin).getListaEspecialidades().size());
		assertTrue(usuarioService.getFornecedores().contains(foundByLogin.toDAO()));
	}
	
	@Test
	@Transactional
	public void testAtualizarCamposValidos() throws Exception  {
		Usuario copia = new Cliente("Joao Da Silva", "joao", "foto.png", "joao@gmail.com", "123456789");
		Usuario usuarioAntes = usuarioService.getByLogin("joao");
		
		AlterarDadosForm form = new AlterarDadosForm("Yuri Silva", "yuri", "yuri@gmail.com", null, "afoto.png");
		
		try {
			usuarioService.atualizarDados(usuarioAntes, form);
			Usuario usuarioAtualizado = usuarioService.getByLogin("yuri");
			assertNotEquals(copia.getNomeCompleto(), usuarioAtualizado.getNomeCompleto());
			assertEquals(usuarioAntes, usuarioAtualizado);
			assertEquals(usuarioAntes.getId(), usuarioAtualizado.getId());
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	@Test
	@Transactional
	public void testAtualizarNomeInvalido() throws Exception {
	
		Usuario fornecedor = usuarioService.getByLogin(fornecedor1.getLogin());
		AlterarDadosForm form = new AlterarDadosForm("a", "vitor", "vivi@gmail.com", especialidades1, "afoto.png");
		
		try {
			usuarioService.atualizarDados(fornecedor, form);
		} catch(Exception e) {
			
			assertEquals(UsuarioValidador.TAMANHO_MINIMO_NOME_EXCEPTION, e.getMessage());
		}
		
		AlterarDadosForm form2 = new AlterarDadosForm("UM NOME MUITO GRANDE PARA ISSO DAR PROBLEMA ABCSDEFUHGFIUGFUYSGFUYWGYFGSUGFQDQIHDQ  Z", "vitor", "vivi@gmail.com", especialidades1, "afoto.png");
		
		try {
			usuarioService.atualizarDados(fornecedor, form2);
		} catch(Exception e) {
			assertEquals(UsuarioValidador.TAMANHO_MAXIMO_NOME_EXCEPTION, e.getMessage());
		}
		
	}
	
	
	@Test
	@Transactional
	public void testAtualizarEmailInvalido() throws Exception {
		Usuario cliente = usuarioService.getByLogin("pauloc");		
		
		// Formado de email invalido
		AlterarDadosForm form = new AlterarDadosForm("Paulo Cesar", "pauloc", "email.png", null, "minha.png");
		
		// Email de um usuario ja cadastrado
		AlterarDadosForm form2 = new AlterarDadosForm("Paulo Cesar", "pauloc", "caio@gmail.com", null, "minha.png");
		
		// Email vazio
		AlterarDadosForm form3 = new AlterarDadosForm("Paulo Cesar", "pauloc", "", null, "minha.png");
		
		try {
			usuarioService.atualizarDados(cliente, form);
		} catch(Exception e) {
			assertEquals(UsuarioValidador.FORMATO_EMAIL_INVALIDO, e.getMessage());
			
		}
		
		try {
			usuarioService.atualizarDados(cliente, form2);
		} catch(Exception e) {
			assertEquals("Email e/ou login já estão sendo usados. Tente outros, por favor.", e.getMessage());
			
		}
		
		try {
			usuarioService.atualizarDados(cliente, form3);
		} catch(Exception e) {
			assertEquals(UsuarioValidador.FORMATO_EMAIL_INVALIDO, e.getMessage());
		}
	}
	
	@Test
	@Transactional
	public void testAtualizarSenhaValida() throws Exception {
		String senhaAntiga = "123456789";
		Usuario usuario = usuarioService.getByLogin("joao");
		String senhaNova = "testando123456";
		NovaSenhaForm form = new NovaSenhaForm(usuario.getSenha(), senhaNova, senhaNova );
		
		try {
			usuarioService.atualizarSenha(usuario, form);
			assertNotEquals(senhaAntiga, usuario.getSenha());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Transactional
	public void testAtualizarSenhaInvalida() throws Exception {
		Usuario usuario = usuarioService.getByLogin("vitor");
		String senhaAntiga = usuario.getSenha();
		// A senha atual fornecida nao eh a do cadastro.
		NovaSenhaForm form1 = new NovaSenhaForm("uma senha ai", "123456789", "123456789");
		
		try {
			usuarioService.atualizarSenha(usuario, form1);
		} catch(Exception e) {
			assertEquals("A senha informada esta incorreta!", e.getMessage());
		}
		
		// As novas senhas estao diferentes
		NovaSenhaForm form2 = new NovaSenhaForm(senhaAntiga, "1234567411", "abcdefghhi");
		
		try {
			usuarioService.atualizarSenha(usuario, form2);
		} catch(Exception e) {
			assertEquals("A nova senha e a confimacao devem ser identicas!", e.getMessage());
		}
		
		// A nova senha eh igual a antiga
		NovaSenhaForm form3 = new NovaSenhaForm(senhaAntiga, senhaAntiga, senhaAntiga);
		
		try {
			usuarioService.atualizarSenha(usuario, form3);
		} catch(Exception e) {
			assertEquals("A nova senha tem que ser diferente da antiga!", e.getMessage());
		}
		
		// A nova senha eh muito pequena
		NovaSenhaForm form4 = new NovaSenhaForm(senhaAntiga, "123", "123");
		
		try {
			usuarioService.atualizarSenha(usuario, form4);
		} catch(Exception e) {
			assertEquals("A senha deve ter no minimo 8 digitos", e.getMessage());
		}
		
		// A nova senha eh muito grande
		NovaSenhaForm form5 = new NovaSenhaForm(senhaAntiga, "uma senha muuuuuitoooo grandeeeeee", "uma senha muuuuuitoooo grandeeeeee");
		
		try {
			usuarioService.atualizarSenha(usuario, form5);
		} catch(Exception e) {
			assertEquals("A senha deve ter no maximo 20 digitos", e.getMessage());
		}
		
		// Nenhuma senha foi informada
		NovaSenhaForm form6 = new NovaSenhaForm(senhaAntiga, null, null);
		
		try {
			usuarioService.atualizarSenha(usuario, form6);
		} catch(Exception e) {
			assertEquals("Preencha todos os campos!", e.getMessage());
		}
		
	}
	
	@Test
	@Transactional
	public void testAtualizarEspecialidadesValidas() throws Exception {
		Fornecedor fornecedor = (Fornecedor) usuarioService.getByLogin("caio");
		int numEspecialidadesAntiga = fornecedor.getListaEspecialidades().size();
		List<Especialidade> novaEspecialidades = new ArrayList<>();
		novaEspecialidades.add(especialidade1);
		AlterarDadosForm form = new AlterarDadosForm("Caio O", "caio", "caio@gmail.com", novaEspecialidades,"fotinha.png");
		
		try {
			usuarioService.atualizarDados(fornecedor, form);
			assertEquals(1, fornecedor.getListaEspecialidades().size());
			assertNotEquals(numEspecialidadesAntiga, fornecedor.getListaEspecialidades().size());
		} catch(Exception e) {
			
		}
	}
	
	@Test
	@Transactional
	public void testAtualizarEspecialidadesInvalidas() throws Exception {
		Fornecedor fornecedor = (Fornecedor) usuarioService.getByLogin("vitor");
		List<Especialidade> especialidadesVazia = new ArrayList<>();
		List<Especialidade> especialidadesInvalida = new ArrayList<>();
		Especialidade espInvalida = new Especialidade("Invalida");
		especialidadesInvalida.add(espInvalida);
		
		// Nenhuma especialidade fornecida
		AlterarDadosForm form1 = new AlterarDadosForm("Vitor Hugo", "vitor", "vivi@gmail.com", null, "afoto.png");
		
		try {
			usuarioService.atualizarDados(fornecedor, form1);
			
		} catch(Exception e) {
			assertEquals("Problemas no formulario! Preencha corretamente.", e.getMessage());
		}
		
		// Especiadades vazia
		AlterarDadosForm form2 = new AlterarDadosForm("Vitor Hugo", "vitor", "vivi@gmail.com", especialidadesVazia, "afoto.png");
		
		try {
			usuarioService.atualizarDados(fornecedor, form2);
		} catch(Exception e) {
			assertEquals("Forneca ao menos uma especialidade valida!", e.getMessage());
		}
		
		AlterarDadosForm form3 = new AlterarDadosForm("Vitor Hugo", "vitor", "vivi@gmail.com", especialidadesInvalida, "afoto.png");
		
		try {
			usuarioService.atualizarDados(fornecedor, form3);
		} catch(Exception e) {
			assertEquals("Forneca ao menos uma especialidade valida!", e.getMessage());
		}
	} 
	
	@Test
	@Transactional
	public void testAtualizarLoginInvalido() throws Exception {
		Usuario usuario = usuarioService.getByLogin("pauloc");
		
		String loginNulo = null;
		String loginVazio = "";
		String loginPequeno = "ab";
		String loginGrande = "umloginmuitograndeparaissonaofuncionar!!!!!!!";
		String loginRepetido = "joao";
		String loginAntigo = "pauloc";
	
		// Login vazio
		AlterarDadosForm form1 = new AlterarDadosForm("Paulo Cesar", loginVazio, "paulo@gmail.com", null, "minha.png");
		
		try {
			usuarioService.atualizarDados(usuario, form1);
		} catch(Exception e) {
			assertEquals("O login deve ter no minimo 4 digitos e nao pode conter espaco", e.getMessage());
		}
		
		// Login com menos de 4 digitos
		AlterarDadosForm form2 = new AlterarDadosForm("Paulo Cesar", loginPequeno, "paulo@gmail.com", null, "minha.png");
		
		try {
			usuarioService.atualizarDados(usuario, form2);
		} catch(Exception e) {
			assertEquals("O login deve ter no minimo 4 digitos e nao pode conter espaco", e.getMessage());
		}
		
		//Login com mais de 15 digitos
		AlterarDadosForm form3 = new AlterarDadosForm("Paulo Cesar", loginGrande, "paulo@gmail.com", null, "minha.png");
		
		try {
			usuarioService.atualizarDados(usuario, form3);
		} catch(Exception e) {
			assertEquals("O login deve ter no maximo 15 digitos", e.getMessage());
		}
		
		// Login que ja possui cadastro no sistema
		AlterarDadosForm form4 = new AlterarDadosForm("Paulo Cesar", loginRepetido, "paulo@gmail.com", null, "minha.png");
		
		try {
			usuarioService.atualizarDados(usuario, form4);
		} catch(Exception e) {
			assertEquals("Email e/ou login já estão sendo usados. Tente outros, por favor.", e.getMessage());
		}
		
		// Login nao informado no formulario
		AlterarDadosForm form5 = new AlterarDadosForm("Paulo Cesar", loginNulo, "paulo@gmail.com", null, "minha.png");
		
		try {
			usuarioService.atualizarDados(usuario, form5);
		} catch(Exception e) {
			assertEquals("Problemas no formulario! Preencha corretamente.", e.getMessage());
		}
		
	}
	
	@Test
	@Transactional
	public void testAtualizarFotoInvalida() throws Exception {
		Usuario usuario = usuarioService.getByLogin("vitor");
		
		// Foto vazia
		AlterarDadosForm form1 = new AlterarDadosForm("Vitor Hugo", "vitor", "vivi@gmail.com", especialidades1, "");
		
		try {
			usuarioService.atualizarDados(usuario, form1);
			
		} catch(Exception e) {
			assertEquals("É obrigatório uma foto para o perfil!", e.getMessage());
		}
		
		// Foto nula
		AlterarDadosForm form2 = new AlterarDadosForm("Vitor Hugo", "vitor", "vivi@gmail.com", especialidades1, null);
		
		try {
			usuarioService.atualizarDados(usuario, form2);
			
		} catch(Exception e) {
			assertEquals("Problemas no formulario! Preencha corretamente.", e.getMessage());
		}
	}
	
	@Test
	@Transactional
	public void testLoginValido() throws Exception {
		Usuario user = usuarioService.getByLogin(cliente1.getLogin());
		
		LoginForm form = new LoginForm();
		form.setLogin(user.getLogin());
		form.setSenha(user.getSenha());
		
		try {
			ResponseEntity<Response> response = usuarioController.login(form);
			assertTrue(usuarioService.checkUser(form.getLogin(), form.getSenha()));
			assertEquals("Login efetuado com sucesso !", response.getBody().getMessage());
			assertNotEquals(null, response.getBody().getData());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Transactional
	public void testLoginInvalido() {
		
		LoginForm formVazio = new LoginForm();
		
		try {
			ResponseEntity<Response> response = usuarioController.login(formVazio);
			assertEquals(UsuarioController.FORM_INCOMPLETO, response.getBody().getMessage());
			assertEquals(null, response.getBody().getData());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		LoginForm formUsuarioInvalido = new LoginForm();
		formUsuarioInvalido.setLogin("umlogininvalido");
		formUsuarioInvalido.setSenha("umasenhaqualquer");
		
		try {
			ResponseEntity<Response> response = usuarioController.login(formUsuarioInvalido);
			assertEquals(UsuarioController.USUARIO_NAO_EXISTENTE, response.getBody().getMessage());
			assertEquals(null, response.getBody().getData());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		LoginForm formSenhaIncorreta = new LoginForm();
		formSenhaIncorreta.setLogin(cliente2.getLogin());
		formSenhaIncorreta.setSenha("fuigwquysdgfuiyewgfew");
		
		try {
			ResponseEntity<Response> response = usuarioController.login(formSenhaIncorreta);
			assertEquals(UsuarioController.SENHA_INCORRETA, response.getBody().getMessage());
			assertEquals(null, response.getBody().getData());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
