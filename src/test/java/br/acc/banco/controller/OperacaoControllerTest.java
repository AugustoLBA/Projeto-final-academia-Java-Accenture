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
import java.util.Date;
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

import br.acc.banco.dto.operacao.OperacaoCreateDTO;
import br.acc.banco.dto.operacao.OperacaoResponseDTO;
import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.mapper.OperacaoMapper;
import br.acc.banco.models.Operacao;
import br.acc.banco.models.enums.TipoOperacao;
import br.acc.banco.service.OperacaoService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(OperacaoController.class)
public class OperacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OperacaoService operacaoService;

    @MockBean
    private OperacaoMapper operacaoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Operacao operacao;
    private OperacaoResponseDTO operacaoResponseDTO;
    private OperacaoCreateDTO operacaoCreateDTO;

    @BeforeEach
    public void setUp() {
        operacao = new Operacao();
        operacao.setId(1L);
        operacao.setValor(BigDecimal.valueOf(100));
        operacao.setTipo(TipoOperacao.SAQUE);
        
        Date date = new Date();

        operacaoResponseDTO = new OperacaoResponseDTO(1L, date, TipoOperacao.SAQUE, BigDecimal.valueOf(100),1L);
        operacaoCreateDTO = new OperacaoCreateDTO(TipoOperacao.SAQUE, BigDecimal.valueOf(100), 1L);
    }



    @Test
    public void testFindAllOperacoesComSucesso() throws Exception {
        List<Operacao> operacoes = Arrays.asList(operacao);
        List<OperacaoResponseDTO> operacoesDto = Arrays.asList(operacaoResponseDTO);

        when(operacaoService.buscarTodas()).thenReturn(operacoes);
        when(operacaoMapper.toListDto(operacoes)).thenReturn(operacoesDto);

        mockMvc.perform(get("/api/banco/operacao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].valor").value(100))
                .andExpect(jsonPath("$[0].tipo").value("SAQUE"));
    }

    @Test
    public void testFindOperacaoByIdComSucesso() throws Exception {
        when(operacaoService.buscarPorId(1L)).thenReturn(operacao);
        when(operacaoMapper.toDto(operacao)).thenReturn(operacaoResponseDTO);

        mockMvc.perform(get("/api/banco/operacao/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.valor").value(100))
                .andExpect(jsonPath("$.tipo").value("SAQUE"));
    }

    @Test
    public void testFindOperacaoByIdNaoExistente() throws Exception {
        when(operacaoService.buscarPorId(1L)).thenThrow(new EntityNotFoundException("Operação não encontrada"));

        mockMvc.perform(get("/api/banco/operacao/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteOperacaoByIdComSucesso() throws Exception {
        doNothing().when(operacaoService).deletarPorId(1L);

        mockMvc.perform(delete("/api/banco/operacao/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteOperacaoByIdNaoExistente() throws Exception {
        doThrow(new EntityNotFoundException("Operação não encontrada")).when(operacaoService).deletarPorId(1L);

        mockMvc.perform(delete("/api/banco/operacao/1"))
                .andExpect(status().isNotFound());
    }
}
