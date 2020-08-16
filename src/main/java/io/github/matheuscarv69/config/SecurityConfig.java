package io.github.matheuscarv69.config;

import io.github.matheuscarv69.service.impl.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Bean // Faz a criptografia
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override // Fará a autenticação dos usuários e aqui será dito de onde virão os user
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(usuarioService)
                .passwordEncoder(passwordEncoder());
    }

    @Override // Fará a verificação das autorizações para acessar as url
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                // USUARIO
                // save ok
                    .antMatchers(HttpMethod.POST, "/api/usuarios")
                .permitAll()
                // update ok
                    .antMatchers(HttpMethod.PUT, "/api/usuarios/updt/**")
                .hasRole("USER")
                // desliga user ok
                    .antMatchers(HttpMethod.DELETE, "/api/usuarios/deslUser/**")
                .hasRole("ADMIN")
                // ativa user ok
                    .antMatchers(HttpMethod.PATCH, "/api/usuarios/ativUser/**")
                .hasRole("ADMIN")
                // busca por id ok
                    .antMatchers(HttpMethod.GET, "/api/usuarios/getId/**")
                .hasAnyRole("TECN", "ADMIN")
                // busca por parametros ok
                    .antMatchers(HttpMethod.GET, "/api/usuarios")
                .hasAnyRole("TECN", "ADMIN")
                // busca tecnicos ok
                    .antMatchers(HttpMethod.GET, "/api/usuarios/tecn")
                .hasAnyRole("ADMIN")
                // busca administradores ok
                    .antMatchers(HttpMethod.GET, "/api/usuarios/admin")
                .hasAnyRole("ADMIN")
                // set tecnico ok
                    .antMatchers(HttpMethod.PUT, "/api/usuarios/admin/setTecn/**")
                .hasRole("ADMIN")
                // set administradores ok
                    .antMatchers(HttpMethod.PUT, "/api/usuarios/admin/setAdmin/**")
                .hasRole("ADMIN")
                // busca chamados req ok
                    .antMatchers(HttpMethod.GET, "/api/usuarios/chamadosReq/**")
                .authenticated()
                // busca chamados tecn TESTAR
                    .antMatchers(HttpMethod.GET, "/api/usuarios/chamadosTecn/**")
                .hasAnyRole("TECN", "ADMIN")
                // CHAMADO
                // Save Chamado
                    .antMatchers(HttpMethod.POST, "/api/chamados")
                .authenticated()
                // Update
                    .antMatchers(HttpMethod.PATCH, "/api/chamados/updt/**")
                .hasAnyRole("TECN", "ADMIN")
                // Arquivar chamado
                    .antMatchers(HttpMethod.DELETE, "/api/chamados/arqCham/**")
                .hasRole("ADMIN")
                // Desarquivar chamado
                    .antMatchers(HttpMethod.PATCH, "/api/chamados/desarqCham/**")
                .hasRole("ADMIN")
                // Buscar por id
                    .antMatchers(HttpMethod.GET, "/api/chamados/getId/**")
                .hasAnyRole("TECN", "ADMIN")
                // Busca por parametro
                    .antMatchers(HttpMethod.GET, "/api/chamados")
                .authenticated()
                // Atribuir tecnico ao chamado
                    .antMatchers(HttpMethod.PATCH, "/api/chamados/atribTecn/**")
                .hasAnyRole("TECN", "ADMIN")
                .anyRequest().authenticated()
                .and()
                .httpBasic();

    }
}
