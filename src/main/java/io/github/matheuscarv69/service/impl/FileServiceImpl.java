package io.github.matheuscarv69.service.impl;

import io.github.matheuscarv69.domain.entity.Chamado;
import io.github.matheuscarv69.domain.entity.Usuario;
import io.github.matheuscarv69.domain.model.File;
import io.github.matheuscarv69.domain.repository.ChamadoRepository;
import io.github.matheuscarv69.domain.repository.UsuarioRepository;
import io.github.matheuscarv69.exception.ChamadoNaoEncontradoException;
import io.github.matheuscarv69.exception.RegraNegocioException;
import io.github.matheuscarv69.exception.UsuarioNaoEncontradoException;
import io.github.matheuscarv69.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final UsuarioRepository usuarioRepository;
    private final ChamadoRepository chamadoRepository;

    @Override
    public File saveImgUser(MultipartFile file, Integer usuarioId) {
        try {
            return uploadFileUser(file, usuarioId);
        } catch (IOException e) {
            throw new RegraNegocioException("Imagem não foi enviada");
        }
    }

    @Override
    public File saveImgCalled(MultipartFile multipartFile, Integer chamadoId) {
        try {
            return uploadFileCalled(multipartFile, chamadoId);
        } catch (IOException e) {
            throw new RegraNegocioException("Imagem não foi enviada");
        }
    }

    @Transactional
    private File uploadFileUser(MultipartFile file, Integer usuarioId) throws IOException {
        byte[] bytes = file.getBytes();

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .map(user -> {
                    user.setImagem(bytes);
                    usuarioRepository.saveAndFlush(user);
                    return user;
                }).orElseThrow(() -> new UsuarioNaoEncontradoException());

        return File
                .builder()
                .bytes(file.getBytes())
                .type(file.getContentType())
                .filename(file.getOriginalFilename())
                .build();
    }

    @Transactional
    private File uploadFileCalled(MultipartFile file, Integer chamadoId) throws IOException {
        byte[] bytes = file.getBytes();

        Chamado chamado = chamadoRepository.findById(chamadoId)
                .map(called -> {
                    called.setImagem(bytes);
                    chamadoRepository.saveAndFlush(called);
                    return called;
                }).orElseThrow(() -> new ChamadoNaoEncontradoException());

        return File
                .builder()
                .bytes(file.getBytes())
                .type(file.getContentType())
                .filename(file.getOriginalFilename())
                .build();
    }

}
