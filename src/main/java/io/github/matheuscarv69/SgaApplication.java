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
    public CommandLineRunner init(@Autowired UsuarioRepository usuarioRepository,
                                  @Autowired ChamadoRepository chamadoRepository) {
        return args -> {

            System.out.println("Salvando usuário");
            Usuario user = new Usuario("201803031905", "Matheus", false, true);
            Usuario user2 = new Usuario("123456789101", "Teuzin", false, true);

            Usuario user3 = new Usuario("123456789101", "Fulano", false, false);
            Usuario user4 = new Usuario("123456789101", "Pedrin", false, false);

            Usuario user5 = new Usuario("201803031905", "Cicrano", true, false);
            Usuario user6 = new Usuario("201803031905", "Beltrano", true, false);

            Usuario user7 = new Usuario("201803031905", "Putrano", false, false);
            Usuario user8 = new Usuario("201803031905", "Sem nome", false, false);

            usuarioRepository.save(user);
            usuarioRepository.save(user2);
            usuarioRepository.save(user3);
            usuarioRepository.save(user4);
            usuarioRepository.save(user5);
            usuarioRepository.save(user6);
            usuarioRepository.save(user7);
            usuarioRepository.save(user8);

            System.out.println("Técnicos");
            List<Usuario> tecs = usuarioRepository.findTecnicos();
            tecs.forEach(System.out::println);

            System.out.println("Administradores");
            List<Usuario> adms = usuarioRepository.findAdministradores();
            adms.forEach(System.out::println);

            System.out.println("Usuarios");
            List<Usuario> users = usuarioRepository.findUsuarios();
            users.forEach(System.out::println);


//            System.out.println("Buscando um usuario pelo nome:");
//            List<Usuario> u = usuarioRepository.findByNomeLike("Matheus");
//            u.forEach(System.out::println);
//
//            System.out.println("Buscando um usuario pela matricula:");
//            Usuario u2 = usuarioRepository.findByMatricula("123456789101");
//            System.out.println(u2);


//            Chamado c = new Chamado();
//            c.setRequerente(user);
//            c.setDataInicio(LocalDate.now());
//            c.setBloco('F');
//            c.setSala(15);
//            c.setTecnico(user2);
//
//            Chamado d = new Chamado();
//            d.setRequerente(user);
//            d.setDataInicio(LocalDate.now());
//            d.setBloco('G');
//            d.setSala(10);
//            d.setTecnico(user);
//
//            Chamado e = new Chamado();
//            e.setRequerente(user);
//            e.setDataInicio(LocalDate.now());
//            e.setBloco('G');
//            e.setSala(12);
//            e.setTecnico(user2);
//
//            chamadoRepository.save(c);
//            chamadoRepository.save(d);
//            chamadoRepository.save(e);
//
//            System.out.println("Imprimindo");
//            System.out.println(user);
//            System.out.println(c);
//            System.out.println(d);
//            System.out.println(e);
//
//            System.out.println("Buscando usuario com chamados");
//            Usuario user3 = usuarioRepository.findUsuarioFetchChamadosReq(user2.getId());
//            System.out.println(user3);
//            System.out.println("Chamados do usuario");
//            System.out.println(user3.getChamadosReq());
//
//            System.out.println("Buscando tecnico com chamados");
//            Usuario user4 = usuarioRepository.findUsuarioFetchChamadosTec(user.getId());
//            System.out.println(user4);
//            System.out.println("Chamados do Tecnico");
//            System.out.println(user4.getChamadosTec());


        };
    }


    public static void main(String[] args) {

        SpringApplication.run(SgaApplication.class, args);
    }
}
