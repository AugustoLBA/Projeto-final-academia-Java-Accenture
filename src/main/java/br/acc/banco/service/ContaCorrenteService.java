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
import br.acc.banco.exception.SeguroInvalidoException;
import br.acc.banco.exception.TransferenciaInvalidaException;
import br.acc.banco.exception.UsernameUniqueViolationException;
import br.acc.banco.models.ContaCorrente;
import br.acc.banco.models.Emprestimo;
import br.acc.banco.models.Operacao;
import br.acc.banco.models.Seguro;
import br.acc.banco.models.enums.StatusEmprestimo;
import br.acc.banco.models.enums.StatusSeguro;
import br.acc.banco.models.enums.TipoOperacao;
import br.acc.banco.models.enums.TipoSeguro;
import br.acc.banco.repository.ContaCorrenteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // Injeção de depêndencia via lombok
@Service
public class ContaCorrenteService {

	private final ContaCorrenteRepository contaCorrenteRepository;
	private final OperacaoService operacaoService;
	private final EmprestimoService emprestimoService;
	private final SeguroService seguroService;

	public ContaCorrente salvar(ContaCorrente contaCorrente) {
		try {
			return contaCorrenteRepository.save(contaCorrente);
		}catch(DataIntegrityViolationException e) {
			//UKhtia8msh3e6tpyr0xgwl8lb98' mensagem do numero da conta duplicado
			//UK9ftydt3tld3sxvk87s3shfjwn' mesnagem de cliente já possui conta cadastrada
			String aux = e.getMostSpecificCause().getMessage();
			String message;
			if(aux.contains("UK9ftydt3tld3sxvk87s3shfjwn'")) {
				message = "Cliente com ID: "+contaCorrente.getCliente().getId()+" já possui uma conta !";
			}
			else if(aux.contains("UKhtia8msh3e6tpyr0xgwl8lb98'")) {
				message = "Conta com numero: "+contaCorrente.getNumero()+" já cadastrada !";
			}else {
				message = "Erro ao salvar a conta!";
			}
			throw new UsernameUniqueViolationException(message);
		}
	}
	public ContaCorrente buscarPorId(Long id) {
		return contaCorrenteRepository.findById(id).orElseThrow(
				()-> new EntityNotFoundException("Conta com ID: "+id+" não encontrada!"));

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

	public Operacao transferencia(BigDecimal valorTransferencia, int numeroContaOrigem, int numeroContaDestino) {
		if(valorTransferencia.compareTo(BigDecimal.ZERO) <= 0){
			throw new TransferenciaInvalidaException("O valor da transferencia não pode ser menor ou igual a zero !");
		}
		ContaCorrente contaOrigem = buscarContaPorNumero(numeroContaOrigem);
		if(valorTransferencia.compareTo(contaOrigem.getSaldo()) > 0){
			throw new TransferenciaInvalidaException("O valor da transferencia é maior que o SALDO da conta !");
		}

		ContaCorrente contaDestino = buscarContaPorNumero(numeroContaDestino);

		contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valorTransferencia));
		contaDestino.setSaldo(contaDestino.getSaldo().add(valorTransferencia));

		Operacao operacaoContaOrigem = new Operacao();
		operacaoContaOrigem.setTipo(TipoOperacao.TRANSFERENCIA);
		operacaoContaOrigem.setValor(valorTransferencia);
		operacaoContaOrigem.setConta(contaOrigem);
		operacaoContaOrigem.setContaDestino(contaDestino);

		Operacao operacaoContaDestino = new Operacao();
		operacaoContaDestino.setTipo(TipoOperacao.RECEBEU_TRANSFERENCIA);
		operacaoContaDestino.setValor(valorTransferencia);
		operacaoContaDestino.setConta(contaDestino);
		operacaoContaDestino.setContaDestino(contaOrigem);

		operacaoService.salvar(operacaoContaOrigem);
		operacaoService.salvar(operacaoContaDestino);

		return operacaoContaOrigem;
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

	public Operacao pix(BigDecimal valorPix, String chavePixOrigem,String chavePixDestino) {
		if(valorPix.compareTo(BigDecimal.ZERO) <= 0){
			throw new PixInvalidoException("O valor do PIX nao pode ser menor ou igual a zero !");
		}
		ContaCorrente contaOrigem = buscarContaPorChavePix(chavePixOrigem);
		if(valorPix.compareTo(contaOrigem.getSaldo()) > 0){
			throw new PixInvalidoException("O valor do PIX eh maior que o SALDO da conta !");
		}
		ContaCorrente contaDestino = buscarContaPorChavePix(chavePixDestino);

		contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valorPix));
		contaDestino.setSaldo(contaDestino.getSaldo().add(valorPix));

		Operacao operacaoContaOrigem = new Operacao();
		operacaoContaOrigem.setConta(contaOrigem);
		operacaoContaOrigem.setTipo(TipoOperacao.PIX);
		operacaoContaOrigem.setChavePix(chavePixOrigem);
		operacaoContaOrigem.setContaDestino(contaDestino);
		operacaoContaOrigem.setValor(valorPix);

		Operacao operacaoContaDestino = new Operacao();
		operacaoContaDestino.setConta(contaDestino);
		operacaoContaDestino.setTipo(TipoOperacao.RECEBEU_PIX);
		operacaoContaDestino.setChavePix(chavePixDestino);
		operacaoContaDestino.setContaDestino(contaOrigem);
		operacaoContaDestino.setValor(valorPix);

		operacaoService.salvar(operacaoContaOrigem);
		operacaoService.salvar(operacaoContaDestino);

		return operacaoContaOrigem;
	}


	public List<Operacao> exibirExtrato(Long id){
		buscarPorId(id); // verifica se a conta existe
		List<Operacao> operacoes = operacaoService.buscarTodas();
		if(operacoes != null) {
			return operacoes.stream()
					.filter(operacao -> operacao.getConta().getId().equals(id))
					.collect(Collectors.toList());
		}
		return operacoes;
	}

	@Transactional
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
		
		Operacao operacao = new Operacao();
		operacao.setConta(contaCorrente);
		operacao.setValor(valorEmprestimo);
		operacao.setTipo(TipoOperacao.FEZ_EMPRESTIMO);
		operacaoService.salvar(operacao);


		return emprestimoService.salvar(emprestimo);
	}

	@Transactional
	public Emprestimo pagarParcelaEmprestimo(Long contaId,Long emprestimoId ,BigDecimal pagamentoParcela) {
		ContaCorrente conta = buscarPorId(contaId);
		Emprestimo emprestimo = emprestimoService.buscarPorId(emprestimoId);

		if(emprestimo.getQuantidadeParcelas() == emprestimo.getQuantidadeParcelasPagas()) {
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
		operacao.setTipo(TipoOperacao.PARCELA_EMPRESTIMO);
		operacao.setValor(pagamentoParcela);
		operacaoService.salvar(operacao);

		emprestimo.setQuantidadeParcelasPagas(emprestimo.getQuantidadeParcelasPagas()+1);
		if(emprestimo.getQuantidadeParcelas() == emprestimo.getQuantidadeParcelasPagas()) {
			emprestimo.setStatus(StatusEmprestimo.PAGO);	
			emprestimoService.salvar(emprestimo);
		}
		
		return emprestimoService.salvar(emprestimo);

	}

	public ContaCorrente buscarContaPorIdCliente(Long id) {
		return contaCorrenteRepository.findByClienteId(id).
				orElseThrow(() -> new EntityNotFoundException("Conta não encontrada para cliente com ID:"+id));
	}

	public ContaCorrente buscarContaPorChavePix(String chavePix) {
		return contaCorrenteRepository.findByChavePix(chavePix).
				orElseThrow(() ->  new EntityNotFoundException("Conta não encontrada para chave Pix: "+chavePix));
	}

	public ContaCorrente buscarContaPorNumero(int numero) {
		return contaCorrenteRepository.findByNumero(numero).
				orElseThrow(() -> new EntityNotFoundException("Conta com número: "+numero+" não encontrada !"));
	}

	@Transactional
	public Seguro solicitarSeguro(Long contaId, BigDecimal valorSeguro, int quantidadeParcelas, TipoSeguro tipo) {
		if (valorSeguro.compareTo(BigDecimal.ZERO) <= 0) {
			throw new SeguroInvalidoException("O valor do seguro não pode ser menor ou igual a zero!");
		}
		if (quantidadeParcelas <= 0) {
			throw new SeguroInvalidoException("A quantidade de parcelas não pode ser menor ou igual a zero!");
		}

		ContaCorrente contaCorrente = buscarPorId(contaId);

		BigDecimal valorParcela = valorSeguro.divide(
				BigDecimal.valueOf(quantidadeParcelas), 2, RoundingMode.HALF_UP);

		Seguro seguro = new Seguro();
		seguro.setValor(valorSeguro);
		seguro.setQuantidadeParcelas(quantidadeParcelas);
		seguro.setQuantidadeParcelasPagas(0);
		seguro.setValorParcela(valorParcela);
		seguro.setStatus(StatusSeguro.ATIVO);
		seguro.setTipo(tipo);
		seguro.setConta(contaCorrente);

		return seguroService.salvar(seguro);
	}


	@Transactional
	public Seguro pagarParcelaSeguro(Long contaId, Long seguroId, BigDecimal valorParcela) {
		Seguro seguro = seguroService.buscarPorId(seguroId);

		if (seguro.getQuantidadeParcelasPagas() == seguro.getQuantidadeParcelas()) {
			throw new SeguroInvalidoException("Todas as parcelas do seguro já foram pagas!");
		}
		if(seguro.getStatus().equals(StatusSeguro.CANCELADO)) {
			throw new SeguroInvalidoException("O seguro foi cancelado !");
		}

		ContaCorrente conta = buscarPorId(contaId);
		if (conta.getSaldo().compareTo(valorParcela) < 0) {
			throw new SeguroInvalidoException("Saldo insuficiente para pagar a parcela do seguro!");
		}
		if(valorParcela.compareTo(seguro.getValorParcela()) < 0
				|| valorParcela.compareTo(seguro.getValorParcela()) > 0 ) {
			throw new SeguroInvalidoException("O valor da PARCELA não pode ser maior e nem menor que: R$"+seguro.getValorParcela());
		}

		conta.setSaldo(conta.getSaldo().subtract(valorParcela));

		seguro.setQuantidadeParcelasPagas(seguro.getQuantidadeParcelasPagas() + 1);
	
		Operacao operacao = new  Operacao();
		operacao.setSeguro(seguro);
		operacao.setTipo(TipoOperacao.PARCELA_SEGURO);
		operacao.setValor(valorParcela);
		operacao.setConta(conta);
		operacaoService.salvar(operacao);
		
		if (seguro.getQuantidadeParcelasPagas() == seguro.getQuantidadeParcelas()) {
			seguro.setStatus(StatusSeguro.RESGATADO);
			conta.setSaldo(conta.getSaldo().add(seguro.getValor()));
			
			Operacao operacaoResgate = new Operacao();
			operacaoResgate.setConta(conta);
			operacaoResgate.setValor(seguro.getValor());
			operacaoResgate.setTipo(TipoOperacao.RESGATOU_SEGURO);
			
			operacaoService.salvar(operacaoResgate);
		}


		return seguroService.salvar(seguro);
		
	}

	@Transactional
	public void cancelarSeguro(Long seguroId) {
		Seguro seguro = seguroService.buscarPorId(seguroId);
		if (seguro.getStatus() == StatusSeguro.RESGATADO || seguro.getStatus() == StatusSeguro.CANCELADO) {
			throw new SeguroInvalidoException("Este seguro já foi resgatado ou cancelado!");
		}

		seguro.setStatus(StatusSeguro.CANCELADO);
		 seguroService.salvar(seguro);
	}
	
	public List<Seguro> buscarSegurosDaConta(Long id){
		// verifica se a conta existe
		return seguroService.findByContaId(buscarPorId(id).getId());
	}
	
	public List<Emprestimo> buscarEmprestimosDaConta(Long id){
		// verifica se a conta existe
		return emprestimoService.findByContaId(buscarPorId(id).getId());
	}

}
