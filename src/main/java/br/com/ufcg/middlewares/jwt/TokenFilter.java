package br.com.ufcg.middlewares.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.ufcg.domain.Usuario;
import br.com.ufcg.domain.enums.TipoUsuario;
import br.com.ufcg.services.UsuarioService;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

public class TokenFilter extends GenericFilterBean {

    private UsuarioService usuarioService;
    
    // CONSTANTES NECESSÁRIAS AO CONTROLLER
    public static final String SECRET = "ProjetoES";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (usuarioService==null){
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            usuarioService = webApplicationContext.getBean(UsuarioService.class);
        }

        HttpServletRequest req = (HttpServletRequest) request;

        ((HttpServletResponse) response).setHeader("Content-Type", "application/json");

        String header = req.getHeader("Authorization");
        try {
            if (header == null || !header.startsWith("Bearer ")) {
                throw new ServletException("Token inválido");
            }
        } catch(ServletException e) {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }


        String token = header.substring(7);
        String tokenBody;

        // Verificar se o token é válido
        try {
            tokenBody = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();

            //validaToken(tokenBody, req.getRequestURI());

        } catch(SignatureException e) {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido.");
            return;
        }

        try {
            String[] usuario = tokenBody.split(" ");
            String login = usuario[0];
            Usuario usuarioLogado = usuarioService.getByLogin(login);

            TipoUsuario tipoUsuario = usuarioLogado.getTipo();

            request.setAttribute("user", usuarioLogado);

            if(tipoUsuario.equals(TipoUsuario.CLIENTE)) {
                request.setAttribute("userType", "Cliente");
            } else {
                request.setAttribute("userType", "Fornecedor");
            }
        }catch(Exception e) {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            return;
        }

        chain.doFilter(request, response);
    }

    private void validaToken(String tokenBody, String requestURI) throws ServletException{
        String[] usuario = tokenBody.split(" ");
        String login = usuario[0];
        String password = usuario[1];

        if (!usuarioService.checkUser(login, password)) {
            throw new ServletException("Token com acesso bloqueado");
        }

    }
}