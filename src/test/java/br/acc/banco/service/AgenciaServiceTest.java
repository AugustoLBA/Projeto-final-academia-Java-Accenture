package br.acc.banco.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.exception.UsernameUniqueViolationException;
import br.acc.banco.models.Agencia;
import br.acc.banco.repository.AgenciaRepository;

@ExtendWith(MockitoExtension.class)
public class AgenciaServiceTest {

    @InjectMocks
    AgenciaService agenciaService;

    @Mock
    AgenciaRepository agenciaRepository;

    Agencia agencia;

    @BeforeEach
    public void setUp() {
        agencia = new Agencia();
        agencia.setId(1L);
        agencia.setNome("Agência Central");
        agencia.setTelefone("11987654321");
        agencia.setEndereco("Rua Principal, 123");
    }

    @Test
    public void testSalvarAgenciaComSucesso() {
        when(agenciaRepository.save(any(Agencia.class))).thenReturn(agencia);

        Agencia agenciaSalva = agenciaService.salvar(agencia);

        assertNotNull(agenciaSalva);
        assertEquals(agencia.getNome(), agenciaSalva.getNome());
        verify(agenciaRepository, times(1)).save(agencia);
    }

    @Test
    public void testSalvarAgenciaComNomeDuplicado() {
        when(agenciaRepository.save(any(Agencia.class)))
                .thenThrow(new DataIntegrityViolationException("nome já cadastrado"));

        UsernameUniqueViolationException exception = assertThrows(UsernameUniqueViolationException.class, () -> {
            agenciaService.salvar(agencia);
        });

        assertTrue(exception.getMessage().contains("Agencia com nome: Agência Central já cadastrado"));
    }

    @Test
    public void testBuscarPorIdComSucesso() {
        when(agenciaRepository.findById(1L)).thenReturn(Optional.of(agencia));

        Agencia agenciaEncontrada = agenciaService.buscarPorId(1L);

        assertNotNull(agenciaEncontrada);
        assertEquals(agencia.getId(), agenciaEncontrada.getId());
        verify(agenciaRepository, times(1)).findById(1L);
    }

    @Test
    public void testBuscarPorIdNaoExistente() {
        when(agenciaRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            agenciaService.buscarPorId(1L);
        });

        assertTrue(exception.getMessage().contains("Agência com 1 não encontrada"));
    }

    @Test
    public void testBuscarTodos() {
        List<Agencia> agencias = Arrays.asList(
                agencia,
                new Agencia()
        );
        
        agencias.get(1).setId(2L);
        agencias.get(1).setNome("Agência Secundária");
        agencias.get(1).setTelefone("11912345678");
        agencias.get(1).setEndereco("Avenida Secundária, 456");

        when(agenciaRepository.findAll()).thenReturn(agencias);

        List<Agencia> resultado = agenciaService.buscarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(agenciaRepository, times(1)).findAll();
    }
    
    @Test
    public void testBuscarTodosSemSucesso() {
        when(agenciaRepository.findAll()).thenReturn(Collections.emptyList());

        List<Agencia> resultado = agenciaService.buscarTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(agenciaRepository, times(1)).findAll();
    }

    @Test
    public void testDeletarPorIdComSucesso() {
        when(agenciaRepository.findById(1L)).thenReturn(Optional.of(agencia));

        agenciaService.deletarPorId(1L);

        verify(agenciaRepository, times(1)).delete(agencia);
    }

    @Test
    public void testDeletarPorIdNaoExistente() {
        when(agenciaRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            agenciaService.deletarPorId(1L);
        });

        assertTrue(exception.getMessage().contains("Agência com 1 não encontrada"));
    }
}
