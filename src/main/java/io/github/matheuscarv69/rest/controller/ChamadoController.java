package io.github.matheuscarv69.rest.controller;

import io.github.matheuscarv69.domain.entity.Chamado;
import io.github.matheuscarv69.domain.enums.StatusChamado;
import io.github.matheuscarv69.exception.ChamadoNaoEncontradoException;
import io.github.matheuscarv69.rest.dto.*;
import io.github.matheuscarv69.service.ChamadoService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static io.github.matheuscarv69.service.impl.ChamadoServiceImpl.converterChamadoParaInformacao;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("api/chamados")
public class ChamadoController {

    private ChamadoService service;

    public ChamadoController(ChamadoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Integer save(@RequestBody @Valid ChamadoDTO dto) {
        Chamado chamado = service.salvar(dto);
        return chamado.getId();
    }

    @PatchMapping("/updt/{id}")
//patchmapping só atualiza campos especificos do objeto, diferentemente do putmapping, onde todos os dados são atualizados
    @ResponseStatus(NO_CONTENT)
    public void updateStatus(@PathVariable Integer id, @RequestBody @Valid AtualizacaoStatusChamadoDTO dto) {
        String novoStatus = dto.getNovoStatus();
        String solucaoDTO = dto.getSolucao();

        service.atualizaStatus(id, StatusChamado.valueOf(novoStatus), solucaoDTO);
    }

    @DeleteMapping("/arqCham/{id}") // deleta um chamado pelo id
    @ResponseStatus(NO_CONTENT)
    public void arquivarChamado(@PathVariable Integer id) {
        service.arquivarChamado(id);
    }

    @PatchMapping("/desarqCham/{id}")
    @ResponseStatus(NO_CONTENT)
    public void desarquivarChamado(@PathVariable Integer id) {
        service.desarquivarChamado(id);
    }

    @GetMapping("/getId/{id}") // busca um chamado por id
    public InformacoesChamadoDTO getById(@PathVariable Integer id) {
        return service
                .buscarChamadoPorId(id)
                .map(chamado -> converterChamadoParaInformacao(chamado))
                .orElseThrow(() ->
                        new ChamadoNaoEncontradoException());
    }

    @GetMapping // busca por parametro e todos
    public List<InformacoesChamadoDTO> find(Chamado filtro, FiltroChamadoDTO filtroDTO) {
        return service.buscarPorPar(filtro, filtroDTO);
    }

    @PatchMapping("/atribTecn/{id}")
    @ResponseStatus(OK)
    public void atribTecn(@PathVariable Integer id, @RequestBody TecnicoDTO tecnicoDTO) {
        service.atribuirTecn(id, tecnicoDTO);
    }

    @PatchMapping("/removerTecn/{id}")
    @ResponseStatus(OK)
    public void removerTecn(@PathVariable Integer id) {
        service.removerTecn(id);
    }

}
