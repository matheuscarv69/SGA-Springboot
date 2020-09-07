package io.github.matheuscarv69.exception;

public class ChamadoNaoEncontradoException extends RuntimeException {

    public ChamadoNaoEncontradoException() {
        super("Chamado não encontrado.");
    }

    public ChamadoNaoEncontradoException(String message) {
        super(message);
    }
}
