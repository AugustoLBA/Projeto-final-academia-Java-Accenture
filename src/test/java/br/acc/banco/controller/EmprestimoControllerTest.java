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

import br.acc.banco.dto.emprestimo.EmprestimoCreateDTO;
import br.acc.banco.dto.emprestimo.EmprestimoResponseDTO;
import br.acc.banco.mapper.EmprestimoMapper;
import br.acc.banco.models.Emprestimo;
import br.acc.banco.models.enums.StatusEmprestimo;
import br.acc.banco.service.EmprestimoService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(EmprestimoController.class)
public class EmprestimoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmprestimoService emprestimoService;

    @MockBean
    private EmprestimoMapper emprestimoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Emprestimo emprestimo;
    private EmprestimoResponseDTO emprestimoResponseDTO;
    private EmprestimoCreateDTO emprestimoCreateDTO;

    @BeforeEach
    public void setUp() {
        emprestimo = new Emprestimo();
        emprestimo.setId(1L);
        emprestimo.setValor(BigDecimal.valueOf(1000));
        emprestimo.setQuantidadeParcelas(10);
        emprestimo.setQuantidadeParcelasPagas(0);
        emprestimo.setValorParcela(BigDecimal.valueOf(100));
        emprestimo.setStatus(StatusEmprestimo.APROVADO);

        emprestimoResponseDTO = new EmprestimoResponseDTO(1L, BigDecimal.valueOf(1000), StatusEmprestimo.APROVADO, 10, 0, BigDecimal.valueOf(100));

        emprestimoCreateDTO = new EmprestimoCreateDTO(BigDecimal.valueOf(1000), 10, 1L);
    }

    @Test
    public void testSaveEmprestimoComSucesso() throws Exception {
        when(emprestimoMapper.toEmprestimo(any(EmprestimoCreateDTO.class))).thenReturn(emprestimo);
        when(emprestimoService.salvar(any(Emprestimo.class))).thenReturn(emprestimo);
        when(emprestimoMapper.toDto(any(Emprestimo.class))).thenReturn(emprestimoResponseDTO);

        mockMvc.perform(post("/api/banco/emprestimo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emprestimoCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.valor").value(1000))
                .andExpect(jsonPath("$.status").value("APROVADO"))
                .andExpect(jsonPath("$.quatidadeParcelas").value(10))
                .andExpect(jsonPath("$.quantidadesParcelasPagas").value(0))
                .andExpect(jsonPath("$.valorparcela").value(100));
    }

    @Test
    public void testGetAllEmprestimosComSucesso() throws Exception {
        List<EmprestimoResponseDTO> emprestimosDto = Arrays.asList(emprestimoResponseDTO);

        when(emprestimoService.buscarTodos()).thenReturn(Arrays.asList(emprestimo));
        when(emprestimoMapper.toListDto(any(List.class))).thenReturn(emprestimosDto);

        mockMvc.perform(get("/api/banco/emprestimo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].valor").value(1000))
                .andExpect(jsonPath("$[0].status").value("APROVADO"))
                .andExpect(jsonPath("$[0].quatidadeParcelas").value(10))
                .andExpect(jsonPath("$[0].quantidadesParcelasPagas").value(0))
                .andExpect(jsonPath("$[0].valorparcela").value(100));
    }

    @Test
    public void testGetEmprestimoByIdComSucesso() throws Exception {
        when(emprestimoService.buscarPorId(1L)).thenReturn(emprestimo);
        when(emprestimoMapper.toDto(any(Emprestimo.class))).thenReturn(emprestimoResponseDTO);

        mockMvc.perform(get("/api/banco/emprestimo/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.valor").value(1000))
                .andExpect(jsonPath("$.status").value("APROVADO"))
                .andExpect(jsonPath("$.quatidadeParcelas").value(10))
                .andExpect(jsonPath("$.quantidadesParcelasPagas").value(0))
                .andExpect(jsonPath("$.valorparcela").value(100));
    }
}
