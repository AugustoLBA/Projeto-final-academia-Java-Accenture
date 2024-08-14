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

import br.acc.banco.dto.agencia.AgenciaCreateDTO;
import br.acc.banco.dto.agencia.AgenciaResponseDTO;
import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.mapper.AgenciaMapper;
import br.acc.banco.models.Agencia;
import br.acc.banco.service.AgenciaService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AgenciaController.class)
public class AgenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgenciaService agenciaService;

    @MockBean
    private AgenciaMapper agenciaMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Agencia agencia;
    private AgenciaResponseDTO agenciaResponseDTO;
    private AgenciaCreateDTO agenciaCreateDTO;

    @BeforeEach
    public void setUp() {
        agencia = new Agencia();
        agencia.setId(1L);
        agencia.setNome("Agência Central");
        agencia.setTelefone("11987654321");
        agencia.setEndereco("Rua Central, 100");

        agenciaResponseDTO = new AgenciaResponseDTO(1L, "Agência Central", "11987654321", "Rua Central, 100");
        agenciaCreateDTO = new AgenciaCreateDTO("Agência Central", "11987654321", "Rua Central, 100");
    }

    @Test
    public void testSaveAgenciaComSucesso() throws Exception {
        when(agenciaMapper.toAgencia(any(AgenciaCreateDTO.class))).thenReturn(agencia);
        when(agenciaService.salvar(any(Agencia.class))).thenReturn(agencia);
        when(agenciaMapper.toDto(any(Agencia.class))).thenReturn(agenciaResponseDTO);

        mockMvc.perform(post("/api/banco/agencia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(agenciaCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Agência Central"));
    }

    @Test
    public void testSaveAgenciaComDadosInvalidos() throws Exception {
        AgenciaCreateDTO agenciaInvalida = new AgenciaCreateDTO("", "123", "");

        mockMvc.perform(post("/api/banco/agencia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(agenciaInvalida)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllAgenciasComSucesso() throws Exception {
        List<Agencia> agencias = Arrays.asList(agencia);
        List<AgenciaResponseDTO> agenciasDto = Arrays.asList(agenciaResponseDTO);

        when(agenciaService.buscarTodos()).thenReturn(agencias);
        when(agenciaMapper.toListDto(agencias)).thenReturn(agenciasDto);

        mockMvc.perform(get("/api/banco/agencia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Agência Central"));
    }

    @Test
    public void testFindAgenciaByIdComSucesso() throws Exception {
        when(agenciaService.buscarPorId(1L)).thenReturn(agencia);
        when(agenciaMapper.toDto(agencia)).thenReturn(agenciaResponseDTO);

        mockMvc.perform(get("/api/banco/agencia/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Agência Central"));
    }

    @Test
    public void testFindAgenciaByIdNaoExistente() throws Exception {
        when(agenciaService.buscarPorId(1L)).thenThrow(new EntityNotFoundException("Agência não encontrada"));

        mockMvc.perform(get("/api/banco/agencia/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteAgenciaByIdComSucesso() throws Exception {
        doNothing().when(agenciaService).deletarPorId(1L);

        mockMvc.perform(delete("/api/banco/agencia/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteAgenciaByIdNaoExistente() throws Exception {
        doThrow(new EntityNotFoundException("Agência não encontrada")).when(agenciaService).deletarPorId(1L);

        mockMvc.perform(delete("/api/banco/agencia/1"))
                .andExpect(status().isNotFound());
    }
}
