package br.com.ufcg;

import br.com.ufcg.middlewares.ClienteFilter;
import br.com.ufcg.middlewares.FornecedorFilter;
import br.com.ufcg.middlewares.jwt.TokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;

@SpringBootApplication
public class ProjectEsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectEsApplication.class, args);
	}


	@Bean
	public FilterRegistrationBean filtroJwt() {
		ArrayList<String> urlPatterns = new ArrayList<>();
		urlPatterns.add("/api/servicos/*");
		urlPatterns.add("/api/usuarios/*");
		urlPatterns.add("/api/fornecedor/servicos/*");
		urlPatterns.add("/api/cliente/servicos/*");
		FilterRegistrationBean frb = new FilterRegistrationBean();
		frb.setFilter(new TokenFilter());
		frb.setUrlPatterns(urlPatterns);
		return frb;
	}
	
	
	@Bean
	public FilterRegistrationBean filtroCliente() {
		ArrayList<String> urlPatterns = new ArrayList<>();
		urlPatterns.add("/api/servicos/cliente/*");
		FilterRegistrationBean frb = new FilterRegistrationBean();
		frb.setFilter(new ClienteFilter());
		frb.setUrlPatterns(urlPatterns);
		return frb;
	}

	@Bean
	public FilterRegistrationBean filtroFornecedor() {
		ArrayList<String> urlPatterns = new ArrayList<>();
		urlPatterns.add("/api/servicos/fornecedor/*");
		urlPatterns.add("/api/fornecedor/servicos/*");
		FilterRegistrationBean frb = new FilterRegistrationBean();
		frb.setFilter(new FornecedorFilter());
		frb.setUrlPatterns(urlPatterns);
		return frb;
	}
	
	@Bean
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}
}
