package io.github.matheuscarv69.service;

import io.github.matheuscarv69.domain.model.File;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    File save(MultipartFile multipartFile, Integer usuarioId);

}
