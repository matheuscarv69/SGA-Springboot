package io.github.matheuscarv69;

import io.github.matheuscarv69.domain.entity.Usuario;
import io.github.matheuscarv69.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class SgaApplication {

    @Bean
    public CommandLineRunner init(@Autowired UsuarioRepository usuarioRepository){
        return args -> {
            System.out.println("Salvando usuário");
            usuarioRepository.save(new Usuario(
                    null,
                    "201803031905",
                    "Matheus",
                    "matheus9126@gmail.com",
                    "95981187680",
                    "MatheusCarv69",
                    "1111",false,
                    false));

            System.out.println("Obter Clientes");
            List<Usuario> todosUsuarios = usuarioRepository
                    .findAll();

            todosUsuarios.forEach(System.out::println);
        };
    }


    public static void main(String[] args) {

        SpringApplication.run(SgaApplication.class, args);
    }
}
