package br.acc.banco.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import br.acc.banco.exception.CompraInvalidaException;
import br.acc.banco.exception.DepositoInvalidoException;
import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.exception.PixInvalidoException;
import br.acc.banco.exception.SaqueInvalidoException;
import br.acc.banco.exception.TransferenciaInvalidaException;
import br.acc.banco.exception.UsernameUniqueViolationException;
import br.acc.banco.models.ContaCorrente;
import br.acc.banco.models.Operacao;
import br.acc.banco.models.enums.TipoOperacao;
import br.acc.banco.repository.ContaCorrenteRepository;

@ExtendWith(MockitoExtension.class)
public class ContaCorrenteServiceTest {

    @InjectMocks
    ContaCorrenteService contaCorrenteService;

    @Mock
    ContaCorrenteRepository contaCorrenteRepository;

    @Mock
    OperacaoService operacaoService;

    ContaCorrente contaCorrente;
    Operacao operacao;

    @BeforeEach
    public void setUp() {
        contaCorrente = new ContaCorrente();
        contaCorrente.setId(1L);
        contaCorrente.setNumero(123456);
        contaCorrente.setSaldo(BigDecimal.valueOf(1000));

        operacao = new Operacao();
        operacao.setConta(contaCorrente);
        operacao.setTipo(TipoOperacao.SAQUE);
        operacao.setValor(BigDecimal.valueOf(100));
    }

    @Test
    public void testSalvarContaCorrenteComSucesso() {
        when(contaCorrenteRepository.save(any(ContaCorrente.class))).thenReturn(contaCorrente);

        ContaCorrente contaSalva = contaCorrenteService.salvar(contaCorrente);

        assertNotNull(contaSalva);
        assertEquals(contaCorrente.getNumero(), contaSalva.getNumero());
        verify(contaCorrenteRepository, times(1)).save(contaCorrente);
    }

    @Test
    public void testSalvarContaCorrenteComNumeroDuplicado() {
        when(contaCorrenteRepository.save(any(ContaCorrente.class)))
                .thenThrow(new DataIntegrityViolationException("numero já cadastrado"));

        UsernameUniqueViolationException exception = assertThrows(UsernameUniqueViolationException.class, () -> {
            contaCorrenteService.salvar(contaCorrente);
        });

        assertTrue(exception.getMessage().contains("Conta com numero: 123456 já cadastrada"));
    }

    @Test
    public void testBuscarPorIdComSucesso() {
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));

        ContaCorrente contaEncontrada = contaCorrenteService.buscarPorId(1L);

        assertNotNull(contaEncontrada);
        assertEquals(contaCorrente.getId(), contaEncontrada.getId());
        verify(contaCorrenteRepository, times(1)).findById(1L);
    }

    @Test
    public void testBuscarPorIdNaoExistente() {
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            contaCorrenteService.buscarPorId(1L);
        });

        assertTrue(exception.getMessage().contains("Conta com 1 não encontrada"));
    }

    @Test
    public void testBuscarTodasComSucesso() {
        ContaCorrente contaCorrente2 = new ContaCorrente();
        contaCorrente2.setId(2L);
        contaCorrente2.setNumero(654321);
        contaCorrente2.setSaldo(BigDecimal.valueOf(500));

        List<ContaCorrente> contas = Arrays.asList(contaCorrente, contaCorrente2);

        when(contaCorrenteRepository.findAll()).thenReturn(contas);

        List<ContaCorrente> resultado = contaCorrenteService.buscarTodas();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(contaCorrenteRepository, times(1)).findAll();
    }

    @Test
    public void testDeletarPorIdComSucesso() {
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));

        contaCorrenteService.deletarPorId(1L);

        verify(contaCorrenteRepository, times(1)).delete(contaCorrente);
    }

    @Test
    public void testDeletarPorIdNaoExistente() {
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            contaCorrenteService.deletarPorId(1L);
        });

        assertTrue(exception.getMessage().contains("Conta com 1 não encontrada"));
    }

    @Test
    public void testSacarComSucesso() {
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));

        Operacao operacao = contaCorrenteService.sacar(BigDecimal.valueOf(100), 1L);

        assertEquals(BigDecimal.valueOf(900), contaCorrente.getSaldo());
        assertEquals(TipoOperacao.SAQUE, operacao.getTipo());
        verify(operacaoService, times(1)).salvar(any(Operacao.class));
    }

    @Test
    public void testSacarComValorMaiorQueSaldo() {
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));

        SaqueInvalidoException exception = assertThrows(SaqueInvalidoException.class, () -> {
            contaCorrenteService.sacar(BigDecimal.valueOf(1100), 1L);
        });

        assertTrue(exception.getMessage().contains("O valor do saque é maior que o SALDO da conta"));
    }

    @Test
    public void testSacarComValorMenorOuIgualAZero() {
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));

        SaqueInvalidoException exceptionZero = assertThrows(SaqueInvalidoException.class, () -> {
            contaCorrenteService.sacar(BigDecimal.ZERO, 1L);
        });

        assertTrue(exceptionZero.getMessage().contains("O valor do saque não pode ser menor ou igual a zero"));

        SaqueInvalidoException exceptionNegativo = assertThrows(SaqueInvalidoException.class, () -> {
            contaCorrenteService.sacar(BigDecimal.valueOf(-100), 1L);
        });

        assertTrue(exceptionNegativo.getMessage().contains("O valor do saque não pode ser menor ou igual a zero"));
    }

    @Test
    public void testDepositoComSucesso() {
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));

        Operacao operacao = contaCorrenteService.deposito(BigDecimal.valueOf(200), 1L);

        assertEquals(BigDecimal.valueOf(1200), contaCorrente.getSaldo());
        assertEquals(TipoOperacao.DEPOSITO, operacao.getTipo());
        verify(operacaoService, times(1)).salvar(any(Operacao.class));
    }

    @Test
    public void testDepositoComValorMenorOuIgualAZero() {
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));

        DepositoInvalidoException exceptionZero = assertThrows(DepositoInvalidoException.class, () -> {
            contaCorrenteService.deposito(BigDecimal.ZERO, 1L);
        });

        assertTrue(exceptionZero.getMessage().contains("O valor do deposito não pode ser menor ou igual a zero"));

        DepositoInvalidoException exceptionNegativo = assertThrows(DepositoInvalidoException.class, () -> {
            contaCorrenteService.deposito(BigDecimal.valueOf(-100), 1L);
        });

        assertTrue(exceptionNegativo.getMessage().contains("O valor do deposito não pode ser menor ou igual a zero"));
    }

    @Test
    public void testTransferenciaComSucesso() {
        ContaCorrente contaDestino = new ContaCorrente();
        contaDestino.setId(2L);
        contaDestino.setNumero(654321);
        contaDestino.setSaldo(BigDecimal.valueOf(500));

        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));
        when(contaCorrenteRepository.findById(2L)).thenReturn(Optional.of(contaDestino));

        Operacao operacao = contaCorrenteService.transferencia(BigDecimal.valueOf(300), 1L, 2L);

        assertEquals(BigDecimal.valueOf(700), contaCorrente.getSaldo());
        assertEquals(BigDecimal.valueOf(800), contaDestino.getSaldo());
        assertEquals(TipoOperacao.TRANSFERENCIA, operacao.getTipo());
        verify(operacaoService, times(1)).salvar(any(Operacao.class));
    }

    @Test
    public void testTransferenciaComValorMenorOuIgualAZero() {
        ContaCorrente contaDestino = new ContaCorrente();
        contaDestino.setId(2L);
        contaDestino.setNumero(654321);
        contaDestino.setSaldo(BigDecimal.valueOf(500));

        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));
        when(contaCorrenteRepository.findById(2L)).thenReturn(Optional.of(contaDestino));

        TransferenciaInvalidaException exceptionZero = assertThrows(TransferenciaInvalidaException.class, () -> {
            contaCorrenteService.transferencia(BigDecimal.ZERO, 1L, 2L);
        });

        assertTrue(exceptionZero.getMessage().contains("O valor da transferencia não pode ser menor ou igual a zero"));

        TransferenciaInvalidaException exceptionNegativo = assertThrows(TransferenciaInvalidaException.class, () -> {
            contaCorrenteService.transferencia(BigDecimal.valueOf(-100), 1L, 2L);
        });

        assertTrue(exceptionNegativo.getMessage().contains("O valor da transferencia não pode ser menor ou igual a zero"));
    }

    @Test
    public void testTransferenciaComValorMaiorQueSaldo() {
        ContaCorrente contaDestino = new ContaCorrente();
        contaDestino.setId(2L);
        contaDestino.setNumero(654321);
        contaDestino.setSaldo(BigDecimal.valueOf(500));

        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));
        when(contaCorrenteRepository.findById(2L)).thenReturn(Optional.of(contaDestino));

        TransferenciaInvalidaException exception = assertThrows(TransferenciaInvalidaException.class, () -> {
            contaCorrenteService.transferencia(BigDecimal.valueOf(1100), 1L, 2L);
        });

        assertTrue(exception.getMessage().contains("O valor da transferencia é maior que o SALDO da conta"));
    }

    @Test
    public void testCompraComSucesso() {
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));

        Operacao operacao = contaCorrenteService.compra(BigDecimal.valueOf(200), 1L, "Loja X");

        assertEquals(BigDecimal.valueOf(800), contaCorrente.getSaldo());
        assertEquals(TipoOperacao.COMPRA, operacao.getTipo());
        verify(operacaoService, times(1)).salvar(any(Operacao.class));
    }

    @Test
    public void testCompraComValorMenorOuIgualAZero() {
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));

        CompraInvalidaException exceptionZero = assertThrows(CompraInvalidaException.class, () -> {
            contaCorrenteService.compra(BigDecimal.ZERO, 1L, "Loja X");
        });

        assertTrue(exceptionZero.getMessage().contains("O valor da compra não pode ser menor ou igual a zero"));

        CompraInvalidaException exceptionNegativo = assertThrows(CompraInvalidaException.class, () -> {
            contaCorrenteService.compra(BigDecimal.valueOf(-100), 1L, "Loja X");
        });

        assertTrue(exceptionNegativo.getMessage().contains("O valor da compra não pode ser menor ou igual a zero"));
    }

    @Test
    public void testCompraComValorMaiorQueSaldo() {
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));

        CompraInvalidaException exception = assertThrows(CompraInvalidaException.class, () -> {
            contaCorrenteService.compra(BigDecimal.valueOf(1100), 1L, "Loja X");
        });

        assertTrue(exception.getMessage().contains("O valor da compra é maior que o SALDO da conta"));
    }

    @Test
    public void testPixComSucesso() {
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));

        Operacao operacao = contaCorrenteService.pix(BigDecimal.valueOf(150), 1L, "chave123");

        assertEquals(BigDecimal.valueOf(850), contaCorrente.getSaldo());
        assertEquals(TipoOperacao.PIX, operacao.getTipo());
        verify(operacaoService, times(1)).salvar(any(Operacao.class));
    }

    @Test
    public void testPixComValorMenorOuIgualAZero() {
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));

        PixInvalidoException exceptionZero = assertThrows(PixInvalidoException.class, () -> {
            contaCorrenteService.pix(BigDecimal.ZERO, 1L, "chave123");
        });

        assertTrue(exceptionZero.getMessage().contains("O valor do PIX não pode ser menor ou igual a zero"));

        PixInvalidoException exceptionNegativo = assertThrows(PixInvalidoException.class, () -> {
            contaCorrenteService.pix(BigDecimal.valueOf(-100), 1L, "chave123");
        });

        assertTrue(exceptionNegativo.getMessage().contains("O valor do PIX não pode ser menor ou igual a zero"));
    }

    @Test
    public void testPixComValorMaiorQueSaldo() {
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));

        PixInvalidoException exception = assertThrows(PixInvalidoException.class, () -> {
            contaCorrenteService.pix(BigDecimal.valueOf(1100), 1L, "chave123");
        });

        assertTrue(exception.getMessage().contains("O valor do PIX é maior que o SALDO da conta"));
    }

    @Test
    public void testExibirExtrato() {
        List<Operacao> operacoes = Arrays.asList(operacao);

        when(operacaoService.buscarTodas()).thenReturn(operacoes);

        List<Operacao> extrato = contaCorrenteService.exibirExtrato(1L);

        assertNotNull(extrato);
        assertEquals(1, extrato.size());
        assertEquals(TipoOperacao.SAQUE, extrato.get(0).getTipo());
    }
    
    @Test
    public void testExibirExtratoComOperacoesNull() {
        // Configura o mock para retornar null
        when(operacaoService.buscarTodas()).thenReturn(null);

        // Chama o método e verifica o retorno
        List<Operacao> extrato = contaCorrenteService.exibirExtrato(1L);

        assertNull(extrato, "O extrato deve ser null quando não há operações");
    }

    @Test
    public void testExibirExtratoComOperacoes() {
        Operacao operacaoOutraConta = new Operacao();
        operacaoOutraConta.setConta(new ContaCorrente());
        operacaoOutraConta.getConta().setId(2L);

        List<Operacao> operacoes = Arrays.asList(operacao, operacaoOutraConta);

        // Configura o mock para retornar uma lista de operações
        when(operacaoService.buscarTodas()).thenReturn(operacoes);

        // Chama o método e verifica o retorno
        List<Operacao> extrato = contaCorrenteService.exibirExtrato(1L);

        assertNotNull(extrato, "O extrato não deve ser null quando há operações");
        assertEquals(1, extrato.size(), "Deve haver apenas uma operação no extrato");
        assertEquals(TipoOperacao.SAQUE, extrato.get(0).getTipo(), "A operação deve ser do tipo SAQUE");
        assertEquals(1L, extrato.get(0).getConta().getId(), "A operação deve pertencer à conta com ID 1");
    }

}
