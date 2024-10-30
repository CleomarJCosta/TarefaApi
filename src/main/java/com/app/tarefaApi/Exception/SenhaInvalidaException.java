package com.app.tarefaApi.Exception;

public class SenhaInvalidaException extends Exception {
    public SenhaInvalidaException(String message){
        super("Senha Inválida");
    }
}
