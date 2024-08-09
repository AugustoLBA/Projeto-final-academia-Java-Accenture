package br.acc.banco.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.acc.banco.dto.cliente.ClienteCreateDTO;
import br.acc.banco.dto.cliente.ClienteResponseDTO;
import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.mapper.ClienteMapper;
import br.acc.banco.models.Cliente;
import br.acc.banco.service.ClienteService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private ClienteMapper clienteMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente cliente;
    private ClienteResponseDTO clienteResponseDTO;
    private ClienteCreateDTO clienteCreateDTO;

    @BeforeEach
    public void setUp() {
        cliente = new Cliente(1L, "João Silva", "12345678901", "11987654321", BigDecimal.valueOf(1000), null);
        clienteResponseDTO = new ClienteResponseDTO(1L, "João Silva", "12345678901", "11987654321", BigDecimal.valueOf(1000));
        clienteCreateDTO = new ClienteCreateDTO("João Silva", "12345678901", "11987654321", BigDecimal.valueOf(1000));
    }

    @Test
    public void testSaveClienteComSucesso() throws Exception {
        when(clienteMapper.toCliente(any(ClienteCreateDTO.class))).thenReturn(cliente);
        when(clienteService.salvar(any(Cliente.class))).thenReturn(cliente);
        when(clienteMapper.toDto(any(Cliente.class))).thenReturn(clienteResponseDTO);

        mockMvc.perform(post("/api/banco/cliente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    public void testSaveClienteComDadosInvalidos() throws Exception {
        ClienteCreateDTO clienteInvalido = new ClienteCreateDTO("", "123", "", BigDecimal.ZERO);

        mockMvc.perform(post("/api/banco/cliente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testFindAllClientesComSucesso() throws Exception {
        List<Cliente> clientes = Arrays.asList(cliente);
        List<ClienteResponseDTO> clientesDto = Arrays.asList(clienteResponseDTO);

        when(clienteService.buscarTodos()).thenReturn(clientes);
        when(clienteMapper.toListDto(clientes)).thenReturn(clientesDto);

        mockMvc.perform(get("/api/banco/cliente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("João Silva"));
    }

    @Test
    public void testFindClienteByIdComSucesso() throws Exception {
        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(clienteMapper.toDto(cliente)).thenReturn(clienteResponseDTO);

        mockMvc.perform(get("/api/banco/cliente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    public void testFindClienteByIdNaoExistente() throws Exception {
        when(clienteService.buscarPorId(1L)).thenThrow(new EntityNotFoundException("Cliente não encontrado"));

        mockMvc.perform(get("/api/banco/cliente/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteClienteByIdComSucesso() throws Exception {
        doNothing().when(clienteService).deletarPorId(1L);

        mockMvc.perform(delete("/api/banco/cliente/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteClienteByIdNaoExistente() throws Exception {
        doThrow(new EntityNotFoundException("Cliente não encontrado")).when(clienteService).deletarPorId(1L);

        mockMvc.perform(delete("/api/banco/cliente/1"))
                .andExpect(status().isNotFound());
    }
}
