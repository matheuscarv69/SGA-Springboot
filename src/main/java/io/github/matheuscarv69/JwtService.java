package io.github.matheuscarv69;

import io.github.matheuscarv69.domain.entity.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
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

}
