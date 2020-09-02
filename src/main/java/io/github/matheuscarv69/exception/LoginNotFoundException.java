package io.github.matheuscarv69.exception;

public class LoginNotFoundException extends RuntimeException{
    public LoginNotFoundException() {
        super("Login do Usuário não encontrado na base de dados");
    }
}
