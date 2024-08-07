package br.acc.banco.exception;

public class SaqueInvalidoException extends RuntimeException{

    public  SaqueInvalidoException(String message){
        super(message);
    }
}
