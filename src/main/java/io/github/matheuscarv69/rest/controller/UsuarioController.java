package io.github.matheuscarv69.rest.controller;

import io.github.matheuscarv69.domain.entity.Usuario;
import io.github.matheuscarv69.rest.dto.CredenciaisDTO;
import io.github.matheuscarv69.rest.dto.InformacoesChamadoDTO;
import io.github.matheuscarv69.rest.dto.TokenDTO;
import io.github.matheuscarv69.service.impl.UsuarioServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private UsuarioServiceImpl service;

    public UsuarioController(UsuarioServiceImpl service) {
        this.service = service;
    }

    @PostMapping // salva um user
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario save(@RequestBody @Valid Usuario usuario) {
        Usuario user = service.salvar(usuario);
        return user;
    }

    @PutMapping("/updt/{id}") // atualiza um usuario
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @RequestBody @Valid Usuario usuario) {
        service.atualizarUser(id, usuario);
    }

    @DeleteMapping("/deslUser/{id}") // deleta um user
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desligarUser(@PathVariable Integer id) {
        service.desligarUser(id);
    }

    @PatchMapping("/ativUser/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativarUser(@PathVariable Integer id) {
        service.ativarUser(id);
    }

    @GetMapping("/getId/{id}") // busca um user por id
    public Usuario getUsuarioById(@PathVariable Integer id) {
        Usuario user = service.buscarUserPorId(id);
        return user;
    }

    @GetMapping("/tecn")
    public List<Usuario> findTecnicos() {
        return service.buscarTecnicos();
    }

    @GetMapping("/admin")
    public List<Usuario> findAdministradores() {
        return service.buscarAdministradores();
    }

    @PutMapping("/admin/setTecn/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setTecn(@PathVariable Integer id, @RequestBody Usuario tecn) {
        service.setTecn(id, tecn);

    }

    @PutMapping("/admin/setAdmin/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setAdmin(@PathVariable Integer id, @RequestBody Usuario admin) {
        service.setAdmin(id, admin);
    }

    @GetMapping("/chamadosReq/{id}") // busca os chamados que um user fez
    public List<InformacoesChamadoDTO> findChamadosReq(@PathVariable Integer id) {
        return service.buscarChamadosReq(id);
    }

    @GetMapping("/chamadosTecn/{id}") // busca os chamados que um tecnico está atribuido
    public List<InformacoesChamadoDTO> findChamadosTecn(@PathVariable Integer id) {
        return service.buscarChamadosTecn(id);
    }

    @GetMapping // busca por parametros: No campo Query defina qual propriedade quer buscar
    public List<Usuario> buscarPorPar(Usuario filtro) {
        return service.buscarPorPar(filtro);
    }

    @PostMapping("/auth")
    public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais) {
        TokenDTO token = service.autenticar(credenciais);
        return token;
    }
}