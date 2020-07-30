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
import io.github.matheuscarv69.rest.dto.TecnicoDTO;
import io.github.matheuscarv69.service.ChamadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
        chamado.setTipo(TipoChamado.valueOf(dto.getTipo()));
        chamado.setBloco(dto.getBloco());
        chamado.setSala(dto.getSala());
        chamado.setDataInicio(LocalDate.now());
        chamado.setStatusChamado(StatusChamado.PENDENTE);

        repository.save(chamado);

        return chamado;
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusChamado statusChamado) {
        repository
                .findById(id)
                .map(c -> {
                    if (!c.isAtivo()) {
                        throw new RegraNegocioException("O chamado não está ativo");
                    }

                    c.setStatusChamado(statusChamado);

                    if (statusChamado == StatusChamado.SOLUCIONADO) {
                        c.setDataFinal(LocalDate.now());
                    }

                    repository.save(c);
                    return c;
                }).orElseThrow(() -> new ChamadoNaoEncontradoException());
    }

    @Override
    @Transactional // desligar chamado
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
    public List<Chamado> buscarPorPar(Chamado filtro) {

        System.out.println("Ativo " + filtro.isAtivo());


        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(filtro, matcher);

        return repository.findAll(example);
    }

    @Override
    @Transactional
    public void atribuirTecn(Integer id, TecnicoDTO tecnicoDTO) {
        Chamado chamado = repository
                .findById(id)
                .orElseThrow(() -> new ChamadoNaoEncontradoException());

        if (!chamado.isAtivo()) {
            throw new RegraNegocioException("O chamado não está ativo");
        }

        Usuario user = usuarioRepository
                .findById(tecnicoDTO.getIdTecnico())
                .orElseThrow(() -> new UsuarioNaoEncontradoException());

        if (!user.isAtivo()) {
            throw new RegraNegocioException("O usuário não está ativo");
        } else if (!user.isTecn()) {
            throw new RegraNegocioException("O usuário não é técnico");
        } else if (chamado.getRequerente().getId() == user.getId()) {
            throw new RegraNegocioException("O mesmo requerente não pode ser atribuído como técnico");
        }

        chamado.setTecnico(user);
        chamado.setStatusChamado(StatusChamado.PROCESSANDO);

        repository.save(chamado);
    }


}
