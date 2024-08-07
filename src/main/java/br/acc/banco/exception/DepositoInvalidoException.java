package br.acc.banco.exception;

public class DepositoInvalidoException extends RuntimeException{

    public  DepositoInvalidoException(String message){
        super(message);
    }
}
