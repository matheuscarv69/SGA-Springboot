package io.github.matheuscarv69.service.impl;

import io.github.matheuscarv69.domain.entity.Chamado;
import io.github.matheuscarv69.domain.entity.Usuario;
import io.github.matheuscarv69.domain.enums.StatusChamado;
import io.github.matheuscarv69.domain.enums.TipoChamado;
import io.github.matheuscarv69.domain.repository.ChamadoRepository;
import io.github.matheuscarv69.domain.repository.UsuarioRepository;
import io.github.matheuscarv69.exception.ChamadoNaoEncontradoException;
import io.github.matheuscarv69.exception.RegraNegocioException;
import io.github.matheuscarv69.exception.UsuarioNaoEncontradoException;
import io.github.matheuscarv69.rest.dto.ChamadoDTO;
import io.github.matheuscarv69.rest.dto.FiltroChamadoDTO;
import io.github.matheuscarv69.rest.dto.InformacoesChamadoDTO;
import io.github.matheuscarv69.rest.dto.TecnicoDTO;
import io.github.matheuscarv69.service.ChamadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChamadoServiceImpl implements ChamadoService {

    private final ChamadoRepository repository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional // garante que tudo seja salvo com sucesso, caso aconteça algum problema ele não irá salvar
    public Chamado salvar(ChamadoDTO dto) {
        Integer idRequerente = dto.getRequerente();
        Usuario usuario = usuarioRepository
                .findById(idRequerente)
                .map(user -> {
                    if (!user.isAtivo()) {
                        throw new RegraNegocioException("O usuário não está ativo");
                    }
                    return user;
                }).orElseThrow(() ->
                        new RegraNegocioException("Código de usuário inválido."));

        Chamado chamado = new Chamado();
        chamado.setRequerente(usuario);
        chamado.setTitulo(dto.getTitulo());
        chamado.setDescricao(dto.getDescricao());
        chamado.setTipoChamado(TipoChamado.valueOf(dto.getTipo()));
        chamado.setBloco(dto.getBloco());
        chamado.setSala(dto.getSala());
        chamado.setDataInicio(LocalDate.now());
        chamado.setStatusChamado(StatusChamado.PENDENTE);

        repository.save(chamado);

        return chamado;
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusChamado statusChamado, String solucaoDTO) {
        if(statusChamado == StatusChamado.SOLUCIONADO && solucaoDTO.isEmpty()){
            throw new RegraNegocioException("Informe a solução.");
        }

        if(statusChamado != StatusChamado.SOLUCIONADO){
            if(!solucaoDTO.isEmpty()){
                throw new RegraNegocioException("O status do chamado é : " + statusChamado + ", dessa forma não pode receber uma solução.");
            }
        }

        repository
                .findById(id)
                .map(c -> {
                    if (!c.isAtivo()) {
                        throw new RegraNegocioException("O chamado não está ativo");
                    }

                    if (c.getTecnico() == null) {
                        throw new RegraNegocioException("Nenhum técnico está atribuído");
                    }

                    if(c.getStatusChamado() == statusChamado.SOLUCIONADO && !c.getSolucao().isEmpty()){
                        throw new RegraNegocioException("O chamado já foi solucionado e finalizado, não é possível alterar o status");
                    }else{
                        c.setStatusChamado(statusChamado);
                    }

                    if (statusChamado == StatusChamado.SOLUCIONADO) {
                        c.setDataFinal(LocalDate.now());
                        c.setSolucao(solucaoDTO);
                    }

                    repository.save(c);
                    return c;
                }).orElseThrow(() -> new ChamadoNaoEncontradoException());
    }

    @Override
    @Transactional
    public void arquivarChamado(Integer id) {
        repository.findById(id)
                .map(c -> {
                    if (!c.isAtivo()) {
                        throw new RegraNegocioException("O chamado já está inativo");
                    }

                    c.setAtivo(false);
                    repository.save(c);

                    return c;
                }).orElseThrow(() ->
                new ChamadoNaoEncontradoException());
    }

    @Override
    @Transactional
    public void desarquivarChamado(Integer id) {
        repository.findById(id)
                .map(c -> {
                    if (c.isAtivo()) {
                        throw new RegraNegocioException("Chamado já está ativo");
                    }

                    c.setAtivo(true);
                    repository.save(c);

                    return c;
                }).orElseThrow(() ->
                new ChamadoNaoEncontradoException());
    }

    @Override
    public Optional<Chamado> buscarChamadoPorId(Integer id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public void atribuirTecn(Integer id, TecnicoDTO tecnicoDTO) {
        Chamado chamado = repository
                .findById(id).map(c -> {
                    if (!c.isAtivo()) {
                        throw new RegraNegocioException("O chamado não está ativo");
                    }
                    return c;
                })
                .orElseThrow(() -> new ChamadoNaoEncontradoException());

        Usuario user = usuarioRepository
                .findById(tecnicoDTO.getIdTecnico())
                .orElseThrow(() -> new UsuarioNaoEncontradoException());

        if (!user.isAtivo()) {
            throw new RegraNegocioException("O usuário não está ativo");
        } else if (!user.isTecn()) {
            throw new RegraNegocioException("O usuário não é técnico");
        } else if (user.isAdmin()) {
            chamado.setTecnico(user);
            chamado.setStatusChamado(StatusChamado.PROCESSANDO);
        } else if (chamado.getRequerente().getId() == user.getId()) {
            throw new RegraNegocioException("O mesmo requerente não pode ser atribuído como técnico");
        }

        chamado.setTecnico(user);
        chamado.setStatusChamado(StatusChamado.PROCESSANDO);

        repository.save(chamado);
    }

    @Override
    @Transactional
    public void removerTecn(Integer id){
        Chamado chamado = repository
                .findById(id).map(c -> {
                    if (!c.isAtivo()) {
                        throw new RegraNegocioException("O chamado não está ativo");
                    }

                    if( c.getTecnico() == null){
                        throw new RegraNegocioException("Não existem técnicos atribuídos ao chamado");
                    }

                    return c;
                })
                .orElseThrow(() -> new ChamadoNaoEncontradoException());

        chamado.setTecnico(null);
        chamado.setDataFinal(null);
        chamado.setStatusChamado(StatusChamado.PENDENTE);

        repository.save(chamado);
    }

    @Override
    public List<InformacoesChamadoDTO> buscarPorPar(Chamado filtro, FiltroChamadoDTO filtroDTO) {

        filtro = converterDTOBuscaPorPar(filtro, filtroDTO);
        System.out.println("filtro: " + filtro);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(filtro, matcher);

        List<Chamado> list = repository.findAll(example);

        List<InformacoesChamadoDTO> list2 = new ArrayList<>();

        for (Chamado c : list) {
            list2.add(converterChamadoParaInformacao(c));
        }

        return list2;
    }

    public Chamado converterDTOBuscaPorPar(Chamado filtro, FiltroChamadoDTO filtroDTO) {
        Usuario req = new Usuario();
        Usuario tec = new Usuario();
        Usuario aux = new Usuario();

        if (filtroDTO.getNomeRequerente() != null) {
            req = usuarioRepository.findByNome(filtroDTO.getNomeRequerente());

            if (req == null) {
                aux.setNome(filtroDTO.getNomeRequerente());
                filtro.setRequerente(aux);
            } else {
                filtro.setRequerente(req);
            }
        }

        if (filtroDTO.getMatriculaRequerente() != null) {
            req = usuarioRepository.buscaMatricula(filtroDTO.getMatriculaRequerente());

            if (req == null) {
                aux.setMatricula(filtroDTO.getMatriculaRequerente());
                filtro.setRequerente(aux);
            } else {
                filtro.setRequerente(req);
            }
        }

        if (filtroDTO.getNomeTecnico() != null) {
            tec = usuarioRepository.findByNome(filtroDTO.getNomeTecnico());

            if (tec == null) {
                aux.setNome(filtroDTO.getNomeTecnico());
                filtro.setTecnico(aux);
            } else {
                filtro.setTecnico(tec);
            }
        }

        if (filtroDTO.getMatriculaTecn() != null) {
            tec = usuarioRepository.buscaMatricula(filtroDTO.getMatriculaTecn());

            if (tec == null) {
                aux.setMatricula(filtroDTO.getMatriculaTecn());
                filtro.setTecnico(aux);
            } else {
                filtro.setTecnico(tec);
            }

        }

        if (filtroDTO.getDataInicial() != null) {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//            LocalDateTime dateTime = LocalDate.parse(filtroDTO.getDataInicial(), formatter).atStartOfDay();
//
//            filtro.setDataInicio(dateTime);
//
//            System.out.println("Data do filtro: " + filtro.getDataInicio());

            //filtro.setDataInicio(LocalDateTime.parse(filtroDTO.getDataInicial(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            filtro.setDataInicio(LocalDate.parse(filtroDTO.getDataInicial(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        if (filtroDTO.getDataSolucao() != null) {
            filtro.setDataFinal(LocalDate.parse(filtroDTO.getDataSolucao(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        if (filtroDTO.getStatus() != null) {
            filtro.setStatusChamado(StatusChamado.valueOf(filtroDTO.getStatus().toUpperCase()));
        }

        if (filtroDTO.getTipo() != null) {
            filtro.setTipoChamado(TipoChamado.valueOf(filtroDTO.getTipo().toUpperCase()));
        }

        return filtro;
    }

    public final static InformacoesChamadoDTO converterChamadoParaInformacao(Chamado chamado) {

        String dataSolucao = new String();
        String solucao = new String();
        String nomeTecn = new String();
        String matriculaTecn = new String();

        if (chamado.getDataFinal() == null) {
            dataSolucao = "Chamado ainda não foi solucionado.";
        } else {
            dataSolucao = chamado.getDataFinal().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        if (chamado.getTecnico() == null) {
            nomeTecn = "Nenhum Técnico atribuído ao chamado.";
            matriculaTecn = "";
        } else {
            nomeTecn = chamado.getTecnico().getNome();
            matriculaTecn = chamado.getTecnico().getMatricula();
        }

        if(chamado.getSolucao() == null || chamado.getSolucao().isEmpty()){
            solucao = "Chamado ainda não foi solucionado.";
        }else{
            solucao = chamado.getSolucao();
        }

        return InformacoesChamadoDTO.builder()
                .id(chamado.getId())
                .requerente(chamado.getRequerente().getNome())
                .matricula(chamado.getRequerente().getMatricula())
                .titulo(chamado.getTitulo())
                .descricao(chamado.getDescricao())
                .tipo(chamado.getTipoChamado().toString())
                .bloco(chamado.getBloco())
                .sala(chamado.getSala())
                .status(chamado.getStatusChamado().name())
                .dataInicio(chamado.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .dataSolucao(dataSolucao)
                .solucao(solucao)
                .tecnico(nomeTecn)
                .matriculaTecn(matriculaTecn)
                .ativo(chamado.isAtivo())
                .build();
    }
}
