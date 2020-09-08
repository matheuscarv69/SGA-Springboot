package io.github.matheuscarv69.rest.controller;

import io.github.matheuscarv69.domain.model.File;
import io.github.matheuscarv69.exception.ArquivoInvalidoException;
import io.github.matheuscarv69.service.FileService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/file")
public class FileController {

    private FileService service;

    public FileController(FileService service) {
        this.service = service;
    }

    @PostMapping("/upldUserImg/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Enviar imagem de perfil de Usuário")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Imagem enviada com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 403, message = "Acesso negado"),
            @ApiResponse(code = 404, message = "Usuário não encontrado")
    })
    public File uploadFileUser(@RequestParam("file") @ApiParam("Arquivo de Imagem") MultipartFile file, @PathVariable(name = "usuarioId") @ApiParam("Id do Usuário") Integer usuarioId) {
        if (file.isEmpty()) {
            throw new ArquivoInvalidoException("Nenhum arquivo foi selecionado");
        }

        return service.saveImgUser(file, usuarioId);
    }

    @PostMapping("/upldCalledImg/{chamadoId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Enviar imagem do Chamado")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Imagem enviada com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 403, message = "Acesso negado"),
            @ApiResponse(code = 404, message = "Chamado não encontrado")
    })
    public File uploadFileCalled(@RequestParam("file")  @ApiParam("Arquivo de Imagem") MultipartFile file, @PathVariable(name = "chamadoId") @ApiParam("Id do Chamado") Integer chamadoId) {
        if (file.isEmpty()) {
            throw new ArquivoInvalidoException("Nenhum arquivo foi selecionado");
        }
        return service.saveImgCalled(file, chamadoId);
    }


}
