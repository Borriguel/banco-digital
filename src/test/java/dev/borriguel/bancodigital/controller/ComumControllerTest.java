package dev.borriguel.bancodigital.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.borriguel.bancodigital.controller.dto.ComumResponse;
import dev.borriguel.bancodigital.entity.Comum;
import dev.borriguel.bancodigital.service.ComumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("dev")
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ComumController.class)
@DisplayName("Testes do ComumController")
class ComumControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ComumService service;
    @MockBean
    private ModelMapper modelMapper;
    private Comum contaComum;
    private ComumResponse contaComumResponse;

    @BeforeEach
    void setUp() {
        contaComum = new Comum();
        contaComum.setId(UUID.randomUUID());
        contaComumResponse = new ComumResponse();
        contaComumResponse.setId(contaComum.getId());
        contaComumResponse.setSaldo(contaComum.getSaldo());
    }

    @Test
    @DisplayName("Teste de criação de conta comum")
    void criarConta() throws Exception {
        Long clienteId = 1L;
        when(service.criarConta(clienteId)).thenReturn(contaComum);
        when(modelMapper.map(contaComum, ComumResponse.class)).thenReturn(contaComumResponse);
        mockMvc.perform(post("/conta/comum/{id}", clienteId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(contaComumResponse.getId().toString()))
                .andExpect(jsonPath("$.saldo").value(contaComumResponse.getSaldo()));
    }

    @Test
    @DisplayName("Teste de busca de conta comum por ID")
    void encontrarContaComumPorId() throws Exception {
        when(service.encontrarContaComumPorId(contaComum.getId())).thenReturn(contaComum);
        when(modelMapper.map(contaComum, ComumResponse.class)).thenReturn(contaComumResponse);
        mockMvc.perform(get("/conta/comum/{id}", contaComum.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contaComumResponse.getId().toString()))
                .andExpect(jsonPath("$.saldo").value(contaComumResponse.getSaldo()));
    }
}
