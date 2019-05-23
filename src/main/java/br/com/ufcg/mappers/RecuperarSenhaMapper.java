package br.com.ufcg.mappers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class RecuperarSenhaMapper {

    private String email;

    public RecuperarSenhaMapper() { super(); }

    public RecuperarSenhaMapper(String email) { this.email = email; }

    public String getEmail() { return this.email; }

}