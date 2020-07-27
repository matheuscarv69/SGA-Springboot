package io.github.matheuscarv69.service;

import io.github.matheuscarv69.domain.entity.Chamado;
import io.github.matheuscarv69.domain.enums.StatusChamado;
import io.github.matheuscarv69.rest.dto.ChamadoDTO;
import io.github.matheuscarv69.rest.dto.InformacoesChamadoDTO;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

public interface ChamadoService {

    Chamado salvar(ChamadoDTO dto);

    Optional<Chamado> buscarChamadoPorId(Integer id);

    List<Chamado> buscarTodos();

    List<Chamado> buscarPorPar(Chamado filtro);

    void atualizaStatus(Integer id, StatusChamado statusChamado);

    void excluir(Integer id);

}
