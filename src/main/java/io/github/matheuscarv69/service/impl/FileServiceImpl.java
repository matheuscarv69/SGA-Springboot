package io.github.matheuscarv69.service.impl;

import io.github.matheuscarv69.domain.entity.Usuario;
import io.github.matheuscarv69.domain.model.File;
import io.github.matheuscarv69.domain.repository.UsuarioRepository;
import io.github.matheuscarv69.exception.RegraNegocioException;
import io.github.matheuscarv69.exception.UsuarioNaoEncontradoException;
import io.github.matheuscarv69.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final UsuarioRepository repository;

    @Override
    public File save(MultipartFile file, Integer usuarioId) {
        try {
            return uploadFile(file, usuarioId);
        } catch (IOException e) {
            throw new RegraNegocioException("Imagem não foi enviada");
        }
    }

    private File uploadFile(MultipartFile file, Integer usuarioId) throws IOException {
        byte[] bytes = file.getBytes();

        Usuario usuario = repository.findById(usuarioId)
                .map(user -> {
                    user.setImagem(bytes);
                    repository.save(user);
                    return user;
                }).orElseThrow(() -> new UsuarioNaoEncontradoException());

        return File
                .builder()
                .bytes(file.getBytes())
                .type(file.getContentType())
                .filename(file.getOriginalFilename())
                .build();
    }


}
