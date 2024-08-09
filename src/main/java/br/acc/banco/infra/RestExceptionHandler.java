package br.acc.banco.infra;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.acc.banco.exception.CompraInvalidaException;
import br.acc.banco.exception.DepositoInvalidoException;
import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.exception.PixInvalidoException;
import br.acc.banco.exception.SaqueInvalidoException;
import br.acc.banco.exception.TransferenciaInvalidaException;
import br.acc.banco.exception.UsernameUniqueViolationException;

@RestControllerAdvice
public class RestExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, "Validation Error", errors);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> entityNotFoundException(EntityNotFoundException ex){
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
	 @ExceptionHandler(UsernameUniqueViolationException.class)
	    public ResponseEntity<ErrorMessage> usernameUniqueViolationException(UsernameUniqueViolationException ex){
	        ErrorMessage errorMessage= new ErrorMessage(HttpStatus.CONFLICT, ex.getMessage());
	        return new ResponseEntity<>(errorMessage,HttpStatus.CONFLICT);
	    }
	 @ExceptionHandler(DepositoInvalidoException.class)
	    public ResponseEntity<ErrorMessage> depositoInvalidoException(DepositoInvalidoException ex){
	        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
	        return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
	    }
	 
	 @ExceptionHandler(SaqueInvalidoException.class)
	    public ResponseEntity<ErrorMessage> saqueInvalidoException(SaqueInvalidoException ex){
	        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
	        return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
	    }
	 @ExceptionHandler(PixInvalidoException.class)
	    public ResponseEntity<ErrorMessage> pixInvalidoException(PixInvalidoException ex){
	        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
	        return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
	    }
	 @ExceptionHandler(TransferenciaInvalidaException.class)
	    public ResponseEntity<ErrorMessage> transferenciaInvalidaException(TransferenciaInvalidaException ex){
	        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
	        return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
	    }
	 @ExceptionHandler(CompraInvalidaException.class)
	    public ResponseEntity<ErrorMessage> compraInvalidaException(CompraInvalidaException ex){
	        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
	        return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
	    }
	 

}