package br.acc.banco.controller;

import br.acc.banco.dto.ContaCorrenteCreateDTO;
import br.acc.banco.dto.ContaCorrenteResponseDTO;
import br.acc.banco.models.ContaCorrente;
import br.acc.banco.service.ContaCorrenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/banco/contaCorrente")
public class ContaCorrenteController {

    private final ContaCorrenteService contaCorrenteService;

    @PostMapping
    public ResponseEntity<ContaCorrenteResponseDTO> save(@Valid @RequestBody ContaCorrenteCreateDTO createDTO) {
        ContaCorrente contaCorrente = contaCorrenteService.salvar(contaCorrenteService.toContaCorrente(createDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(contaCorrenteService.toDto(contaCorrente));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaCorrenteResponseDTO> findById(@PathVariable Long id) {
        ContaCorrente contaCorrente = contaCorrenteService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(contaCorrenteService.toDto(contaCorrente));
    }

    @GetMapping
    public ResponseEntity<List<ContaCorrenteResponseDTO>> findAll() {
        List<ContaCorrente> contaCorrentes = contaCorrenteService.buscarTodas();
        return ResponseEntity.status(HttpStatus.OK).body(contaCorrenteService.toListDto(contaCorrentes));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        contaCorrenteService.deletarPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/deposito/{id}/{valorDeposito}")
    public ResponseEntity<ContaCorrenteResponseDTO> deposito(@PathVariable Long id, @PathVariable BigDecimal valorDeposito) {
        ContaCorrente contaCorrente = contaCorrenteService.deposito(valorDeposito, id);
        return ResponseEntity.status(HttpStatus.OK).body(contaCorrenteService.toDto(contaCorrente));
    }

    @PatchMapping("/saque/{id}/{valorSaque}")
    public ResponseEntity<ContaCorrenteResponseDTO> sacar(@PathVariable Long id, @PathVariable BigDecimal valorSaque) {
        ContaCorrente contaCorrente = contaCorrenteService.sacar(valorSaque, id);
        return ResponseEntity.status(HttpStatus.OK).body(contaCorrenteService.toDto(contaCorrente));
    }

    @PatchMapping("/transferencia/{idOrigem}/{idDestino}/{valorTransferencia}")
    public ResponseEntity<ContaCorrenteResponseDTO> transferencia(@PathVariable Long idOrigem, @PathVariable Long idDestino, @PathVariable BigDecimal valorTransferencia) {
        ContaCorrente contaCorrente = contaCorrenteService.transferencia(valorTransferencia, idOrigem, idDestino);
        return ResponseEntity.status(HttpStatus.OK).body(contaCorrenteService.toDto(contaCorrente));
    }

    @PatchMapping("/compra/{id}/{valorCompra}/{nomeEstabelecimento}")
    public ResponseEntity<ContaCorrenteResponseDTO> compra(@PathVariable Long id, @PathVariable BigDecimal valorCompra, @PathVariable String nomeEstabelecimento) {
        ContaCorrente contaCorrente = contaCorrenteService.compra(valorCompra, id, nomeEstabelecimento);
        return ResponseEntity.status(HttpStatus.OK).body(contaCorrenteService.toDto(contaCorrente));
    }

    @PatchMapping("/pix/{id}/{valorPix}/{chavePix}")
    public ResponseEntity<ContaCorrenteResponseDTO> pix(@PathVariable Long id, @PathVariable BigDecimal valorPix, @PathVariable String chavePix) {
        ContaCorrente contaCorrente = contaCorrenteService.pix(valorPix, id, chavePix);
        return ResponseEntity.status(HttpStatus.OK).body(contaCorrenteService.toDto(contaCorrente));
    }
}
