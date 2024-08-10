package br.acc.banco.infra;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.acc.banco.exception.CompraInvalidaException;
import br.acc.banco.exception.DepositoInvalidoException;
import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.exception.PixInvalidoException;
import br.acc.banco.exception.SaqueInvalidoException;
import br.acc.banco.exception.TransferenciaInvalidaException;
import br.acc.banco.exception.UsernameUniqueViolationException;

@RestController
public class ExceptionTriggerController {



    @GetMapping("/trigger/entity-not-found")
    public void triggerEntityNotFoundException() {
        throw new EntityNotFoundException("Entity not found");
    }

    @GetMapping("/trigger/username-conflict")
    public void triggerUsernameConflictException() {
        throw new UsernameUniqueViolationException("Username already exists");
    }

    @GetMapping("/trigger/invalid-deposit")
    public void triggerInvalidDepositException() {
        throw new DepositoInvalidoException("Invalid deposit");
    }

    @GetMapping("/trigger/invalid-withdraw")
    public void triggerInvalidWithdrawException() {
        throw new SaqueInvalidoException("Invalid withdraw");
    }

    @GetMapping("/trigger/invalid-pix")
    public void triggerInvalidPixException() {
        throw new PixInvalidoException("Invalid pix");
    }

    @GetMapping("/trigger/invalid-transfer")
    public void triggerInvalidTransferException() {
        throw new TransferenciaInvalidaException("Invalid transfer");
    }

    @GetMapping("/trigger/invalid-purchase")
    public void triggerInvalidPurchaseException() {
        throw new CompraInvalidaException("Invalid purchase");
    }
}
