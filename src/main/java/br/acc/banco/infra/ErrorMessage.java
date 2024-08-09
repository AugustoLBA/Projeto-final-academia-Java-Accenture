package br.acc.banco.infra;

import java.util.Map;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorMessage {

    private HttpStatus status;

    private String message;

    private Map<String, String> errors;

    public ErrorMessage(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.errors = null;
    }
}