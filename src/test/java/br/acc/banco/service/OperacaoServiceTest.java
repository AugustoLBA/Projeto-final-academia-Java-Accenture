package br.acc.banco.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.models.ContaCorrente;
import br.acc.banco.models.Operacao;
import br.acc.banco.models.enums.TipoOperacao;
import br.acc.banco.repository.OperacaoRepository;

@ExtendWith(MockitoExtension.class)
public class OperacaoServiceTest {

    @InjectMocks
    OperacaoService operacaoService;

    @Mock
    OperacaoRepository operacaoRepository;

    Operacao operacao;
    ContaCorrente contaCorrente;

    @BeforeEach
    public void setUp() {
        contaCorrente = new ContaCorrente();
        contaCorrente.setId(1L);

        operacao = new Operacao();
        operacao.setId(1L);
        operacao.setConta(contaCorrente);
        operacao.setValor(BigDecimal.valueOf(100));
        operacao.setTipo(TipoOperacao.SAQUE);
        operacao.setDataRealizada(new Date());
    }

    @Test
    public void testSalvarOperacaoComSucesso() {
        when(operacaoRepository.save(any(Operacao.class))).thenReturn(operacao);

        Operacao operacaoSalva = operacaoService.salvar(operacao);

        assertNotNull(operacaoSalva);
        assertEquals(operacao.getId(), operacaoSalva.getId());
        assertEquals(operacao.getValor(), operacaoSalva.getValor());
        assertEquals(operacao.getTipo(), operacaoSalva.getTipo());
        verify(operacaoRepository, times(1)).save(operacao);
    }

    @Test
    public void testSalvarOperacaoFalha() {
        when(operacaoRepository.save(any(Operacao.class))).thenThrow(new RuntimeException("Erro ao salvar operação"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            operacaoService.salvar(operacao);
        });

        assertTrue(exception.getMessage().contains("Erro ao salvar operação"));
    }

    @Test
    public void testBuscarTodasOperacoesComSucesso() {
        Operacao outraOperacao = new Operacao();
        outraOperacao.setId(2L);
        outraOperacao.setConta(contaCorrente);
        outraOperacao.setValor(BigDecimal.valueOf(200));
        outraOperacao.setTipo(TipoOperacao.DEPOSITO);

        when(operacaoRepository.findAll()).thenReturn(Arrays.asList(operacao, outraOperacao));

        List<Operacao> operacoes = operacaoService.buscarTodas();

        assertNotNull(operacoes);
        assertEquals(2, operacoes.size());
        verify(operacaoRepository, times(1)).findAll();
    }

    @Test
    public void testBuscarTodasOperacoesVazio() {
        when(operacaoRepository.findAll()).thenReturn(Collections.emptyList());

        List<Operacao> operacoes = operacaoService.buscarTodas();

        assertNotNull(operacoes);
        assertTrue(operacoes.isEmpty(), "A lista de operações deve estar vazia");
        verify(operacaoRepository, times(1)).findAll();
    }

    @Test
    public void testBuscarPorIdComSucesso() {
        when(operacaoRepository.findById(1L)).thenReturn(Optional.of(operacao));

        Operacao operacaoEncontrada = operacaoService.buscarPorId(1L);

        assertNotNull(operacaoEncontrada);
        assertEquals(operacao.getId(), operacaoEncontrada.getId());
        verify(operacaoRepository, times(1)).findById(1L);
    }

    @Test
    public void testBuscarPorIdNaoExistente() {
        when(operacaoRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            operacaoService.buscarPorId(1L);
        });

        assertTrue(exception.getMessage().contains("Operação com 1 não encontrada"));
    }

    @Test
    public void testDeletarPorIdComSucesso() {
        when(operacaoRepository.findById(1L)).thenReturn(Optional.of(operacao));

        operacaoService.deletarPorId(1L);

        verify(operacaoRepository, times(1)).delete(operacao);
    }

    @Test
    public void testDeletarPorIdNaoExistente() {
        when(operacaoRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            operacaoService.deletarPorId(1L);
        });

        assertTrue(exception.getMessage().contains("Operação com 1 não encontrada"));
    }

    @Test
    public void testDeletarPorIdErroAoExcluir() {
        when(operacaoRepository.findById(1L)).thenReturn(Optional.of(operacao));
        doThrow(new RuntimeException("Erro ao excluir operação")).when(operacaoRepository).delete(operacao);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            operacaoService.deletarPorId(1L);
        });

        assertTrue(exception.getMessage().contains("Erro ao excluir operação"));
    }

    @Test
    public void testSalvarOperacaoComTipoInvalido() {
        operacao.setTipo(null);

        when(operacaoRepository.save(operacao)).thenThrow(new IllegalArgumentException("Tipo de operação inválido"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            operacaoService.salvar(operacao);
        });

        assertTrue(exception.getMessage().contains("Tipo de operação inválido"));
    }

    @Test
    public void testBuscarPorIdComTipoOperacaoDiferente() {
        Operacao operacaoDiferente = new Operacao();
        
        operacaoDiferente.setId(2L);
        operacaoDiferente.setConta(contaCorrente);
        operacaoDiferente.setTipo(TipoOperacao.COMPRA);
        operacaoDiferente.setValor(BigDecimal.valueOf(50));

        when(operacaoRepository.findById(2L)).thenReturn(Optional.of(operacaoDiferente));

        Operacao operacaoEncontrada = operacaoService.buscarPorId(2L);

        assertNotNull(operacaoEncontrada);
        assertEquals(operacaoDiferente.getId(), operacaoEncontrada.getId());
        assertEquals(TipoOperacao.COMPRA, operacaoEncontrada.getTipo());
        verify(operacaoRepository, times(1)).findById(2L);
    }
}
