package io.github.matheuscarv69.service;

import io.github.matheuscarv69.domain.entity.Chamado;
import io.github.matheuscarv69.domain.enums.StatusChamado;
import io.github.matheuscarv69.rest.dto.ChamadoDTO;
import io.github.matheuscarv69.rest.dto.FiltroChamadoDTO;
import io.github.matheuscarv69.rest.dto.TecnicoDTO;

import java.util.List;
import java.util.Optional;

public interface ChamadoService {

    Chamado salvar(ChamadoDTO dto);

    Optional<Chamado> buscarChamadoPorId(Integer id);

    //List<Chamado> buscarPorPar(Chamado filtro);

    List<Chamado> buscarPorPar(Chamado filtro1, FiltroChamadoDTO filtroDTO);

    void atualizaStatus(Integer id, StatusChamado statusChamado);

    void atribuirTecn(Integer id, TecnicoDTO tecnicoDTO);

    void removerTecn(Integer id);

    void arquivarChamado(Integer id);

    void desarquivarChamado(Integer id);

}
