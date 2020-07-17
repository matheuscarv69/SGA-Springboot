package io.github.matheuscarv69;

import io.github.matheuscarv69.domain.entity.Chamado;
import io.github.matheuscarv69.domain.entity.Usuario;
import io.github.matheuscarv69.domain.repository.ChamadoRepository;
import io.github.matheuscarv69.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class SgaApplication {

    @Bean
    public CommandLineRunner commandLineRunner(@Autowired UsuarioRepository repository, @Autowired ChamadoRepository chamadoRepository){
        return args -> {
            Usuario user = new Usuario("Arthur");
            Usuario tecn = new Usuario("Matheus");

            tecn.setTecn(true);

            repository.save(user);
            repository.save(tecn);

            Chamado c = new Chamado();
            c.setRequerente(user);
            c.setTecnico(tecn);
            c.setTitulo("quebrado");

            Chamado d = new Chamado();
            d.setRequerente(user);
            d.setTecnico(tecn);
            d.setTitulo("pao");

            Chamado e = new Chamado();
            e.setRequerente(tecn);
            e.setTitulo("Quadro");


            chamadoRepository.save(c);
            chamadoRepository.save(d);
            chamadoRepository.save(e);

        };

    }

    public static void main(String[] args) {

        SpringApplication.run(SgaApplication.class, args);
    }
}
