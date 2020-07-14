package io.github.matheuscarv69.domain.repository;

import io.github.matheuscarv69.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {


}
