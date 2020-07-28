package io.github.matheuscarv69.domain.repository;

import io.github.matheuscarv69.domain.entity.Chamado;
import io.github.matheuscarv69.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    List<Usuario> findByNomeLike(String nome); //busca um usuario com o nome informado

    Usuario findByMatricula(String nome); //busca um usuario pela matricula

    @Query("select u from Usuario u left join fetch u.chamadosReq where u.id = :id")
    //@Query(value = "SELECT * FROM CHAMADO AS C WHERE C.REQUERENTE_ID = :id", nativeQuery = true)
    Usuario findUsuarioFetchChamadosReq(@Param("id") Integer id);

    @Query("select u from Usuario u left join fetch u.chamadosTecn where u.id = :id and u.tecn = true")
    Usuario findUsuarioFetchChamadosTecn(@Param("id") Integer id);

    @Query(value = "SELECT * FROM USUARIO WHERE TECN = TRUE", nativeQuery = true) //busca tecnicos
    List<Usuario> findTecnicos();

    @Query(value = "SELECT * FROM USUARIO WHERE ADMIN = TRUE", nativeQuery = true) //busca administradores
    List<Usuario> findAdministradores();

    @Query(value = "SELECT * FROM USUARIO WHERE ADMIN = FALSE AND TECN = FALSE", nativeQuery = true) //busca usuarios normais
    List<Usuario> findUsuarios();
}
