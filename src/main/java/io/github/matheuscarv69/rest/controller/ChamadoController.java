package io.github.matheuscarv69.rest.controller;

import io.github.matheuscarv69.domain.entity.Chamado;
import io.github.matheuscarv69.domain.enums.StatusChamado;
import io.github.matheuscarv69.exception.ChamadoNaoEncontradoException;
import io.github.matheuscarv69.rest.dto.*;
import io.github.matheuscarv69.service.ChamadoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    @ApiOperation("Salvar um chamado")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Chamado cadastrado com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 403, message = "Acesso negado")
    })
    public Integer save(@RequestBody @ApiParam("Dados do Chamado") @Valid ChamadoDTO dto) {
        Chamado chamado = service.salvar(dto);
        return chamado.getId();
    }

    @PatchMapping("/updt/{id}")//patchmapping só atualiza campos especificos do objeto, diferentemente do putmapping, onde todos os dados são atualizados
    @ApiOperation("Atualizar status do Chamado")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Status do Chamado atualizado com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 401, message = "Usuário não autorizado"),
            @ApiResponse(code = 403, message = "Acesso negado"),
            @ApiResponse(code = 404, message = "Chamado não encontrado")
    })
    @ResponseStatus(NO_CONTENT)
    public void updateStatus(@PathVariable @ApiParam("Id do Chamado") Integer id, @RequestBody @ApiParam("Status e Solução do chamado") @Valid AtualizacaoStatusChamadoDTO dto) {
        String novoStatus = dto.getNovoStatus();
        String solucaoDTO = dto.getSolucao();

        service.atualizaStatus(id, StatusChamado.valueOf(novoStatus), solucaoDTO);
    }

    @DeleteMapping("/arqCham/{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Arquivar um Chamado")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Chamado arquivado com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 401, message = "Usuário não autorizado"),
            @ApiResponse(code = 403, message = "Acesso negado"),
            @ApiResponse(code = 404, message = "Chamado não encontrado")

    })
    public void arquivarChamado(@PathVariable @ApiParam("Id do Chamado") Integer id) {
        service.arquivarChamado(id);
    }

    @PatchMapping("/desarqCham/{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Desarquivar um Chamado")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Chamado desarquivado com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 401, message = "Usuário não autorizado"),
            @ApiResponse(code = 403, message = "Acesso negado"),
            @ApiResponse(code = 404, message = "Chamado não encontrado")
    })
    public void desarquivarChamado(@PathVariable @ApiParam("Id do Chamado") Integer id) {
        service.desarquivarChamado(id);
    }

    @GetMapping("/getId/{id}")
    @ApiOperation("Buscar um Chamado pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Chamado encontrado com sucesso"),
            @ApiResponse(code = 401, message = "Usuário não autorizado"),
            @ApiResponse(code = 403, message = "Acesso negado"),
            @ApiResponse(code = 404, message = "Chamado não encontrado")
    })
    public InformacoesChamadoDTO getById(@PathVariable @ApiParam("Id do Chamado") Integer id) {
        return service
                .buscarChamadoPorId(id)
                .map(chamado -> converterChamadoParaInformacao(chamado))
                .orElseThrow(() ->
                        new ChamadoNaoEncontradoException());
    }

    @GetMapping // busca por parametro e todos
    @ApiOperation("Buscar Usuários por Parâmetro e Buscar todos os chamados")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Usuário não autorizado"),
            @ApiResponse(code = 403, message = "Acesso negado")
    })
    public List<InformacoesChamadoDTO> buscarPorPar(Chamado filtro, FiltroChamadoDTO filtroDTO) {
        return service.buscarPorPar(filtro, filtroDTO);
    }

    @PatchMapping("/atribTecn/{id}")
    @ResponseStatus(OK)
    @ApiOperation("Atribuir um Técnico à um Chamado")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Técnico atribuído com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 401, message = "Usuário não autorizado"),
            @ApiResponse(code = 403, message = "Acesso negado"),
            @ApiResponse(code = 404, message = "Dados não encontrados")
    })
    public void atribTecn(@PathVariable @ApiParam("Id do Chamado") Integer id, @RequestBody @ApiParam("Id do Técnico") TecnicoDTO tecnicoDTO) {
        service.atribuirTecn(id, tecnicoDTO);
    }

    @PatchMapping("/removerTecn/{id}")
    @ResponseStatus(OK)
    @ApiOperation("Remover um Técnico de um Chamado")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Técnico removido do Chamado com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 401, message = "Usuário não autorizado"),
            @ApiResponse(code = 403, message = "Acesso negado"),
            @ApiResponse(code = 404, message = "Chamado não encontrados")
    })
    public void removerTecn(@PathVariable @ApiParam("Id do Chamado") Integer id) {
        service.removerTecn(id);
    }

}
