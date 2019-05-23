package br.com.ufcg.domain.enums;

public enum TipoStatus {
	
    AGUARDANDO_OFERTAS("Aguardando Ofertas"),
    ACEITO("Aceito"),
    CANCELADO("Cancelado"),
    CONCLUIDO("Concluído");

    private final String tipo;

    TipoStatus(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return this.tipo;
    }
}
