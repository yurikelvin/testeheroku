package br.com.ufcg.util;

public class UtilCampos {

    private static final String CAMPO_MIN = "A quantidade mínima de caracteres pro campo ";
    private static final String CAMPO_MAX = "A quantidade máxima de caracteres pro campo ";

    public static void validaTamanhoCampo(String campo, int valorMin, int valorMax, String nomeCampo) throws Exception {
        if(campo.length() < valorMin) {
            throw new Exception(CAMPO_MIN + "'" + nomeCampo + "'" + " é de " + valorMin + " caracteres.");
        }

        if(campo.length() > valorMax) {
            throw new Exception(CAMPO_MAX + "'" + nomeCampo + "'" + " é de " + valorMax + " caracteres.");
        }
    }
}
