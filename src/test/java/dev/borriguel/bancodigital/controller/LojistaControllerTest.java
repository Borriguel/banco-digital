package dev.borriguel.bancodigital.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.borriguel.bancodigital.controller.dto.LojistaRequest;
import dev.borriguel.bancodigital.controller.dto.LojistaResponse;
import dev.borriguel.bancodigital.entity.Lojista;
import dev.borriguel.bancodigital.service.LojistaService;
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

import static dev.borriguel.bancodigital.Util.LojistaFactory.*;
import static dev.borriguel.bancodigital.Util.JsonUtils.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("dev")
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = LojistaController.class)
@DisplayName("Testes do LojistaController")
class LojistaControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LojistaService lojistaService;
    @MockBean
    private ModelMapper modelMapper;
    private Lojista contaLojista;
    private LojistaResponse contaLojistaResponse;
    private LojistaRequest contaLojistaRequest;

    LojistaControllerTest() {
    }

    @BeforeEach
    void setUp() {
        contaLojista = criarContaLojista();
        contaLojistaResponse = criarContaLojistaResponse();
        contaLojistaRequest = criarContaLojistaRequest();
    }

    @Test
    public void testCriarConta() throws Exception {
        Long clienteId = 1L;
        when(lojistaService.criarConta(eq(clienteId), any(LojistaRequest.class))).thenReturn(contaLojista);
        when(modelMapper.map(contaLojista, LojistaResponse.class)).thenReturn(contaLojistaResponse);
        mockMvc.perform(post("/conta/lojista/{id}", clienteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(contaLojistaRequest)))
                .andExpect(jsonPath("$.id").value(contaLojistaResponse.getId().toString()))
                .andExpect(jsonPath("$.saldo").value(contaLojistaResponse.getSaldo()))
                .andExpect(jsonPath("$.nomeEmpresa").value(contaLojistaResponse.getNomeEmpresa()))
                .andExpect(jsonPath("$.cnpj").value(contaLojistaResponse.getCnpj()));
    }

    @Test
    @DisplayName("Teste de busca de conta lojista por ID")
    void encontrarContaLojistaPorId() throws Exception {
        when(lojistaService.encontrarContaLojistaPorId(contaLojista.getId())).thenReturn(contaLojista);
        when(modelMapper.map(contaLojista, LojistaResponse.class)).thenReturn(contaLojistaResponse);
        mockMvc.perform(get("/conta/lojista/{id}", contaLojista.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contaLojistaResponse.getId().toString()))
                .andExpect(jsonPath("$.saldo").value(contaLojistaResponse.getSaldo()))
                .andExpect(jsonPath("$.nomeEmpresa").value(contaLojistaResponse.getNomeEmpresa()))
                .andExpect(jsonPath("$.cnpj").value(contaLojistaResponse.getCnpj()));
    }
}