package dev.borriguel.bancodigital.controller;

import dev.borriguel.bancodigital.controller.dto.TransacaoRequest;
import dev.borriguel.bancodigital.controller.dto.TransacaoResponse;
import dev.borriguel.bancodigital.entity.Transacao;
import dev.borriguel.bancodigital.service.TransacaoService;
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

import static dev.borriguel.bancodigital.Util.JsonUtils.asJsonString;
import static dev.borriguel.bancodigital.Util.TransacaoFactory.criarTransacao;
import static dev.borriguel.bancodigital.Util.TransacaoFactory.criarTransacaoRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("dev")
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = TransacaoController.class)
@DisplayName("Testes do TransacaoController")
class TransacaoControllerTest {
    @MockBean
    private ModelMapper modelMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransacaoService service;
    private Transacao transacao;
    private TransacaoRequest request;
    private TransacaoResponse response;

    @BeforeEach
    void setUp() {
        transacao = criarTransacao();
        request = criarTransacaoRequest();
    }

    @Test
    @DisplayName("Teste de criação de transação")
    public void testTransacao() throws Exception {
        response = new TransacaoResponse();
        when(service.criarTransacao(any(TransacaoRequest.class))).thenReturn(transacao);
        when(modelMapper.map(transacao, TransacaoResponse.class)).thenReturn(response);
        mockMvc.perform(post("/transacao").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.valorTransacao").value(response.getValorTransacao()))
                .andExpect(jsonPath("$.idPagador").value(response.getIdPagador()))
                .andExpect(jsonPath("$.idDeposito").value(response.getIdDeposito()));
    }
}