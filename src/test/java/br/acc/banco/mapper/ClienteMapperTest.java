package br.acc.banco.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.acc.banco.dto.cliente.ClienteCreateDTO;
import br.acc.banco.dto.cliente.ClienteResponseDTO;
import br.acc.banco.models.Cliente;

public class ClienteMapperTest {

    private ClienteMapper clienteMapper;

    private Cliente cliente;
    private ClienteCreateDTO clienteCreateDTO;
    private ClienteResponseDTO clienteResponseDTO;

    @BeforeEach
    public void setUp() {
        clienteMapper = new ClienteMapper();

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setCpf("12345678901");
        cliente.setTelefone("11987654321");
        cliente.setRenda(BigDecimal.valueOf(1000));

        clienteCreateDTO = new ClienteCreateDTO();
        clienteCreateDTO.setNome("João Silva");
        clienteCreateDTO.setCpf("12345678901");
        clienteCreateDTO.setTelefone("11987654321");
        clienteCreateDTO.setRenda(BigDecimal.valueOf(1000));

        clienteResponseDTO = new ClienteResponseDTO();
        clienteResponseDTO.setId(1L);
        clienteResponseDTO.setNome("João Silva");
        clienteResponseDTO.setCpf("12345678901");
        clienteResponseDTO.setTelefone("11987654321");
        clienteResponseDTO.setRenda(BigDecimal.valueOf(1000));
    }

    @Test
    public void testToCliente() {
        Cliente mappedCliente = clienteMapper.toCliente(clienteCreateDTO);

        assertEquals(clienteCreateDTO.getNome(), mappedCliente.getNome());
        assertEquals(clienteCreateDTO.getCpf(), mappedCliente.getCpf());
        assertEquals(clienteCreateDTO.getTelefone(), mappedCliente.getTelefone());
        assertEquals(clienteCreateDTO.getRenda(), mappedCliente.getRenda());
    }

    @Test
    public void testToDto() {
        ClienteResponseDTO mappedDto = clienteMapper.toDto(cliente);

        assertEquals(cliente.getId(), mappedDto.getId());
        assertEquals(cliente.getNome(), mappedDto.getNome());
        assertEquals(cliente.getCpf(), mappedDto.getCpf());
        assertEquals(cliente.getTelefone(), mappedDto.getTelefone());
        assertEquals(cliente.getRenda(), mappedDto.getRenda());
    }

    @Test
    public void testToListDto() {
        List<Cliente> clientes = Arrays.asList(cliente);

        List<ClienteResponseDTO> dtos = clienteMapper.toListDto(clientes);

        assertEquals(1, dtos.size());
        assertEquals(cliente.getId(), dtos.get(0).getId());
        assertEquals(cliente.getNome(), dtos.get(0).getNome());
        assertEquals(cliente.getCpf(), dtos.get(0).getCpf());
        assertEquals(cliente.getTelefone(), dtos.get(0).getTelefone());
        assertEquals(cliente.getRenda(), dtos.get(0).getRenda());
    }
}
