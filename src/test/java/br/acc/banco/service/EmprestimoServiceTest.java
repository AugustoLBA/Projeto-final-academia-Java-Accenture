package br.acc.banco.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.models.Emprestimo;
import br.acc.banco.repository.EmprestimoRepository;

@ExtendWith(MockitoExtension.class)
public class EmprestimoServiceTest {

    @InjectMocks
    private EmprestimoService emprestimoService;

    @Mock
    private EmprestimoRepository emprestimoRepository;

    private Emprestimo emprestimo;

    @BeforeEach
    public void setUp() {
        emprestimo = new Emprestimo();
        emprestimo.setId(1L);
        emprestimo.setValor(BigDecimal.valueOf(1000));
        emprestimo.setQuantidadeParcelas(10);
        emprestimo.setQuantidadeParcelasPagas(0);
        emprestimo.setValorParcela(BigDecimal.valueOf(100));
    }

    @Test
    public void testSalvarEmprestimoComSucesso() {
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimo);

        Emprestimo emprestimoSalvo = emprestimoService.salvar(emprestimo);

        assertNotNull(emprestimoSalvo);
        assertEquals(1L, emprestimoSalvo.getId());
        assertEquals(BigDecimal.valueOf(1000), emprestimoSalvo.getValor());
        verify(emprestimoRepository, times(1)).save(emprestimo);
    }

    @Test
    public void testBuscarPorIdComSucesso() {
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));

        Emprestimo emprestimoEncontrado = emprestimoService.buscarPorId(1L);

        assertNotNull(emprestimoEncontrado);
        assertEquals(1L, emprestimoEncontrado.getId());
        verify(emprestimoRepository, times(1)).findById(1L);
    }

    @Test
    public void testBuscarPorIdNaoExistente() {
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            emprestimoService.buscarPorId(1L);
        });

        assertEquals("Emprestimo com 1 não encontrado!", exception.getMessage());
        verify(emprestimoRepository, times(1)).findById(1L);
    }

    @Test
    public void testBuscarTodosComSucesso() {
        Emprestimo emprestimo2 = new Emprestimo();
        emprestimo2.setId(2L);
        List<Emprestimo> emprestimos = Arrays.asList(emprestimo, emprestimo2);

        when(emprestimoRepository.findAll()).thenReturn(emprestimos);

        List<Emprestimo> resultado = emprestimoService.buscarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(emprestimoRepository, times(1)).findAll();
    }

    @Test
    public void testDeletarPorIdComSucesso() {
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));

        emprestimoService.deletarPorId(1L);

        verify(emprestimoRepository, times(1)).delete(emprestimo);
    }

    @Test
    public void testDeletarPorIdNaoExistente() {
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            emprestimoService.deletarPorId(1L);
        });

        assertEquals("Emprestimo com 1 não encontrado!", exception.getMessage());
        verify(emprestimoRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindByContaIdComSucesso() {
        Emprestimo emprestimo2 = new Emprestimo();
        emprestimo2.setId(2L);
        List<Emprestimo> emprestimos = Arrays.asList(emprestimo, emprestimo2);

        when(emprestimoRepository.findByContaId(1L)).thenReturn(emprestimos);

        List<Emprestimo> resultado = emprestimoService.findByContaId(1L);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(emprestimoRepository, times(1)).findByContaId(1L);
    }
}
