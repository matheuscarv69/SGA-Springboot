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
                .orElseThrow(() ->
                        new RegraNegocioException("Código de usuário inválido."));

        Chamado chamado = new Chamado();
        chamado.setRequerente(usuario);
        chamado.setTitulo(dto.getTitulo());
        chamado.setDescricao(dto.getDescricao());
        chamado.setTipo(TipoChamado.valueOf(dto.getTipo()));
        chamado.setBloco(dto.getBloco());
        chamado.setSala(dto.getSala());
        chamado.setDataInicio(LocalDate.now());
        chamado.setStatus(StatusChamado.PENDENTE);

        repository.save(chamado);

        return chamado;
    }

    @Override
    public Optional<Chamado> buscarChamadoPorId(Integer id) {

        return repository.findById(id);
    }

    @Override
    public List<Chamado> buscarTodos() {
        return repository.findAll();
    }

    @Override
    public List<Chamado> buscarPorPar(Chamado filtro) {

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(filtro, matcher);

        return repository.findAll(example);
    }


    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusChamado statusChamado) {
        repository
                .findById(id)
                .map(c -> {
                    c.setStatus(statusChamado);
                    repository.save(c);
                    return c;
                }).orElseThrow(() -> new ChamadoNaoEncontradoException());
    }

    @Override
    @Transactional
    public void atribuirTecn(Integer id, TecnicoDTO tecnicoDTO) {

        Usuario user = usuarioRepository
                .findById(tecnicoDTO.getIdTecnico())
                .orElseThrow(() -> new UsuarioNaoEncontradoException());

        Chamado chamado = repository
                .findById(id)
                .orElseThrow(() -> new ChamadoNaoEncontradoException());

        System.out.println("Usuario é tecnico: " + user.isTecn());

        if (!user.isTecn()) {
            throw new RegraNegocioException("O usuário não é técnico");
        } else if (chamado.getRequerente().getId() == user.getId()) {
            throw new RegraNegocioException("O mesmo requerente não pode ser atribuído como técnico");
        }

        chamado.setTecnico(user);
        chamado.setStatus(StatusChamado.PROCESSANDO);

        repository.save(chamado);
    }

    @Override
    @Transactional
    public void excluir(Integer id) {
        repository.findById(id)
                .map(c -> {
                    repository.delete(c);
                    return c;
                }).orElseThrow(() -> new ChamadoNaoEncontradoException());
    }


}
