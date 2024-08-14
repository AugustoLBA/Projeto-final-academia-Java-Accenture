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
import br.acc.banco.models.Seguro;
import br.acc.banco.repository.SeguroRepository;

@ExtendWith(MockitoExtension.class)
public class SeguroServiceTest {

    @InjectMocks
    private SeguroService seguroService;

    @Mock
    private SeguroRepository seguroRepository;

    private Seguro seguro;

    @BeforeEach
    public void setUp() {
        seguro = new Seguro();
        seguro.setId(1L);
        seguro.setQuantidadeParcelas(10);
        seguro.setQuantidadeParcelasPagas(0);
        seguro.setValorParcela(BigDecimal.valueOf(100));
    }

    @Test
    public void testSalvarSeguroComSucesso() {
        when(seguroRepository.save(any(Seguro.class))).thenReturn(seguro);

        Seguro seguroSalvo = seguroService.salvar(seguro);

        assertNotNull(seguroSalvo);
        assertEquals(1L, seguroSalvo.getId());
        verify(seguroRepository, times(1)).save(seguro);
    }

    @Test
    public void testBuscarPorIdComSucesso() {
        when(seguroRepository.findById(1L)).thenReturn(Optional.of(seguro));

        Seguro seguroEncontrado = seguroService.buscarPorId(1L);

        assertNotNull(seguroEncontrado);
        assertEquals(1L, seguroEncontrado.getId());
        verify(seguroRepository, times(1)).findById(1L);
    }

    @Test
    public void testBuscarPorIdNaoExistente() {
        when(seguroRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            seguroService.buscarPorId(1L);
        });

        assertEquals("seguro com 1 não encontrado!", exception.getMessage());
        verify(seguroRepository, times(1)).findById(1L);
    }

    @Test
    public void testBuscarTodosComSucesso() {
        Seguro seguro2 = new Seguro();
        seguro2.setId(2L);
        List<Seguro> seguros = Arrays.asList(seguro, seguro2);

        when(seguroRepository.findAll()).thenReturn(seguros);

        List<Seguro> resultado = seguroService.buscarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(seguroRepository, times(1)).findAll();
    }

    @Test
    public void testDeletarPorIdComSucesso() {
        when(seguroRepository.findById(1L)).thenReturn(Optional.of(seguro));

        seguroService.deletarPorId(1L);

        verify(seguroRepository, times(1)).delete(seguro);
    }

    @Test
    public void testDeletarPorIdNaoExistente() {
        when(seguroRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            seguroService.deletarPorId(1L);
        });

        assertEquals("seguro com 1 não encontrado!", exception.getMessage());
        verify(seguroRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindByContaIdComSucesso() {
        Seguro seguro2 = new Seguro();
        seguro2.setId(2L);
        List<Seguro> seguros = Arrays.asList(seguro, seguro2);

        when(seguroRepository.findByContaId(1L)).thenReturn(seguros);

        List<Seguro> resultado = seguroService.findByContaId(1L);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(seguroRepository, times(1)).findByContaId(1L);
    }
}
