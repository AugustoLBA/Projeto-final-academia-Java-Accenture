package br.acc.banco.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.acc.banco.dto.agencia.AgenciaCreateDTO;
import br.acc.banco.dto.agencia.AgenciaResponseDTO;
import br.acc.banco.models.Agencia;

public class AgenciaMapperTest {

    private AgenciaMapper agenciaMapper;

    private Agencia agencia;
    private AgenciaCreateDTO agenciaCreateDTO;
    private AgenciaResponseDTO agenciaResponseDTO;

    @BeforeEach
    public void setUp() {
        agenciaMapper = new AgenciaMapper();

        agencia = new Agencia();
        agencia.setId(1L);
        agencia.setNome("Agência Central");
        agencia.setTelefone("11987654321");
        agencia.setEndereco("Rua Central, 100");

        agenciaCreateDTO = new AgenciaCreateDTO();
        agenciaCreateDTO.setNome("Agência Central");
        agenciaCreateDTO.setTelefone("11987654321");
        agenciaCreateDTO.setEndereco("Rua Central, 100");

        agenciaResponseDTO = new AgenciaResponseDTO();
        agenciaResponseDTO.setId(1L);
        agenciaResponseDTO.setNome("Agência Central");
        agenciaResponseDTO.setTelefone("11987654321");
        agenciaResponseDTO.setEndereco("Rua Central, 100");
    }

    @Test
    public void testToAgencia() {
        Agencia mappedAgencia = agenciaMapper.toAgencia(agenciaCreateDTO);

        assertEquals(agenciaCreateDTO.getNome(), mappedAgencia.getNome());
        assertEquals(agenciaCreateDTO.getTelefone(), mappedAgencia.getTelefone());
        assertEquals(agenciaCreateDTO.getEndereco(), mappedAgencia.getEndereco());
    }

    @Test
    public void testToDto() {
        AgenciaResponseDTO mappedDto = agenciaMapper.toDto(agencia);

        assertEquals(agencia.getId(), mappedDto.getId());
        assertEquals(agencia.getNome(), mappedDto.getNome());
        assertEquals(agencia.getTelefone(), mappedDto.getTelefone());
        assertEquals(agencia.getEndereco(), mappedDto.getEndereco());
    }

    @Test
    public void testToListDto() {
        List<Agencia> agencias = Arrays.asList(agencia);

        List<AgenciaResponseDTO> dtos = agenciaMapper.toListDto(agencias);

        assertEquals(1, dtos.size());
        assertEquals(agencia.getId(), dtos.get(0).getId());
        assertEquals(agencia.getNome(), dtos.get(0).getNome());
        assertEquals(agencia.getTelefone(), dtos.get(0).getTelefone());
        assertEquals(agencia.getEndereco(), dtos.get(0).getEndereco());
    }
}
