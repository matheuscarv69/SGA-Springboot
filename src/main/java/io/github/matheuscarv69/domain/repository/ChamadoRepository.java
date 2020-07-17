package io.github.matheuscarv69.domain.repository;

import io.github.matheuscarv69.domain.entity.Chamado;
import io.github.matheuscarv69.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChamadoRepository extends JpaRepository<Chamado, Integer> {

    List<Chamado> findByRequerente(Usuario usuario); //retorna os chamados feito pelo requerente

    List<Chamado> findByTecnico(Usuario usuario); // retorna os chamados de um tecnico

}
