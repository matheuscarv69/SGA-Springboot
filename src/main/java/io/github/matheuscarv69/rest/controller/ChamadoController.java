package io.github.matheuscarv69.rest.controller;

import io.github.matheuscarv69.domain.entity.Chamado;
import io.github.matheuscarv69.domain.enums.StatusChamado;
import io.github.matheuscarv69.exception.ChamadoNaoEncontradoException;
import io.github.matheuscarv69.rest.dto.*;
import io.github.matheuscarv69.service.ChamadoService;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    public Integer save(@RequestBody ChamadoDTO dto) {
        Chamado chamado = service.salvar(dto);

        return chamado.getId();
    }

    @PatchMapping("{id}")
//patchmapping só atualiza campos especificos do objeto, diferentemente do putmapping, onde todos os dados são atualizados
    @ResponseStatus(NO_CONTENT)
    public void updateStatus(@PathVariable Integer id, @RequestBody AtualizacaoStatusChamadoDTO dto) {

        String novoStatus = dto.getNovoStatus();

        service.atualizaStatus(id, StatusChamado.valueOf(novoStatus));

    }

    @DeleteMapping("{id}") // deleta um chamado pelo id
    @ResponseStatus(NO_CONTENT)
    public void arquivarChamado(@PathVariable Integer id) {
        service.arquivarChamado(id);
    }

    @PatchMapping("/desArqChamado/{id}")
    @ResponseStatus(NO_CONTENT)
    public void desarquivarChamado(@PathVariable Integer id) {
        service.desarquivarChamado(id);
    }

    @GetMapping("{id}") // busca um chamado por id
    public InformacoesChamadoDTO getById(@PathVariable Integer id) {
        return service
                .buscarChamadoPorId(id)
                .map(chamado -> converter(chamado))
                .orElseThrow(() ->
                        new ChamadoNaoEncontradoException());

    }

    @GetMapping // busca por parametro e todos
    public List<InformacoesChamadoDTO> find(Chamado filtro, FiltroChamadoDTO filtroDTO) {
        List<Chamado> list = service.buscarPorPar(filtro, filtroDTO);

        List<InformacoesChamadoDTO> list2 = new ArrayList<>();

        for (Chamado c : list) {
            list2.add(converter(c));
        }

        return list2;
    }

    @PatchMapping("/atribTecn/{id}")
    @ResponseStatus(OK)
    public void atribTecn(@PathVariable Integer id, @RequestBody TecnicoDTO tecnicoDTO) {
        service.atribuirTecn(id, tecnicoDTO);
    }

    public final static InformacoesChamadoDTO converter(Chamado chamado) {

        String dataSolucao = new String();
        String nomeTecn = new String();
        String matriculaTecn = new String();

        if (chamado.getDataFinal() == null) {
            dataSolucao = "Chamado ainda não foi solucionado.";
        } else {
            dataSolucao = chamado.getDataFinal().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        if (chamado.getTecnico() == null) {
            nomeTecn = "Técnico não atribuído ao chamado.";
            matriculaTecn = "";
        } else {
            nomeTecn = chamado.getTecnico().getNome();
            matriculaTecn = chamado.getTecnico().getMatricula();
        }

        return InformacoesChamadoDTO.builder()
                .id(chamado.getId())
                .requerente(chamado.getRequerente().getNome())
                .matricula(chamado.getRequerente().getMatricula())
                .titulo(chamado.getTitulo())
                .descricao(chamado.getDescricao())
                .tipo(chamado.getTipo().toString())
                .bloco(chamado.getBloco())
                .sala(chamado.getSala())
                .status(chamado.getStatusChamado().name())
                .dataInicio(chamado.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyy")))
                .dataSolucao(dataSolucao)
                .tecnico(nomeTecn)
                .matriculaTecn(matriculaTecn)
                .ativo(chamado.isAtivo())
                .build();
    }

}
