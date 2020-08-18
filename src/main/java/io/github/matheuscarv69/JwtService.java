package io.github.matheuscarv69;

import io.github.matheuscarv69.domain.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.expiracao}")
    private String expiracao;
    @Value("${security.jwt.chave-assinatura}")
    private String chaveAssinatura;

    public String gerarToken(Usuario usuario) {
        long expString = Long.valueOf(expiracao);
        LocalDateTime dataHoraExpiracao = LocalDateTime
                .now()
                .plusMinutes(expString);

        Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
        Date dataExpiracao = Date.from(instant);

        return Jwts
                .builder()
                .setSubject(usuario.getLogin())
                .setExpiration(dataExpiracao)
                .signWith(SignatureAlgorithm.HS512, chaveAssinatura)
                .compact();
    }

    private Claims obterClaims(String token) throws ExpiredJwtException{
        return Jwts
                .parser()
                .setSigningKey(chaveAssinatura)
                .parseClaimsJws(token)
                .getBody(); // retorna as informações do token
    }

    public boolean tokenValido(String token){

        try {
            Claims claims = obterClaims(token);
            Date dataExpiracao = claims.getExpiration();
            LocalDateTime dataHoraExpiracao = dataExpiracao
                    .toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();

            return !LocalDateTime.now().isAfter(dataHoraExpiracao);
            // se retornar true significa que o token é inválido, caso contrário ele ainda é válido
        }catch (Exception e){
            return false;
        }
    }

    public String obterLoginUsuario(String token) throws ExpiredJwtException{
        return (String) obterClaims(token).getSubject();
    }

    

}
