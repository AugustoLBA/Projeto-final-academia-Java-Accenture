package br.acc.banco.infra;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ExceptionTriggerController.class)
@Import(RestExceptionHandler.class)
public class RestExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testEntityNotFoundException() throws Exception {
        mockMvc.perform(get("/trigger/entity-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo("NOT_FOUND")))
                .andExpect(jsonPath("$.message", equalTo("Entity not found")));
    }

    @Test
    public void testUsernameUniqueViolationException() throws Exception {
        mockMvc.perform(get("/trigger/username-conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", equalTo("CONFLICT")))
                .andExpect(jsonPath("$.message", equalTo("Username already exists")));
    }

    @Test
    public void testDepositoInvalidoException() throws Exception {
        mockMvc.perform(get("/trigger/invalid-deposit"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", equalTo("Invalid deposit")));
    }

    @Test
    public void testSaqueInvalidoException() throws Exception {
        mockMvc.perform(get("/trigger/invalid-withdraw"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", equalTo("Invalid withdraw")));
    }

    @Test
    public void testPixInvalidoException() throws Exception {
        mockMvc.perform(get("/trigger/invalid-pix"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", equalTo("Invalid pix")));
    }

    @Test
    public void testTransferenciaInvalidaException() throws Exception {
        mockMvc.perform(get("/trigger/invalid-transfer"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", equalTo("Invalid transfer")));
    }

    @Test
    public void testCompraInvalidaException() throws Exception {
        mockMvc.perform(get("/trigger/invalid-purchase"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", equalTo("Invalid purchase")));
    }
}
