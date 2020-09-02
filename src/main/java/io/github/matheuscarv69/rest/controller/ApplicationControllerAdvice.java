package io.github.matheuscarv69.rest.controller;

import io.github.matheuscarv69.exception.*;
import io.github.matheuscarv69.rest.ApiErrors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(RegraNegocioException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleRegraNegocioException(RegraNegocioException ex) {
        String mensagemErro = ex.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(ChamadoNaoEncontradoException.class)
    @ResponseStatus(NOT_FOUND)
    public ApiErrors handleChamadoNotFoundException(ChamadoNaoEncontradoException ex) {
        String mensagemErro = ex.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    @ResponseStatus(NOT_FOUND)
    public ApiErrors handleUsuarioNotFoundException(UsuarioNaoEncontradoException ex) {
        String mensagemErro = ex.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(erro -> erro.getDefaultMessage())
                .collect(Collectors.toList());

        return new ApiErrors(errors);
    }

    @ExceptionHandler(ArquivoInvalidoException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleArquivoInvalidoException(ArquivoInvalidoException ex){
        String mensagemErro = ex.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(SenhaInvalidaException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleSenhaInvalidaException(SenhaInvalidaException ex){
        String mensagemErro = ex.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(LoginNotFoundException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ApiErrors handleLoginNotFoundException(LoginNotFoundException ex){
        String mensagemErro = ex.getMessage();
        return new ApiErrors(mensagemErro);
    }

}
