package io.github.matheuscarv69.rest.controller;

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
import io.github.matheuscarv69.service.impl.UsuarioLoginServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.github.matheuscarv69.rest.controller.ChamadoController.converter;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioLoginServiceImpl usuarioLoginService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    private UsuarioRepository repository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.repository = usuarioRepository;
    }

    @PostMapping // salva um user
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario save(@RequestBody @Valid Usuario usuario) {

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

    @PutMapping("/updt/{id}") // atualiza um usuario
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @RequestBody @Valid Usuario usuario) {
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

    @DeleteMapping("/deslUser/{id}") // deleta um user
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desligarUser(@PathVariable Integer id) {
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

    @PatchMapping("/ativUser/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativarUser(@PathVariable Integer id) {
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

    @GetMapping("/getId/{id}") // busca um user por id
    public Usuario getUsuarioById(@PathVariable Integer id) {

        return repository
                .findById(id)
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException());
    }

    @GetMapping("/tecn")
    public List<Usuario> findTecnicos() {
        List<Usuario> list = repository.findTecnicos();

        if (list.isEmpty()) {
            throw new UsuarioNaoEncontradoException("Nenhum técnico cadastrado");
        }

        return list;
    }

    @GetMapping("/admin")
    public List<Usuario> findAdministradores() {
        List<Usuario> list = repository.findAdministradores();

        if (list.isEmpty()) {
            throw new UsuarioNaoEncontradoException("Nenhum administrador cadastrado");
        }

        return list;
    }

    @PutMapping("/admin/setTecn/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setTecn(@PathVariable Integer id, @RequestBody Usuario tecn) {

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

    @PutMapping("/admin/setAdmin/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setAdmin(@PathVariable Integer id, @RequestBody Usuario admin) {

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

    @GetMapping("/chamadosReq/{id}") // busca os chamados que um user fez
    public List<InformacoesChamadoDTO> findChamadosReq(@PathVariable Integer id) {
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

    @GetMapping("/chamadosTecn/{id}") // busca os chamados que um tecnico está atribuido
    public List<InformacoesChamadoDTO> findChamadosTecn(@PathVariable Integer id) {
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

    @GetMapping // busca por parametros: No campo Query defina qual propriedade quer buscar
    public List<Usuario> buscarPorPar(Usuario filtro) {

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(filtro, matcher);

        return repository.findAll(example);
    }

    @PostMapping("/auth")
    public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais) {
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