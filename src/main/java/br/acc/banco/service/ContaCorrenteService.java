package br.acc.banco.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.acc.banco.exception.CompraInvalidaException;
import br.acc.banco.exception.DepositoInvalidoException;
import br.acc.banco.exception.EmprestimoInvalidoException;
import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.exception.PixInvalidoException;
import br.acc.banco.exception.SaqueInvalidoException;
import br.acc.banco.exception.TransferenciaInvalidaException;
import br.acc.banco.exception.UsernameUniqueViolationException;
import br.acc.banco.models.ContaCorrente;
import br.acc.banco.models.Emprestimo;
import br.acc.banco.models.Operacao;
import br.acc.banco.models.enums.StatusEmprestimo;
import br.acc.banco.models.enums.TipoOperacao;
import br.acc.banco.repository.ContaCorrenteRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // Injeção de depêndencia via lombok
@Service
public class ContaCorrenteService {

	private final ContaCorrenteRepository contaCorrenteRepository;
	private final OperacaoService operacaoService;
	private final EmprestimoService emprestimoService;


	public ContaCorrente salvar(ContaCorrente contaCorrente) {
		try {
			return contaCorrenteRepository.save(contaCorrente);
		}catch(DataIntegrityViolationException e) {
			throw new UsernameUniqueViolationException("Conta com numero: "+contaCorrente.getNumero()
			+" já cadastrada !");
		}
	}
	public ContaCorrente buscarPorId(Long id) {
		return contaCorrenteRepository.findById(id).orElseThrow(
				()-> new EntityNotFoundException("Conta com "+id+" não encontrada!"));

	}

	public List<ContaCorrente> buscarTodas(){
		return contaCorrenteRepository.findAll();
	}

	public void deletarPorId(Long id) {
		ContaCorrente contaCorrente = buscarPorId(id);
		contaCorrenteRepository.delete(contaCorrente);
	}

	public Operacao sacar(BigDecimal valor, Long id) {
		ContaCorrente contaCorrente = buscarPorId(id);
		if(valor.compareTo(BigDecimal.ZERO) <= 0){
			throw new SaqueInvalidoException("O valor do saque não pode ser menor ou igual a zero !");
		}
		if(valor.compareTo(contaCorrente.getSaldo()) > 0){
			throw new SaqueInvalidoException("O valor do saque é maior que o SALDO da conta !");
		}
		contaCorrente.setSaldo(contaCorrente.getSaldo().subtract(valor));

		Operacao operacao = new Operacao();
		operacao.setConta(contaCorrente);
		operacao.setTipo(TipoOperacao.SAQUE);
		operacao.setValor(valor);

		operacaoService.salvar(operacao);
		return operacao;
	}

	public Operacao deposito(BigDecimal valorDeposito, Long id) {
		ContaCorrente contaCorrente = buscarPorId(id);
		if(valorDeposito.compareTo(BigDecimal.ZERO) <= 0){
			throw new DepositoInvalidoException("O valor do deposito não pode ser menor ou igual a zero !");
		}
		contaCorrente.setSaldo(contaCorrente.getSaldo().add(valorDeposito));

		Operacao operacao = new Operacao();
		operacao.setTipo(TipoOperacao.DEPOSITO);
		operacao.setValor(valorDeposito);
		operacao.setConta(contaCorrente);
		operacaoService.salvar(operacao);

		return operacao;
	}

	public Operacao transferencia(BigDecimal valorTransferencia, Long idContaOrigem, Long idContaDestino) {
		ContaCorrente contaOrigem = buscarPorId(idContaOrigem);
		ContaCorrente contaDestino = buscarPorId(idContaDestino);
		if(valorTransferencia.compareTo(BigDecimal.ZERO) <= 0){
			throw new TransferenciaInvalidaException("O valor da transferencia não pode ser menor ou igual a zero !");
		}
		if(valorTransferencia.compareTo(contaOrigem.getSaldo()) > 0){
			throw new TransferenciaInvalidaException("O valor da transferencia é maior que o SALDO da conta !");
		}

		contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valorTransferencia));
		contaDestino.setSaldo(contaDestino.getSaldo().add(valorTransferencia));

		Operacao operacao = new Operacao();
		operacao.setTipo(TipoOperacao.TRANSFERENCIA);
		operacao.setValor(valorTransferencia);
		operacao.setConta(contaOrigem);
		operacao.setContaDestino(contaDestino);
		operacaoService.salvar(operacao);

		return operacao;
	}

	public Operacao compra(BigDecimal valorCompra, Long id, String nomeEstabelecimento) {
		ContaCorrente contaOrigem = buscarPorId(id);
		if(valorCompra.compareTo(BigDecimal.ZERO) <= 0){
			throw new CompraInvalidaException("O valor da compra não pode ser menor ou igual a zero !");
		}
		if(valorCompra.compareTo(contaOrigem.getSaldo()) > 0){
			throw new CompraInvalidaException("O valor da compra é maior que o SALDO da conta !");
		}
		contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valorCompra));

		Operacao operacao = new Operacao();
		operacao.setConta(contaOrigem);
		operacao.setTipo(TipoOperacao.COMPRA);
		operacao.setValor(valorCompra);
		operacao.setNomeEstabelecimento(nomeEstabelecimento);

		operacaoService.salvar(operacao);
		return operacao;
	}

	public Operacao pix(BigDecimal valorPix, Long id,String chavePix) {
		ContaCorrente contaOrigem = buscarPorId(id);
		if(valorPix.compareTo(BigDecimal.ZERO) <= 0){
			throw new PixInvalidoException("O valor do PIX não pode ser menor ou igual a zero !");
		}
		if(valorPix.compareTo(contaOrigem.getSaldo()) > 0){
			throw new PixInvalidoException("O valor do PIX é maior que o SALDO da conta !");
		}

		contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valorPix));

		Operacao operacao = new Operacao();
		operacao.setConta(contaOrigem);
		operacao.setTipo(TipoOperacao.PIX);
		operacao.setChavePix(chavePix);
		operacao.setValor(valorPix);

		operacaoService.salvar(operacao);
		return operacao;
	}


	public List<Operacao> exibirExtrato(Long id){
		List<Operacao> operacoes = operacaoService.buscarTodas();
		if(operacoes != null) {
			return operacoes.stream()
					.filter(operacao -> operacao.getConta().getId().equals(id))
					.collect(Collectors.toList());
		}
		return operacoes;
	}

	public Emprestimo solicitarEmprestimo(Long contaId, BigDecimal valorEmprestimo, int quantidadeParcelas) {
		if(valorEmprestimo.compareTo(BigDecimal.ZERO) <= 0){
			throw new EmprestimoInvalidoException("O valor do EMPRESTIMO não pode ser menor ou igual a zero !");
		}
		if(quantidadeParcelas <= 0) {
			throw new EmprestimoInvalidoException("A quantidade de parcelas não pode ser menor ou igual a zero!");
		}

		ContaCorrente contaCorrente = buscarPorId(contaId);
		contaCorrente.setSaldo(contaCorrente.getSaldo().add(valorEmprestimo));

		// Calcular o valor de cada parcela
		BigDecimal valorParcela = valorEmprestimo.divide(
				BigDecimal.valueOf(quantidadeParcelas),2,RoundingMode.HALF_UP);


		Emprestimo emprestimo = new Emprestimo();
		emprestimo.setValor(valorEmprestimo);
		emprestimo.setQuantidadeParcelas(quantidadeParcelas);
		emprestimo.setQuantidadeParcelasPagas(0);
		emprestimo.setValorParcela(valorParcela);
		emprestimo.setStatus(StatusEmprestimo.APROVADO);
		emprestimo.setConta(contaCorrente);


		return emprestimoService.salvar(emprestimo);
	}

	public Emprestimo pagarParcelaEmprestimo(Long contaId,Long emprestimoId ,BigDecimal pagamentoParcela) {
		ContaCorrente conta = buscarPorId(contaId);
		Emprestimo emprestimo = emprestimoService.buscarPorId(emprestimoId);
		
		if(emprestimo.getQuantidadeParcelas() == emprestimo.getQuantidadeParcelasPagas()) {
			emprestimo.setStatus(StatusEmprestimo.PAGO);
			emprestimoService.salvar(emprestimo);
			throw new EmprestimoInvalidoException("O emprestimo já está QUITADO !");	
		}
		if(conta.getSaldo().compareTo(pagamentoParcela) < 0) {
			throw new EmprestimoInvalidoException("Saldo insuficiente, verifique seu saldo!");
		}
		if(pagamentoParcela.compareTo(emprestimo.getValorParcela()) < 0
				|| pagamentoParcela.compareTo(emprestimo.getValorParcela()) > 0 ) {
			throw new EmprestimoInvalidoException("O valor da PARCELA não pode ser maior e nem menor que: R$"+emprestimo.getValorParcela());
		}

		conta.setSaldo(conta.getSaldo().subtract(pagamentoParcela));
		
		Operacao operacao = new Operacao();
		operacao.setConta(conta);
		operacao.setEmprestimo(emprestimo);
		operacao.setTipo(TipoOperacao.PARCELAEMPRESTIMO);
		operacao.setValor(pagamentoParcela);
		operacaoService.salvar(operacao);
		
		emprestimo.setQuantidadeParcelasPagas(emprestimo.getQuantidadeParcelasPagas()+1);
		return emprestimoService.salvar(emprestimo);

	}
}
