package io.github.matheuscarv69.rest.controller;

import io.github.matheuscarv69.domain.entity.Usuario;
import io.github.matheuscarv69.rest.dto.*;
import io.github.matheuscarv69.service.impl.UsuarioServiceImpl;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Api("Api Usuários")
public class UsuarioController {

    private UsuarioServiceImpl service;

    public UsuarioController(UsuarioServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Salvar um Usuário")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Usuário cadastrado com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação")
    })
    public Usuario save(@RequestBody @ApiParam("Dados do Usuário") @Valid Usuario usuario) {
        Usuario user = service.salvar(usuario);
        return user;
    }

    @PutMapping("/updt/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Atualizar um Usuário")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Usuário atualizado com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 401, message = "Usuário não autorizado"),
            @ApiResponse(code = 403, message = "Acesso negado"),
            @ApiResponse(code = 404, message = "Usuário não encontrado")
    })
    public void update(@PathVariable @ApiParam("Id do Usuário") Integer id, @RequestBody @ApiParam("Dados do Usuário") @Valid Usuario usuario) {
        service.atualizarUser(id, usuario);
    }

    @DeleteMapping("/deslUser/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Desligar um Usuário")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Usuário desligado com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 401, message = "Usuário não autorizado"),
            @ApiResponse(code = 403, message = "Acesso negado"),
            @ApiResponse(code = 404, message = "Usuário não encontrado")

    })
    public void desligarUser(@PathVariable @ApiParam("Id do Usuário") Integer id) {
        service.desligarUser(id);
    }

    @PatchMapping("/ativUser/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Ativar um Usuário")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Usuário ativado com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 401, message = "Usuário não autorizado"),
            @ApiResponse(code = 403, message = "Acesso negado"),
            @ApiResponse(code = 404, message = "Usuário não encontrado")
    })
    public void ativarUser(@PathVariable @ApiParam("Id do Usuário") Integer id) {
        service.ativarUser(id);
    }

    @GetMapping("/getId/{id}")
    @ApiOperation("Buscar um Usuário pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuário encontrado com sucesso"),
            @ApiResponse(code = 401, message = "Usuário não autorizado"),
            @ApiResponse(code = 403, message = "Acesso negado"),
            @ApiResponse(code = 404, message = "Usuário não encontrado")
    })
    public Usuario getUsuarioById(@PathVariable @ApiParam("Id do Usuário") Integer id) {
        Usuario user = service.buscarUserPorId(id);
        return user;
    }

    @GetMapping("/tecn")
    @ApiOperation("Buscar todos os Técnicos")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Técnicos encontrados com sucesso"),
            @ApiResponse(code = 401, message = "Usuário não autorizado"),
            @ApiResponse(code = 403, message = "Acesso negado"),
            @ApiResponse(code = 404, message = "Nenhum técnico encontrado")
    })
    public List<Usuario> findTecnicos() {
        return service.buscarTecnicos();
    }

    @GetMapping("/admin")
    @ApiOperation("Buscar todos os Administradores")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Administradores encontrados com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 401, message = "Usuário não autorizado"),
            @ApiResponse(code = 403, message = "Acesso negado"),
            @ApiResponse(code = 404, message = "Usuário não encontrado")
    })
    public List<Usuario> findAdministradores() {
        return service.buscarAdministradores();
    }

    @PatchMapping("/admin/setTecn/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Definir Usuário como Técnico")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Propriedade de Técnico do Usuário alterada com sucesso"),
            @ApiResponse(code = 401, message = "Usuário não autorizado"),
            @ApiResponse(code = 403, message = "Acesso negado"),
            @ApiResponse(code = 404, message = "Usuário não encontrado")
    })
    public void setTecn(@PathVariable @ApiParam("Id do Usuário") Integer id, @RequestBody @ApiParam("Propriedade de técnico") BoolTecnicoDTO boolTecnDTO) {
        service.setTecn(id, boolTecnDTO);

    }

    @PatchMapping("/admin/setAdmin/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Definir Usuário como Administrador")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Propriedade de Administrador do Usuário alterada com sucesso"),
            @ApiResponse(code = 401, message = "Usuário não autorizado"),
            @ApiResponse(code = 403, message = "Acesso negado"),
            @ApiResponse(code = 404, message = "Usuário não encontrado")
    })
    public void setAdmin(@PathVariable @ApiParam("Id do Usuário") Integer id, @RequestBody  @ApiParam("Propriedade de Administrador") BoolAdministradorDTO admin) {
        service.setAdmin(id, admin);
    }

    @GetMapping("/chamadosReq/{id}")
    @ApiOperation("Buscar os Chamados que um Usuário fez")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Chamados do Usuário informado foram encontrados com sucesso"),
            @ApiResponse(code = 401, message = "Usuário não autorizado"),
            @ApiResponse(code = 404, message = "Dados não encontrados")
    })
    public List<InformacoesChamadoDTO> findChamadosReq(@PathVariable @ApiParam("Id do Usuário") Integer id) {
        return service.buscarChamadosReq(id);
    }

    @GetMapping("/chamadosTecn/{id}")
    @ApiOperation("Buscar os Chamados que um Técnico está atribuído")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Chamados do Técnico informado foram encontrados com sucesso"),
            @ApiResponse(code = 401, message = "Usuário não autorizado"),
            @ApiResponse(code = 403, message = "Acesso negado"),
            @ApiResponse(code = 404, message = "Dados não encontrados")
    })
    public List<InformacoesChamadoDTO> findChamadosTecn(@PathVariable @ApiParam("Id do Usuário") Integer id) {
        return service.buscarChamadosTecn(id);
    }

    @GetMapping
    @ApiOperation("Buscar de Usuário por Parâmetro e Busca todos os Usuários")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Usuário não autorizado"),
            @ApiResponse(code = 403, message = "Acesso negado")
    })
    public List<Usuario> buscarPorPar(Usuario filtro) {
        return service.buscarPorPar(filtro);
    }

    @PostMapping("/auth")
    @ApiOperation("Autenticar Login do Usuário")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuário autenticado com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 401, message = "Usuário não autorizado")
    })
    public TokenDTO autenticar(@RequestBody @ApiParam("Login e Senha do Usuário") CredenciaisDTO credenciais) {
        TokenDTO token = service.autenticar(credenciais);
        return token;
    }
}
