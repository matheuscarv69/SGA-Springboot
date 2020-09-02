package io.github.matheuscarv69.rest.controller;

import io.github.matheuscarv69.domain.model.File;
import io.github.matheuscarv69.exception.ArquivoInvalidoException;
import io.github.matheuscarv69.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/usuarios")
public class FileController {

    private FileService service;

    public FileController(FileService service) {
        this.service = service;
    }

    @PostMapping("/uploadFile/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    public File uploadFile(@RequestParam("file") MultipartFile file, @PathVariable(name = "usuarioId") Integer usuarioId) {
        if(file.isEmpty()){
            throw new ArquivoInvalidoException("Nenhum arquivo foi selecionado");
        }

        return service.save(file, usuarioId);
    }


}
