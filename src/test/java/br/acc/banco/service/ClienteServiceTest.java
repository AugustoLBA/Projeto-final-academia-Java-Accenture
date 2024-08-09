package br.acc.banco.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.exception.UsernameUniqueViolationException;
import br.acc.banco.models.Cliente;
import br.acc.banco.repository.ClienteRepository;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @InjectMocks
    ClienteService clienteService;

    @Mock
    ClienteRepository clienteRepository;

    Cliente cliente;

    @BeforeEach
    public void setUp() {
        cliente = new Cliente(1L, "João Silva", "12345678901", "11987654321", BigDecimal.valueOf(1000), null);
    }

    @Test
    public void testSalvarClienteComSucesso() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente clienteSalvo = clienteService.salvar(cliente);

        assertNotNull(clienteSalvo);
        assertEquals(cliente.getCpf(), clienteSalvo.getCpf());
        verify(clienteRepository, times(1)).save(cliente);
    }
    
    @Test
    public void testSalvarClienteComErroGenerico() {
        when(clienteRepository.save(any(Cliente.class)))
                .thenThrow(new DataIntegrityViolationException("Erro desconhecido"));

        UsernameUniqueViolationException exception = assertThrows(UsernameUniqueViolationException.class, () -> {
            clienteService.salvar(cliente);
        });

        assertTrue(exception.getMessage().contains("Erro ao salvar cliente"));
    }

    @Test
    public void testSalvarClienteComCpfDuplicado() {
        when(clienteRepository.save(any(Cliente.class)))
                .thenThrow(new DataIntegrityViolationException("CPF"));

        UsernameUniqueViolationException exception = assertThrows(UsernameUniqueViolationException.class, () -> {
            clienteService.salvar(cliente);
        });

        assertTrue(exception.getMessage().contains("CPF"));
    }

    @Test
    public void testSalvarClienteComTelefoneDuplicado() {
        when(clienteRepository.save(any(Cliente.class)))
                .thenThrow(new DataIntegrityViolationException("telefone"));

        UsernameUniqueViolationException exception = assertThrows(UsernameUniqueViolationException.class, () -> {
            clienteService.salvar(cliente);
        });

        assertTrue(exception.getMessage().contains("telefone"));
    }

    @Test
    public void testBuscarPorIdComSucesso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente clienteEncontrado = clienteService.buscarPorId(1L);

        assertNotNull(clienteEncontrado);
        assertEquals(cliente.getId(), clienteEncontrado.getId());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    public void testBuscarPorIdNaoExistente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            clienteService.buscarPorId(1L);
        });

        assertTrue(exception.getMessage().contains("Cliente com 1 não encontrado"));
    }

    @Test
    public void testDeletarPorIdComSucesso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        clienteService.deletarPorId(1L);

        verify(clienteRepository, times(1)).delete(cliente);
    }

    @Test
    public void testDeletarPorIdNaoExistente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            clienteService.deletarPorId(1L);
        });

        assertTrue(exception.getMessage().contains("Cliente com 1 não encontrado"));
    }
    
    @Test
    public void testBuscarTodos() {
        List<Cliente> clientes = Arrays.asList(
                cliente,
                new Cliente(2L, "Maria Souza", "10987654321", "11912345678", BigDecimal.valueOf(2000), null)
        );

        when(clienteRepository.findAll()).thenReturn(clientes);

        List<Cliente> resultado = clienteService.buscarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(clienteRepository, times(1)).findAll();
    }
}
