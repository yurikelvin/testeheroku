package br.com.ufcg.domain.vo;

public class NovaSenhaForm {
	private String senhaAntiga;
	private String senhaNova;
	private String confirmacao;
	
	public NovaSenhaForm() {
		
	}
	
	public NovaSenhaForm(String senhaAntiga, String senhaNova, String confirmacao) {
		this.senhaAntiga = senhaAntiga;
		this.senhaNova = senhaNova;
		this.confirmacao = confirmacao;
	}

	public String getSenhaAntiga() {
		return senhaAntiga;
	}

	public void setSenhaAntiga(String senhaAntiga) {
		this.senhaAntiga = senhaAntiga;
	}

	public String getSenhaNova() {
		return senhaNova;
	}

	public void setSenhaNova(String senhaNova) {
		this.senhaNova = senhaNova;
	}

	public String getConfirmacao() {
		return confirmacao;
	}

	public void setConfirmacao(String confirmacao) {
		this.confirmacao = confirmacao;
	}
}
