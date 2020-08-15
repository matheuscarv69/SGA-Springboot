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
    public PasswordEncoder passwordEncoder(){
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
                    // save ok
                    .antMatchers(HttpMethod.POST, "/api/usuarios")
                .permitAll()
                    // update ok
                    .antMatchers(HttpMethod.PUT,"/api/usuarios/**")
                .hasRole("USER")
                    // desliga user
                    .antMatchers(HttpMethod.DELETE, "/api/usuarios/deslUser/**")
                .hasRole("ADMIN")
                    // ativa user
                    .antMatchers(HttpMethod.PATCH, "/api/usuarios/ativUser/**")
                .hasRole("ADMIN")
                    // busca por id
                    .antMatchers(HttpMethod.GET,"/api/usuarios/**")
                // mudar isso depois para somente tecnico e admin.hasAnyRole("USER","TECN", "ADMIN")
                .permitAll()

//                    // busca tecnicos
//                    .antMatchers(HttpMethod.GET, "/api/usuarios/tecn")
//                .hasAnyRole("TECN", "ADMIN")
//                    // busca administradores
//                    .antMatchers(HttpMethod.GET, "/api/usuarios/admin")
//                .hasAnyRole("TECN","ADMIN")
//                    // set tecnico
//                    .antMatchers(HttpMethod.PUT, "/api/usuarios/admin/setTecn/**")
//                .hasRole("ADMIN")
//                    // set administradores
//                    .antMatchers(HttpMethod.PUT, "/api/usuarios/admin/setAdmin/**")
//                .hasRole("ADMIN")
//                    // busca chamados req
//                    .antMatchers(HttpMethod.GET, "/api/usuarios/chamadosReq/**")
//                .authenticated()
//                    // busca chamados tecn
//                    .antMatchers(HttpMethod.GET, "/api/usuarios/chamadosTecn/**")
//                .hasAnyRole("TECN", "ADMIN")
                    // busca por parametros
//                    .antMatchers(HttpMethod.GET, "/api/usuarios")
//                .permitAll()
                    .antMatchers("/api/chamados/**")
                .hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                    .httpBasic();
//.authenticated()
    }
}
