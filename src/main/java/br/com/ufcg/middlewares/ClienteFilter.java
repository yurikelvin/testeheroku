package br.com.ufcg.middlewares;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

import br.com.ufcg.services.UsuarioService;
import io.jsonwebtoken.SignatureException;

public class ClienteFilter extends GenericFilterBean {

    private static final String SOMENTE_CLIENTES = "Desculpe, somente clientes podem acessar esse recurso.";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        ((HttpServletResponse) response).setHeader("Content-Type", "application/json");

        String userType = (String) req.getAttribute("userType");

        if(userType.equals("Fornecedor")) {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, SOMENTE_CLIENTES);
            return;
        }

        chain.doFilter(request, response);
    }

}
