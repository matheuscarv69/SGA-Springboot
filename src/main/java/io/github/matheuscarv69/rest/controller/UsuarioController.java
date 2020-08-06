package io.github.matheuscarv69.rest.controller;

import io.github.matheuscarv69.domain.entity.Chamado;
import io.github.matheuscarv69.domain.entity.Usuario;
import io.github.matheuscarv69.domain.repository.UsuarioRepository;
import io.github.matheuscarv69.exception.RegraNegocioException;
import io.github.matheuscarv69.exception.UsuarioNaoEncontradoException;
import io.github.matheuscarv69.rest.dto.InformacoesChamadoDTO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static io.github.matheuscarv69.rest.controller.ChamadoController.converter;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private UsuarioRepository repository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.repository = usuarioRepository;
    }

    @PostMapping // salva um user
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario save(@RequestBody Usuario usuario) {

        if (usuario.getMatricula().isEmpty()) {
            throw new RegraNegocioException("O campo de matrícula está vazio");
        }

        Usuario user = repository.buscaMatricula(usuario.getMatricula());

        System.out.println("Usuario : " + user);

        if (user == null) {
            return repository.save(usuario);
        } else {
            throw new RegraNegocioException("A matrícula informada já existe");
        }

    }

    @PutMapping("{id}") // atualiza um usuario
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @RequestBody Usuario usuario) {
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

                    if (user == null) {
                        usuario.setId(usuarioExistente.getId());
                        repository.save(usuario);
                        return usuarioExistente;

                    } else {
                        if (user.getMatricula() == usuarioExistente.getMatricula()) {
                            usuario.setId(usuarioExistente.getId());

                            repository.save(usuario);
                            return usuarioExistente;
                        } else {
                            throw new RegraNegocioException("A matrícula informada já existe");
                        }
                    }
                }).orElseThrow(() ->
                new UsuarioNaoEncontradoException());

    }

    @DeleteMapping("{id}") // deleta um user
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

    @PatchMapping("{id}")
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

    @GetMapping("{id}") // busca um user por id
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

        if(user == null){
            List<InformacoesChamadoDTO> list = new ArrayList<>() ;
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

        if(user == null){
            List<InformacoesChamadoDTO> list = new ArrayList<>() ;
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

        //filtro.setStatus(true);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(filtro, matcher);

        return repository.findAll(example);
    }

}
