package io.github.matheuscarv69.service.impl;

import io.github.matheuscarv69.domain.entity.Chamado;
import io.github.matheuscarv69.domain.entity.Usuario;
import io.github.matheuscarv69.domain.repository.UsuarioRepository;
import io.github.matheuscarv69.exception.RegraNegocioException;
import io.github.matheuscarv69.exception.SenhaInvalidaException;
import io.github.matheuscarv69.exception.UsuarioNaoEncontradoException;
import io.github.matheuscarv69.rest.dto.CredenciaisDTO;
import io.github.matheuscarv69.rest.dto.InformacoesChamadoDTO;
import io.github.matheuscarv69.rest.dto.TokenDTO;
import io.github.matheuscarv69.security.jwt.JwtService;
import io.github.matheuscarv69.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.github.matheuscarv69.rest.controller.ChamadoController.converter;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioLoginServiceImpl usuarioLoginService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override //ok
    public Usuario salvar(Usuario usuario) {
        if (usuario.getMatricula().isEmpty()) {
            throw new RegraNegocioException("O campo de matrícula está vazio");
        }

        Usuario user = repository.buscaMatricula(usuario.getMatricula());
        Optional<Usuario> user2 = repository.findByLogin(usuario.getLogin());

        if (user != null) {
            throw new RegraNegocioException("A matrícula informada já existe");
        } else if (user2.isPresent()) {
            throw new RegraNegocioException("O login informado já existe");
        } else {
            String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());

            usuario.setSenha(senhaCriptografada);

            return repository.save(usuario);
        }
    }

    @Override //ok
    public void atualizarUser(Integer id, Usuario usuario) {
        if (usuario.getMatricula().isEmpty()) {
            throw new RegraNegocioException("O campo de matrícula está vazio");
        }

        repository
                .findById(id)
                .map(usuarioExistente -> {
                    if (!usuarioExistente.isAtivo()) {
                        throw new RegraNegocioException("O usuário não está ativo");
                    }

                    Usuario user = repository.buscaMatricula(usuario.getMatricula());
                    Optional<Usuario> user2 = repository.findByLogin(usuario.getLogin());

                    if (user2.isPresent()) {
                        if (usuarioExistente.getLogin() == user2.get().getLogin()) {
                            boolean igual = true;
                        } else if (usuarioExistente.getMatricula() == user2.get().getMatricula()) {
                            boolean igual = true;
                        } else {
                            throw new RegraNegocioException("O login informado já está existe e não pertence ao usuário informado");
                        }
                    }

                    if (user == null) {
                        usuario.setId(usuarioExistente.getId());

                        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());

                        usuario.setSenha(senhaCriptografada);

                        repository.save(usuario);
                        return usuario;
                    } else if (usuarioExistente.getMatricula() == user.getMatricula()) {
                        System.out.println("Matrículas são iguais");
                        System.out.println("User: " + user.getMatricula());
                        System.out.println("UsuarioExistente: " + usuarioExistente.getMatricula());

                        usuario.setId(usuarioExistente.getId());
                        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());

                        usuario.setSenha(senhaCriptografada);

                        repository.save(usuario);
                        return usuario;
                    } else {
                        throw new RegraNegocioException("A matrícula informada já existe e não pertence ao usuário informado");
                    }


                }).orElseThrow(() ->
                new UsuarioNaoEncontradoException());

    }

    @Override //ok
    public void desligarUser(Integer id) {
        repository
                .findById(id)
                .map(usuarioExistente -> {
                    if (!usuarioExistente.isAtivo()) {
                        throw new RegraNegocioException("O Usuário já está inativo");
                    }

                    usuarioExistente.setAtivo(false);
                    repository.save(usuarioExistente);

                    return usuarioExistente;
                }).orElseThrow(() ->
                new UsuarioNaoEncontradoException());
    }

    @Override //ok
    public void ativarUser(Integer id) {
        repository.findById(id)
                .map(usuarioExistente -> {
                    if (usuarioExistente.isAtivo()) {
                        throw new RegraNegocioException("Usuário já está ativo");
                    }

                    usuarioExistente.setAtivo(true);
                    repository.save(usuarioExistente);

                    return usuarioExistente;
                }).orElseThrow(() ->
                new UsuarioNaoEncontradoException());
    }

    @Override //ok
    public Usuario buscarUserPorId(Integer id) {
        return repository
                .findById(id)
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException());
    }

    @Override //ok
    public List<Usuario> buscarTecnicos() {
        List<Usuario> list = repository.findTecnicos();

        if (list.isEmpty()) {
            throw new UsuarioNaoEncontradoException("Nenhum técnico cadastrado");
        }

        return list;
    }

    @Override //ok
    public List<Usuario> buscarAdministradores() {
        List<Usuario> list = repository.findAdministradores();

        if (list.isEmpty()) {
            throw new UsuarioNaoEncontradoException("Nenhum administrador cadastrado");
        }

        return list;
    }

    @Override //ok
    public void setTecn(Integer id, Usuario tecn) {
        repository.findById(id)
                .map(usuario -> {
                    if (!usuario.isAtivo()) {
                        throw new RegraNegocioException("O usuário não está ativo");
                    } else if (usuario.isTecn() && tecn.isTecn()) {
                        throw new RegraNegocioException("Usuário já é técnico");
                    }

                    usuario.setTecn(tecn.isTecn());
                    repository.save(usuario);

                    return usuario;
                }).orElseThrow(() ->
                new UsuarioNaoEncontradoException());
    }

    @Override //ok
    public void setAdmin(Integer id, Usuario admin) {
        repository.findById(id)
                .map(usuario -> {
                    if (!usuario.isAtivo()) {
                        throw new RegraNegocioException("O usuário não está ativo");
                    } else if (usuario.isAdmin() && admin.isAdmin()) {
                        throw new RegraNegocioException("Usuário já é administrador");
                    }

                    usuario.setAdmin(admin.isAdmin());
                    usuario.setTecn(true); // Em teste
                    repository.save(usuario);

                    return usuario;
                }).orElseThrow(() ->
                new UsuarioNaoEncontradoException());
    }

    @Override //ok
    public List<InformacoesChamadoDTO> buscarChamadosReq(Integer id) {
        Usuario user = repository.findById(id)
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException());

        user = repository.findUsuarioFetchChamadosReq(user.getId());

        if (user == null) {
            List<InformacoesChamadoDTO> list = new ArrayList<>();
            return list;
        }

        List<Chamado> listaChamado = user.getChamadosReq();

        List<InformacoesChamadoDTO> listaDTO = new ArrayList<>();

        for (Chamado c : listaChamado) {
            listaDTO.add(converter(c));
        }

        return listaDTO;
    }

    @Override //ok
    public List<InformacoesChamadoDTO> buscarChamadosTecn(Integer id) {
        Usuario user = repository.findById(id)
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException());

        if (!user.isTecn()) {
            throw new RegraNegocioException("O usuário não é técnico");
        }

        user = repository.findUsuarioFetchChamadosTecn(user.getId());

        if (user == null) {
            List<InformacoesChamadoDTO> list = new ArrayList<>();
            return list;
        }

        List<Chamado> listaChamado = user.getChamadosTecn();
        List<InformacoesChamadoDTO> listaDTO = new ArrayList<>();

        for (Chamado c : listaChamado) {
            listaDTO.add(converter(c));
        }

        return listaDTO;
    }

    @Override //ok
    public List<Usuario> buscarPorPar(Usuario filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(filtro, matcher);

        return repository.findAll(example);
    }

    @Override //ok
    public TokenDTO autenticar(CredenciaisDTO credenciais) {
        try {
            Usuario usuario = Usuario
                    .builder()
                    .login(credenciais.getLogin())
                    .senha(credenciais.getSenha())
                    .build();
            UserDetails usuarioAutenticado = usuarioLoginService.autenticar(usuario);
            String token = jwtService.gerarToken(usuario);

            return new TokenDTO(usuario.getLogin(), token);

        } catch (UsernameNotFoundException | SenhaInvalidaException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
