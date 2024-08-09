package br.acc.banco.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.acc.banco.dto.ContaCorrenteCreateDTO;
import br.acc.banco.dto.ContaCorrenteResponseDTO;
import br.acc.banco.dto.OperacaoResponseDTO;
import br.acc.banco.mapper.ContaCorrenteMapper;
import br.acc.banco.mapper.OperacaoMapper;
import br.acc.banco.models.ContaCorrente;
import br.acc.banco.models.Operacao;
import br.acc.banco.service.ContaCorrenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/banco/contaCorrente")
public class ContaCorrenteController {

    private final ContaCorrenteService contaCorrenteService;
    
    private final ContaCorrenteMapper contaCorrenteMapper;
    
    private final OperacaoMapper operacaoMapper;

    @PostMapping
    public ResponseEntity<ContaCorrenteResponseDTO> save(@Valid @RequestBody ContaCorrenteCreateDTO createDTO) {
        ContaCorrente contaCorrente = contaCorrenteService.salvar(contaCorrenteMapper.toContaCorrente(createDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(contaCorrenteMapper.toDto(contaCorrente));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaCorrenteResponseDTO> findById(@PathVariable Long id) {
        ContaCorrente contaCorrente = contaCorrenteService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(contaCorrenteMapper.toDto(contaCorrente));
    }

    @GetMapping
    public ResponseEntity<List<ContaCorrenteResponseDTO>> findAll() {
        List<ContaCorrente> contaCorrentes = contaCorrenteService.buscarTodas();
        return ResponseEntity.status(HttpStatus.OK).body(contaCorrenteMapper.toListDto(contaCorrentes));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        contaCorrenteService.deletarPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/deposito/{id}/{valorDeposito}")
    public ResponseEntity<OperacaoResponseDTO> deposito(@PathVariable Long id, @PathVariable BigDecimal valorDeposito) {
    	Operacao operacao = contaCorrenteService.deposito(valorDeposito, id);
        return ResponseEntity.status(HttpStatus.OK).body(operacaoMapper.toDto(operacao));
    }

    @PostMapping("/saque/{id}/{valorSaque}")
    public ResponseEntity<OperacaoResponseDTO> sacar(@PathVariable Long id, @PathVariable BigDecimal valorSaque) {
        
    	Operacao operacao = contaCorrenteService.sacar(valorSaque, id);
        return ResponseEntity.status(HttpStatus.OK).body(operacaoMapper.toDto(operacao));
    }

    @PostMapping("/transferencia/{idOrigem}/{idDestino}/{valorTransferencia}")
    public ResponseEntity<OperacaoResponseDTO> transferencia(@PathVariable Long idOrigem, @PathVariable Long idDestino, @PathVariable BigDecimal valorTransferencia) {
    	Operacao operacao = contaCorrenteService.transferencia(valorTransferencia, idOrigem, idDestino);
        return ResponseEntity.status(HttpStatus.OK).body(operacaoMapper.toDto(operacao));
    }

    @PostMapping("/compra/{id}/{valorCompra}/{nomeEstabelecimento}")
    public ResponseEntity<OperacaoResponseDTO> compra(@PathVariable Long id, @PathVariable BigDecimal valorCompra, @PathVariable String nomeEstabelecimento) {
    	Operacao operacao = contaCorrenteService.compra(valorCompra, id, nomeEstabelecimento);
        return ResponseEntity.status(HttpStatus.OK).body(operacaoMapper.toDto(operacao));
    }

    @PostMapping("/pix/{id}/{valorPix}/{chavePix}")
    public ResponseEntity<OperacaoResponseDTO> pix(@PathVariable Long id, @PathVariable BigDecimal valorPix, @PathVariable String chavePix) {
    	Operacao operacao = contaCorrenteService.pix(valorPix, id, chavePix);
        return ResponseEntity.status(HttpStatus.OK).body(operacaoMapper.toDto(operacao));
    }
    @GetMapping("/extrato/{id}")
    public ResponseEntity<List<OperacaoResponseDTO>> exibirExtrato(@PathVariable Long id) {
        List<OperacaoResponseDTO> operacoes = operacaoMapper.toListDto(contaCorrenteService.exibirExtrato(id));
        return ResponseEntity.status(HttpStatus.OK).body(operacoes);
    }
}
