package br.acc.banco.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.acc.banco.dto.contaCorrente.ContaCorrenteCreateDTO;
import br.acc.banco.dto.contaCorrente.ContaCorrenteResponseDTO;
import br.acc.banco.models.Agencia;
import br.acc.banco.models.Cliente;
import br.acc.banco.models.ContaCorrente;
import br.acc.banco.service.AgenciaService;
import br.acc.banco.service.ClienteService;

@ExtendWith(MockitoExtension.class)
public class ContaCorrenteMapperTest {

    @InjectMocks
    private ContaCorrenteMapper contaCorrenteMapper;

    @Mock
    private AgenciaService agenciaService;

    @Mock
    private ClienteService clienteService;

    private Agencia agencia;
    private Cliente cliente;
    private ContaCorrente contaCorrente;
    private ContaCorrenteCreateDTO contaCorrenteCreateDTO;
    private ContaCorrenteResponseDTO contaCorrenteResponseDTO;

    @BeforeEach
    public void setUp() {
        agencia = new Agencia();
        agencia.setId(1L);
        agencia.setNome("Agência Central");
        agencia.setTelefone("11987654321");
        agencia.setEndereco("Rua Central, 100");

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setCpf("12345678901");
        cliente.setTelefone("11987654321");
        cliente.setRenda(BigDecimal.valueOf(1000));

        contaCorrente = new ContaCorrente();
        contaCorrente.setId(1L);
        contaCorrente.setNumero(123456);
        contaCorrente.setSaldo(BigDecimal.valueOf(1000));
        contaCorrente.setAgencia(agencia);
        contaCorrente.setCliente(cliente);

        contaCorrenteCreateDTO = new ContaCorrenteCreateDTO();
        contaCorrenteCreateDTO.setNumero(123456);
        contaCorrenteCreateDTO.setSaldo(BigDecimal.valueOf(1000));
        contaCorrenteCreateDTO.setAgenciaId(1L);
        contaCorrenteCreateDTO.setClienteId(1L);

        contaCorrenteResponseDTO = new ContaCorrenteResponseDTO();
        contaCorrenteResponseDTO.setId(1L);
        contaCorrenteResponseDTO.setNumero(123456);
        contaCorrenteResponseDTO.setSaldo(BigDecimal.valueOf(1000));
        contaCorrenteResponseDTO.setAgenciaId(1L);
        contaCorrenteResponseDTO.setClienteId(1L);
    }

    @Test
    public void testToContaCorrente() {
        when(agenciaService.buscarPorId(anyLong())).thenReturn(agencia);
        when(clienteService.buscarPorId(anyLong())).thenReturn(cliente);

        ContaCorrente mappedConta = contaCorrenteMapper.toContaCorrente(contaCorrenteCreateDTO);

        assertEquals(contaCorrenteCreateDTO.getNumero(), mappedConta.getNumero());
        assertEquals(contaCorrenteCreateDTO.getSaldo(), mappedConta.getSaldo());
        assertEquals(agencia, mappedConta.getAgencia());
        assertEquals(cliente, mappedConta.getCliente());
    }

    @Test
    public void testToDto() {
        ContaCorrenteResponseDTO mappedDto = contaCorrenteMapper.toDto(contaCorrente);

        assertEquals(contaCorrente.getId(), mappedDto.getId());
        assertEquals(contaCorrente.getNumero(), mappedDto.getNumero());
        assertEquals(contaCorrente.getSaldo(), mappedDto.getSaldo());
        assertEquals(contaCorrente.getAgencia().getId(), mappedDto.getAgenciaId());
        assertEquals(contaCorrente.getCliente().getId(), mappedDto.getClienteId());
    }

    @Test
    public void testToListDto() {
        List<ContaCorrente> contas = Arrays.asList(contaCorrente);

        List<ContaCorrenteResponseDTO> dtos = contaCorrenteMapper.toListDto(contas);

        assertEquals(1, dtos.size());
        assertEquals(contaCorrente.getId(), dtos.get(0).getId());
        assertEquals(contaCorrente.getNumero(), dtos.get(0).getNumero());
        assertEquals(contaCorrente.getSaldo(), dtos.get(0).getSaldo());
        assertEquals(contaCorrente.getAgencia().getId(), dtos.get(0).getAgenciaId());
        assertEquals(contaCorrente.getCliente().getId(), dtos.get(0).getClienteId());
    }
}
