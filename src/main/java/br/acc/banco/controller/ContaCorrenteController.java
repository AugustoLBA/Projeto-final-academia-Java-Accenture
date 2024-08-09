package br.acc.banco.controller;

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

import br.acc.banco.dto.contaCorrente.ContaCorrenteCreateDTO;
import br.acc.banco.dto.contaCorrente.ContaCorrenteResponseDTO;
import br.acc.banco.dto.operacao.CompraCreateDTO;
import br.acc.banco.dto.operacao.OperacaoCreateDTO;
import br.acc.banco.dto.operacao.OperacaoResponseDTO;
import br.acc.banco.dto.operacao.PixCreateDTO;
import br.acc.banco.dto.operacao.TransferenciaCreateDTO;
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

    @PostMapping("/deposito")
    public ResponseEntity<OperacaoResponseDTO> deposito(@Valid @RequestBody OperacaoCreateDTO createDTO) {
    	Operacao operacao = contaCorrenteService.deposito(createDTO.getValor(), createDTO.getContaCorrenteId());
        return ResponseEntity.status(HttpStatus.OK).body(operacaoMapper.toDto(operacao));
    }

    @PostMapping("/saque")
    public ResponseEntity<OperacaoResponseDTO> sacar(@Valid @RequestBody OperacaoCreateDTO createDTO) {
        
    	Operacao operacao = contaCorrenteService.sacar(createDTO.getValor(), createDTO.getContaCorrenteId());
        return ResponseEntity.status(HttpStatus.OK).body(operacaoMapper.toDto(operacao));
    }

    @PostMapping("/transferencia")
    public ResponseEntity<OperacaoResponseDTO> transferencia(@Valid @RequestBody TransferenciaCreateDTO createDTO) {
    	Operacao operacao = contaCorrenteService.transferencia(createDTO.getValor(), createDTO.getContaCorrenteId(), createDTO.getContaCorrenteDestinoId());
        return ResponseEntity.status(HttpStatus.OK).body(operacaoMapper.toDto(operacao));
    }

    @PostMapping("/compra")
    public ResponseEntity<OperacaoResponseDTO> compra(@Valid @RequestBody CompraCreateDTO createDTO) {
    	Operacao operacao = contaCorrenteService.compra(createDTO.getValor(), createDTO.getContaCorrenteId(), createDTO.getNomeEstabelecimento());
        return ResponseEntity.status(HttpStatus.OK).body(operacaoMapper.toDto(operacao));
    }

    @PostMapping("/pix")
    public ResponseEntity<OperacaoResponseDTO> pix(@Valid @RequestBody PixCreateDTO createDTO) {
    	Operacao operacao = contaCorrenteService.pix(createDTO.getValor(), createDTO.getContaCorrenteId(), createDTO.getChavePix());
        return ResponseEntity.status(HttpStatus.OK).body(operacaoMapper.toDto(operacao));
    }
    @GetMapping("/extrato/{id}")
    public ResponseEntity<List<OperacaoResponseDTO>> exibirExtrato(@PathVariable Long id) {
        List<OperacaoResponseDTO> operacoes = operacaoMapper.toListDto(contaCorrenteService.exibirExtrato(id));
        return ResponseEntity.status(HttpStatus.OK).body(operacoes);
    }
}
