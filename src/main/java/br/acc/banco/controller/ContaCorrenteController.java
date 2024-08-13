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
import br.acc.banco.dto.emprestimo.EmprestimoCreateDTO;
import br.acc.banco.dto.emprestimo.EmprestimoResponseDTO;
import br.acc.banco.dto.emprestimo.ParcelaEmprestimoDTO;
import br.acc.banco.dto.operacao.CompraCreateDTO;
import br.acc.banco.dto.operacao.OperacaoCreateDTO;
import br.acc.banco.dto.operacao.OperacaoResponseDTO;
import br.acc.banco.dto.operacao.PixCreateDTO;
import br.acc.banco.dto.operacao.TransferenciaCreateDTO;
import br.acc.banco.dto.seguro.ParcelaSeguroDTO;
import br.acc.banco.dto.seguro.SeguroCreateDTO;
import br.acc.banco.dto.seguro.SeguroResponseDTO;
import br.acc.banco.mapper.ContaCorrenteMapper;
import br.acc.banco.mapper.EmprestimoMapper;
import br.acc.banco.mapper.OperacaoMapper;
import br.acc.banco.mapper.SeguroMapper;
import br.acc.banco.models.ContaCorrente;
import br.acc.banco.models.Emprestimo;
import br.acc.banco.models.Operacao;
import br.acc.banco.models.Seguro;
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
    
    private final EmprestimoMapper emprestimoMapper;
    
    private final SeguroMapper seguroMapper;

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
    	Operacao operacao = contaCorrenteService.transferencia(createDTO.getValor(), createDTO.getContaCorrenteOrigem(), createDTO.getContaCorrenteDestinoNumero());
        return ResponseEntity.status(HttpStatus.OK).body(operacaoMapper.toDto(operacao));
    }

    @PostMapping("/compra")
    public ResponseEntity<OperacaoResponseDTO> compra(@Valid @RequestBody CompraCreateDTO createDTO) {
    	Operacao operacao = contaCorrenteService.compra(createDTO.getValor(), createDTO.getContaCorrenteId(), createDTO.getNomeEstabelecimento());
        return ResponseEntity.status(HttpStatus.OK).body(operacaoMapper.toDto(operacao));
    }

    @PostMapping("/pix")
    public ResponseEntity<OperacaoResponseDTO> pix(@Valid @RequestBody PixCreateDTO createDTO) {
    	Operacao operacao = contaCorrenteService.pix(createDTO.getValor(), createDTO.getChavePixOrigem(), createDTO.getChavePixDestino());
        return ResponseEntity.status(HttpStatus.OK).body(operacaoMapper.toDto(operacao));
    }
    @GetMapping("/extrato/{id}")
    public ResponseEntity<List<OperacaoResponseDTO>> exibirExtrato(@PathVariable Long id) {
        List<OperacaoResponseDTO> operacoes = operacaoMapper.toListDto(contaCorrenteService.exibirExtrato(id));
        return ResponseEntity.status(HttpStatus.OK).body(operacoes);
    }
    
    @PostMapping("/emprestimo/solicitar")
    public ResponseEntity<EmprestimoResponseDTO> solicitarEmprestimo(@Valid @RequestBody EmprestimoCreateDTO createDTO) {
        Emprestimo emprestimo = contaCorrenteService.solicitarEmprestimo(
        		createDTO.getContaCorrenteId(),
        		createDTO.getValor(),
        		createDTO.getQuantidadeParcelas());
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoMapper.toDto(emprestimo));
    }
    
    @PostMapping("/emprestimo/pagar/parcela")
    public ResponseEntity<EmprestimoResponseDTO> pagarParcelaEmprestimo(@Valid @RequestBody ParcelaEmprestimoDTO dto){
    	Emprestimo emprestimo = contaCorrenteService.pagarParcelaEmprestimo(
    			dto.getContaCorrenteId(), 
    			dto.getEmprestimoId(),
    			dto.getValor());
    	return ResponseEntity.status(HttpStatus.OK).body(emprestimoMapper.toDto(emprestimo));
    }
    
    @GetMapping("buscar/{id}")
    public ResponseEntity<ContaCorrenteResponseDTO> buscarContaPorIdCliente(@PathVariable Long id){
    	ContaCorrente conta = contaCorrenteService.buscarContaPorIdCliente(id);
    	return ResponseEntity.status(HttpStatus.OK).body(contaCorrenteMapper.toDto(conta));
    }
    @GetMapping("buscar/chavepix/{chavePix}")
    public ResponseEntity<ContaCorrenteResponseDTO> buscarContaPorChavePix(@PathVariable String chavePix){
    	ContaCorrente conta = contaCorrenteService.buscarContaPorChavePix(chavePix);
    	return ResponseEntity.status(HttpStatus.OK).body(contaCorrenteMapper.toDto(conta));
    }
    @GetMapping("buscar/numero/{numero}")
    public ResponseEntity<ContaCorrenteResponseDTO> buscarContaPorNumero(@PathVariable int numero){
    	ContaCorrente conta = contaCorrenteService.buscarContaPorNumero(numero);
    	return ResponseEntity.status(HttpStatus.OK).body(contaCorrenteMapper.toDto(conta));
    }
    @PostMapping("/seguro/pagar/parcela")
    public ResponseEntity<SeguroResponseDTO> pagarParcelaSeguro(@Valid @RequestBody ParcelaSeguroDTO dto){
        Seguro seguro = contaCorrenteService.pagarParcelaSeguro(
            dto.getContaCorrenteId(),
            dto.getSeguroID(),
            dto.getValor());
        return ResponseEntity.status(HttpStatus.OK).body(seguroMapper.toDto(seguro));
    }
    
    @PostMapping("/seguro/solicitar")
    public ResponseEntity<SeguroResponseDTO> solicitarSeguro(@Valid @RequestBody SeguroCreateDTO createDTO) {
        Seguro seguro = contaCorrenteService.solicitarSeguro(
            createDTO.getContaCorrenteId(),
            createDTO.getValor(),
            createDTO.getQuantidadeParcelas(),
            createDTO.getTipo());
        return ResponseEntity.status(HttpStatus.OK).body(seguroMapper.toDto(seguro));
    }

    @PostMapping("/seguro/cancelar/{seguroId}")
    public ResponseEntity<Void> cancelarSeguro(@PathVariable Long seguroId) {
        contaCorrenteService.cancelarSeguro(seguroId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
