package io.github.matheuscarv69.service;

import io.github.matheuscarv69.domain.entity.Chamado;
import io.github.matheuscarv69.domain.enums.StatusChamado;
import io.github.matheuscarv69.rest.dto.ChamadoDTO;

import java.util.Optional;

public interface ChamadoService {

    Chamado salvar(ChamadoDTO dto);

    Optional<Chamado> buscarChamadoPorId(Integer id);

    void atualizaStatus(Integer id, StatusChamado statusChamado);

}
