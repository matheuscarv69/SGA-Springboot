package io.github.matheuscarv69.exception;

public class SenhaInvalidaException extends RuntimeException{
    public SenhaInvalidaException() {
        super("Senha inválida");
    }
}
