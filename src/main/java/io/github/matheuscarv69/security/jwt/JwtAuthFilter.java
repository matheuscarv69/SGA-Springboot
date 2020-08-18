package io.github.matheuscarv69.security.jwt;

import io.github.matheuscarv69.service.impl.UsuarioLoginServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private UsuarioLoginServiceImpl usuarioLoginService;

    public JwtAuthFilter(JwtService jwtService, UsuarioLoginServiceImpl usuarioLoginService) {
        this.jwtService = jwtService;
        this.usuarioLoginService = usuarioLoginService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain) throws ServletException, IOException {

        String authorization = httpServletRequest.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer")) {
            String token = authorization.split(" ")[1];
            boolean isValid = jwtService.tokenValido(token);

            if (isValid) {
                String loginUsuario = jwtService.obterLoginUsuario(token);
                UserDetails usuario = usuarioLoginService.loadUserByUsername(loginUsuario);
                //colocamos o usuario no contexto do spring security
                UsernamePasswordAuthenticationToken user = new
                        UsernamePasswordAuthenticationToken(usuario, null,
                        usuario.getAuthorities());
                // Dizemos para o contexto do spring security que se trata de uma autenticacao de uma aplicacao web
                user.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                // Esse é o contexto do spring security
                SecurityContextHolder.getContext().setAuthentication(user);

            }


        }

        // Despachamos a requisicao
        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }

}
