package br.acc.banco.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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

import br.acc.banco.dto.seguro.SeguroCreateDTO;
import br.acc.banco.dto.seguro.SeguroResponseDTO;
import br.acc.banco.mapper.SeguroMapper;
import br.acc.banco.models.Seguro;
import br.acc.banco.models.enums.StatusSeguro;
import br.acc.banco.models.enums.TipoSeguro;
import br.acc.banco.service.SeguroService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(SeguroController.class)
public class SeguroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeguroService seguroService;

    @MockBean
    private SeguroMapper seguroMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Seguro seguro;
    private SeguroResponseDTO seguroResponseDTO;
    private SeguroCreateDTO seguroCreateDTO;

    @BeforeEach
    public void setUp() {
        seguro = new Seguro();
        seguro.setId(1L);
        seguro.setValor(BigDecimal.valueOf(1000));
        seguro.setQuantidadeParcelas(10);
        seguro.setQuantidadeParcelasPagas(0);
        seguro.setValorParcela(BigDecimal.valueOf(100));
        seguro.setStatus(StatusSeguro.ATIVO);
        seguro.setTipo(TipoSeguro.VIDA);

        seguroResponseDTO = new SeguroResponseDTO(1L, BigDecimal.valueOf(1000), StatusSeguro.ATIVO, TipoSeguro.VIDA, 10, 0, BigDecimal.valueOf(100));

        seguroCreateDTO = new SeguroCreateDTO(BigDecimal.valueOf(1000), 10, TipoSeguro.VIDA, 1L);
    }

    @Test
    public void testSaveSeguroComSucesso() throws Exception {
        when(seguroMapper.toSeguro(any(SeguroCreateDTO.class))).thenReturn(seguro);
        when(seguroService.salvar(any(Seguro.class))).thenReturn(seguro);
        when(seguroMapper.toDto(any(Seguro.class))).thenReturn(seguroResponseDTO);

        mockMvc.perform(post("/api/banco/seguro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(seguroCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.valor").value(1000))
                .andExpect(jsonPath("$.status").value("ATIVO"))
                .andExpect(jsonPath("$.tipoSeguro").value("VIDA"))
                .andExpect(jsonPath("$.quatidadeParcelas").value(10))
                .andExpect(jsonPath("$.quantidadesParcelasPagas").value(0))
                .andExpect(jsonPath("$.valorparcela").value(100));
    }

    @Test
    public void testGetAllSegurosComSucesso() throws Exception {
        List<SeguroResponseDTO> seguros = Arrays.asList(seguroResponseDTO);

        when(seguroService.buscarTodos()).thenReturn(Arrays.asList(seguro));
        when(seguroMapper.toListDto(any(List.class))).thenReturn(seguros);

        mockMvc.perform(get("/api/banco/seguro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].valor").value(1000))
                .andExpect(jsonPath("$[0].status").value("ATIVO"))
                .andExpect(jsonPath("$[0].tipoSeguro").value("VIDA"));
    }

    @Test
    public void testGetSeguroByIdComSucesso() throws Exception {
        when(seguroService.buscarPorId(1L)).thenReturn(seguro);
        when(seguroMapper.toDto(any(Seguro.class))).thenReturn(seguroResponseDTO);

        mockMvc.perform(get("/api/banco/seguro/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.valor").value(1000))
                .andExpect(jsonPath("$.status").value("ATIVO"))
                .andExpect(jsonPath("$.tipoSeguro").value("VIDA"))
                .andExpect(jsonPath("$.quatidadeParcelas").value(10))
                .andExpect(jsonPath("$.quantidadesParcelasPagas").value(0))
                .andExpect(jsonPath("$.valorparcela").value(100));
    }
}
