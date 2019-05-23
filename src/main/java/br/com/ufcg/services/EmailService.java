package br.com.ufcg.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.com.ufcg.domain.Usuario;

@Service
public class EmailService {

	@Autowired 
	private JavaMailSender mailSender;

  
    public String recuperarSenha(Usuario usuario, String novaSenha) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Recuperar senha da sua conta Trampo!");
        message.setText("Olá, " + usuario.getNomeCompleto() + "! " +
        		"Uma recuperação de senha foi solicitada para sua conta no Trampo." +
        		"Segue os novos dados: " +
        		System.lineSeparator() +
        		"Login: " + usuario.getLogin() + System.lineSeparator() +
        		"Nova senha: " + novaSenha + System.lineSeparator() +
        		"A equipe Trampo agradece! Até mais!");
        message.setTo(usuario.getEmail().toLowerCase());
        message.setFrom("projeto.trampo2019@gmail.com");

        
      
        try {
            mailSender.send(message);
            return "Email enviado com sucesso!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}