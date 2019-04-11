package br.com.ufcg.util.validadores;

import br.com.ufcg.domain.Usuario;
import br.com.ufcg.domain.vo.NovaSenhaForm;

public class SenhaFormValidador {
	
	private static final String SENHA_INCORRETA_EXCEPTION = "A senha informada esta incorreta!";
	private static final String SENHAS_DIFERENTES_EXCEPTION = "A nova senha e a confimacao devem ser identicas!";

	public static void valida(Usuario usuario, NovaSenhaForm form) throws Exception {
		validaSenhaAntiga(usuario.getSenha(), form.getSenhaAntiga());
		validaSenhaNova(form.getSenhaNova());
		validaSenhaNovaComConfirmacao(form.getSenhaNova(), form.getConfirmacao());
		validaSenhasDiferentes(form.getSenhaNova(), usuario.getSenha());
	}

	private static void validaSenhasDiferentes(String senhaNova, String senhaAntiga) throws Exception {
		if(senhaNova.equals(senhaAntiga)) {
			throw new Exception("A nova senha tem que ser diferente da antiga!");
		}
		
	}

	private static void validaSenhaAntiga(String senhaUsuario, String senhaInformada) throws Exception {
		
		if(!senhaUsuario.equals(senhaInformada)) {
			throw new Exception(SENHA_INCORRETA_EXCEPTION);
		}
		
	}
	
	private static void validaSenhaNovaComConfirmacao(String senhaNova, String confirmacao) throws Exception {
		if(!senhaNova.equals(confirmacao)) {
			throw new Exception(SENHAS_DIFERENTES_EXCEPTION);
		}
	}
	
	private static void validaSenhaNova(String senhaNova) throws Exception {
		UsuarioValidador.validaSenha(senhaNova);
	}
}
