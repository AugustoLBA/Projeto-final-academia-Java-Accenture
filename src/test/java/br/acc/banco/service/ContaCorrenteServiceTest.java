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
import br.acc.banco.exception.EmprestimoInvalidoException;
import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.exception.PixInvalidoException;
import br.acc.banco.exception.SaqueInvalidoException;
import br.acc.banco.exception.SeguroInvalidoException;
import br.acc.banco.exception.TransferenciaInvalidaException;
import br.acc.banco.exception.UsernameUniqueViolationException;
import br.acc.banco.models.Cliente;
import br.acc.banco.models.ContaCorrente;
import br.acc.banco.models.Emprestimo;
import br.acc.banco.models.Operacao;
import br.acc.banco.models.Seguro;
import br.acc.banco.models.enums.StatusEmprestimo;
import br.acc.banco.models.enums.StatusSeguro;
import br.acc.banco.models.enums.TipoOperacao;
import br.acc.banco.models.enums.TipoSeguro;
import br.acc.banco.repository.ContaCorrenteRepository;

@ExtendWith(MockitoExtension.class)
public class ContaCorrenteServiceTest {

    @InjectMocks
    ContaCorrenteService contaCorrenteService;

    @Mock
    ContaCorrenteRepository contaCorrenteRepository;

    @Mock
    OperacaoService operacaoService;

    @Mock
    EmprestimoService emprestimoService;

    @Mock
    SeguroService seguroService;

    ContaCorrente contaCorrente;
    Operacao operacao;
    Cliente cliente;

    @BeforeEach
    public void setUp() {
        cliente = new Cliente(1L, "Cliente Teste", null, null, null, contaCorrente);

        contaCorrente = new ContaCorrente();
        contaCorrente.setId(1L);
        contaCorrente.setNumero(123456);
        contaCorrente.setSaldo(BigDecimal.valueOf(1000));
        contaCorrente.setCliente(cliente);

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
        String sqlMessage = "UKhtia8msh3e6tpyr0xgwl8lb98'";
        when(contaCorrenteRepository.save(any(ContaCorrente.class)))
                .thenThrow(new DataIntegrityViolationException(sqlMessage));

        UsernameUniqueViolationException exception = assertThrows(UsernameUniqueViolationException.class, () -> {
            contaCorrenteService.salvar(contaCorrente);
        });

        assertTrue(exception.getMessage().contains("Conta com numero: 123456 já cadastrada"));
    }

    @Test
    public void testSalvarContaCorrenteComClienteDuplicado() {
        String sqlMessage = "UK9ftydt3tld3sxvk87s3shfjwn'";
        when(contaCorrenteRepository.save(any(ContaCorrente.class)))
                .thenThrow(new DataIntegrityViolationException(sqlMessage));

        UsernameUniqueViolationException exception = assertThrows(UsernameUniqueViolationException.class, () -> {
            contaCorrenteService.salvar(contaCorrente);
        });

        assertTrue(exception.getMessage().contains("Cliente com ID: 1 já possui uma conta !"));
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

        assertTrue(exception.getMessage().contains("Conta com ID: 1 não encontrada"));
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

        assertTrue(exception.getMessage().contains("Conta com ID: 1 não encontrada"));
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

        when(contaCorrenteRepository.findByNumero(123456)).thenReturn(Optional.of(contaCorrente));
        when(contaCorrenteRepository.findByNumero(654321)).thenReturn(Optional.of(contaDestino));

        Operacao operacao = contaCorrenteService.transferencia(BigDecimal.valueOf(300), 123456, 654321);

        assertEquals(BigDecimal.valueOf(700), contaCorrente.getSaldo());
        assertEquals(BigDecimal.valueOf(800), contaDestino.getSaldo());
        assertEquals(TipoOperacao.TRANSFERENCIA, operacao.getTipo());
        
    }

    @Test
    public void testTransferenciaComValorMenorOuIgualAZero() {
        ContaCorrente contaDestino = new ContaCorrente();
        contaDestino.setId(2L);
        contaDestino.setNumero(654321);
        contaDestino.setSaldo(BigDecimal.valueOf(500));

        

        TransferenciaInvalidaException exceptionZero = assertThrows(TransferenciaInvalidaException.class, () -> {
            contaCorrenteService.transferencia(BigDecimal.ZERO, 123456, 654321);
        });

        assertTrue(exceptionZero.getMessage().contains("O valor da transferencia não pode ser menor ou igual a zero"));

        TransferenciaInvalidaException exceptionNegativo = assertThrows(TransferenciaInvalidaException.class, () -> {
            contaCorrenteService.transferencia(BigDecimal.valueOf(-100), 123456, 654321);
        });

        assertTrue(exceptionNegativo.getMessage().contains("O valor da transferencia não pode ser menor ou igual a zero"));
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
    public void testPixComValorMaiorQueSaldo() {
        // Configura o saldo inicial da conta de origem
        contaCorrente.setSaldo(BigDecimal.valueOf(1000)); // Saldo menor que o valor do PIX
        contaCorrente.setChavePix("chaveOrigem"); // Certifique-se de que a chave está configurada

        // Configura a conta de destino
        ContaCorrente contaDestino = new ContaCorrente();
        contaDestino.setId(2L);
        contaDestino.setNumero(654321);
        contaDestino.setSaldo(BigDecimal.valueOf(500));
        contaDestino.setChavePix("chaveDestino");

        // Configura os mocks para retornar as contas de origem e destino
        when(contaCorrenteRepository.findByChavePix("chaveOrigem")).thenReturn(Optional.of(contaCorrente));
        

        // Verifica se a exceção é lançada ao tentar realizar o PIX com valor maior que o saldo
        PixInvalidoException exception = assertThrows(PixInvalidoException.class, () -> {
            contaCorrenteService.pix(BigDecimal.valueOf(1100), "chaveOrigem", "chaveDestino");
        });

        // Verifica se a mensagem da exceção está correta
        assertTrue(exception.getMessage().contains("O valor do PIX eh maior que o SALDO da conta !"));
    }



    @Test
    public void testExibirExtratoComContaNaoExistente() {
        // Configura o mock para lançar uma exceção quando a conta não for encontrada
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.empty());

        // Verifica se a exceção EntityNotFoundException é lançada
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            contaCorrenteService.exibirExtrato(1L);
        });

        assertTrue(exception.getMessage().contains("Conta com ID: 1 não encontrada!"));
    }






    @Test
    public void testSolicitarEmprestimoComSucesso() {
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));
        when(emprestimoService.salvar(any(Emprestimo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Emprestimo emprestimo = contaCorrenteService.solicitarEmprestimo(1L, BigDecimal.valueOf(5000), 10);

        assertNotNull(emprestimo);
        assertEquals(BigDecimal.valueOf(5000), emprestimo.getValor());
        assertEquals(10, emprestimo.getQuantidadeParcelas());
        assertEquals(0, emprestimo.getQuantidadeParcelasPagas());
        assertEquals(StatusEmprestimo.APROVADO, emprestimo.getStatus());
        verify(emprestimoService, times(1)).salvar(any(Emprestimo.class));
    }




    @Test
    public void testPagarParcelaEmprestimoComSucesso() {
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setId(1L);
        emprestimo.setQuantidadeParcelas(10);
        emprestimo.setQuantidadeParcelasPagas(5);
        emprestimo.setValorParcela(BigDecimal.valueOf(500));

        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));
        when(emprestimoService.buscarPorId(1L)).thenReturn(emprestimo);
        when(emprestimoService.salvar(any(Emprestimo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Emprestimo emprestimoAtualizado = contaCorrenteService.pagarParcelaEmprestimo(1L, 1L, BigDecimal.valueOf(500));

        assertNotNull(emprestimoAtualizado);
        assertEquals(6, emprestimoAtualizado.getQuantidadeParcelasPagas());
        verify(emprestimoService, times(1)).salvar(any(Emprestimo.class));
    }

    @Test
    public void testPagarParcelaEmprestimoJaQuitado() {
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setId(1L);
        emprestimo.setQuantidadeParcelas(10);
        emprestimo.setQuantidadeParcelasPagas(10);

        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));
        when(emprestimoService.buscarPorId(1L)).thenReturn(emprestimo);
       

        EmprestimoInvalidoException exception = assertThrows(EmprestimoInvalidoException.class, () -> {
            contaCorrenteService.pagarParcelaEmprestimo(1L, 1L, BigDecimal.valueOf(500));
        });

        assertTrue(exception.getMessage().contains("O emprestimo já está QUITADO"));
    }

    @Test
    public void testPagarParcelaEmprestimoSaldoInsuficiente() {
        contaCorrente.setSaldo(BigDecimal.valueOf(400));

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setId(1L);
        emprestimo.setQuantidadeParcelas(10);
        emprestimo.setQuantidadeParcelasPagas(5);
        emprestimo.setValorParcela(BigDecimal.valueOf(500));

        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));
        when(emprestimoService.buscarPorId(1L)).thenReturn(emprestimo);

        EmprestimoInvalidoException exception = assertThrows(EmprestimoInvalidoException.class, () -> {
            contaCorrenteService.pagarParcelaEmprestimo(1L, 1L, BigDecimal.valueOf(500));
        });

        assertTrue(exception.getMessage().contains("Saldo insuficiente, verifique seu saldo"));
    }

    @Test
    public void testPagarParcelaEmprestimoValorDiferente() {
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setId(1L);
        emprestimo.setQuantidadeParcelas(10);
        emprestimo.setQuantidadeParcelasPagas(5);
        emprestimo.setValorParcela(BigDecimal.valueOf(500));

        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));
        when(emprestimoService.buscarPorId(1L)).thenReturn(emprestimo);

        EmprestimoInvalidoException exception = assertThrows(EmprestimoInvalidoException.class, () -> {
            contaCorrenteService.pagarParcelaEmprestimo(1L, 1L, BigDecimal.valueOf(600));
        });

        assertTrue(exception.getMessage().contains("O valor da PARCELA não pode ser maior e nem menor que"));
    }

    @Test
    public void testSolicitarSeguroComSucesso() {
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));
        when(seguroService.salvar(any(Seguro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Seguro seguro = contaCorrenteService.solicitarSeguro(1L, BigDecimal.valueOf(1000), 12, TipoSeguro.VIDA);

        assertNotNull(seguro);
        assertEquals(BigDecimal.valueOf(1000), seguro.getValor());
        assertEquals(12, seguro.getQuantidadeParcelas());
        assertEquals(StatusSeguro.ATIVO, seguro.getStatus());
        assertEquals(TipoSeguro.VIDA, seguro.getTipo());
        verify(seguroService, times(1)).salvar(any(Seguro.class));
    }

   




    @Test
    public void testPagarParcelaSeguroCancelado() {
        Seguro seguro = new Seguro();
        seguro.setId(1L);
        seguro.setQuantidadeParcelas(12);
        seguro.setQuantidadeParcelasPagas(5);
        seguro.setStatus(StatusSeguro.CANCELADO);

        when(seguroService.buscarPorId(1L)).thenReturn(seguro);

        SeguroInvalidoException exception = assertThrows(SeguroInvalidoException.class, () -> {
            contaCorrenteService.pagarParcelaSeguro(1L, 1L, BigDecimal.valueOf(100));
        });

        assertTrue(exception.getMessage().contains("O seguro foi cancelado"));
    }



    @Test
    public void testCancelarSeguroComSucesso() {
        Seguro seguro = new Seguro();
        seguro.setId(1L);
        seguro.setStatus(StatusSeguro.ATIVO);

        when(seguroService.buscarPorId(1L)).thenReturn(seguro);
        when(seguroService.salvar(any(Seguro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        contaCorrenteService.cancelarSeguro(1L);
        Seguro seguroCancelado = seguroService.buscarPorId(1L);
        
        
        
        
        
        

        assertNotNull(seguroCancelado);
        assertEquals(StatusSeguro.CANCELADO, seguroCancelado.getStatus());
        verify(seguroService, times(1)).salvar(any(Seguro.class));
    }

    @Test
    public void testCancelarSeguroJaResgatadoOuCancelado() {
        Seguro seguroPago = new Seguro();
        seguroPago.setId(1L);
        seguroPago.setStatus(StatusSeguro.RESGATADO);

        when(seguroService.buscarPorId(1L)).thenReturn(seguroPago);

        SeguroInvalidoException exceptionPago = assertThrows(SeguroInvalidoException.class, () -> {
            contaCorrenteService.cancelarSeguro(1L);
        });

        assertTrue(exceptionPago.getMessage().contains("Este seguro já foi resgatado ou cancelado"));

        Seguro seguroCancelado = new Seguro();
        seguroCancelado.setId(1L);
        seguroCancelado.setStatus(StatusSeguro.CANCELADO);

        when(seguroService.buscarPorId(1L)).thenReturn(seguroCancelado);

        SeguroInvalidoException exceptionCancelado = assertThrows(SeguroInvalidoException.class, () -> {
            contaCorrenteService.cancelarSeguro(1L);
        });

        assertTrue(exceptionCancelado.getMessage().contains("Este seguro já foi resgatado ou cancelado"));
    }

    @Test
    public void testBuscarContaPorIdClienteComSucesso() {
        when(contaCorrenteRepository.findByClienteId(1L)).thenReturn(Optional.of(contaCorrente));

        ContaCorrente contaEncontrada = contaCorrenteService.buscarContaPorIdCliente(1L);

        assertNotNull(contaEncontrada);
        assertEquals(1L, contaEncontrada.getId());
        verify(contaCorrenteRepository, times(1)).findByClienteId(1L);
    }

    @Test
    public void testBuscarContaPorIdClienteNaoExistente() {
        when(contaCorrenteRepository.findByClienteId(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            contaCorrenteService.buscarContaPorIdCliente(1L);
        });

        assertTrue(exception.getMessage().contains("Conta não encontrada para cliente com ID:1"));
    }

    @Test
    public void testBuscarContaPorChavePixComSucesso() {
        when(contaCorrenteRepository.findByChavePix("chavePix")).thenReturn(Optional.of(contaCorrente));

        ContaCorrente contaEncontrada = contaCorrenteService.buscarContaPorChavePix("chavePix");

        assertNotNull(contaEncontrada);
        assertEquals(1L, contaEncontrada.getId());
        verify(contaCorrenteRepository, times(1)).findByChavePix("chavePix");
    }

    @Test
    public void testBuscarContaPorChavePixNaoExistente() {
        when(contaCorrenteRepository.findByChavePix("chavePix")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            contaCorrenteService.buscarContaPorChavePix("chavePix");
        });

        assertTrue(exception.getMessage().contains("Conta não encontrada para chave Pix: chavePix"));
    }

    @Test
    public void testBuscarContaPorNumeroComSucesso() {
        when(contaCorrenteRepository.findByNumero(123456)).thenReturn(Optional.of(contaCorrente));

        ContaCorrente contaEncontrada = contaCorrenteService.buscarContaPorNumero(123456);

        assertNotNull(contaEncontrada);
        assertEquals(1L, contaEncontrada.getId());
        verify(contaCorrenteRepository, times(1)).findByNumero(123456);
    }

    @Test
    public void testBuscarContaPorNumeroNaoExistente() {
        when(contaCorrenteRepository.findByNumero(123456)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            contaCorrenteService.buscarContaPorNumero(123456);
        });

        assertTrue(exception.getMessage().contains("Conta com número: 123456 não encontrada"));
    }

    @Test
    public void testBuscarSegurosDaConta() {
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));
        when(seguroService.findByContaId(1L)).thenReturn(Arrays.asList(new Seguro(), new Seguro()));

        List<Seguro> seguros = contaCorrenteService.buscarSegurosDaConta(1L);

        assertNotNull(seguros);
        assertEquals(2, seguros.size());
        verify(seguroService, times(1)).findByContaId(1L);
    }

    @Test
    public void testBuscarEmprestimosDaConta() {
        when(contaCorrenteRepository.findById(1L)).thenReturn(Optional.of(contaCorrente));
        when(emprestimoService.findByContaId(1L)).thenReturn(Arrays.asList(new Emprestimo(), new Emprestimo()));

        List<Emprestimo> emprestimos = contaCorrenteService.buscarEmprestimosDaConta(1L);

        assertNotNull(emprestimos);
        assertEquals(2, emprestimos.size());
        verify(emprestimoService, times(1)).findByContaId(1L);
    }
}
