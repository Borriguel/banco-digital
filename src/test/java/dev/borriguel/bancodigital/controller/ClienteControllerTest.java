package dev.borriguel.bancodigital.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.borriguel.bancodigital.Util.ClienteFactory;
import dev.borriguel.bancodigital.controller.dto.ClienteRequest;
import dev.borriguel.bancodigital.controller.dto.ClienteResponse;
import dev.borriguel.bancodigital.entity.Cliente;
import dev.borriguel.bancodigital.service.ClienteService;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static dev.borriguel.bancodigital.Util.JsonUtils.asJsonString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("dev")
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ClienteController.class)
@DisplayName("Testes do ClienteController")
class ClienteControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ModelMapper modelMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClienteService service;
    private ClienteRequest clienteRequest;
    private ClienteResponse clienteResponse;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = ClienteFactory.criarCliente();
        clienteRequest = ClienteFactory.criarClienteRequest();
        clienteResponse = ClienteFactory.criarClienteResponse();
    }

    @Test
    @DisplayName("Teste de criação de cliente")
    void criarCliente() throws Exception {
        when(service.criarCliente(clienteRequest)).thenReturn(cliente);
        when(modelMapper.map(cliente, ClienteResponse.class)).thenReturn(clienteResponse);
        mockMvc.perform(MockMvcRequestBuilders.post("/cliente")
                        .content(asJsonString(clienteRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(clienteResponse.getId().toString()))
                .andExpect(jsonPath("$.nome").value(clienteResponse.getNome()))
                .andExpect(jsonPath("$.cpf").value(clienteResponse.getCpf()))
                .andExpect(jsonPath("$.email").value(clienteResponse.getEmail()));
    }

    @Test
    @DisplayName("Teste de busca de cliente por ID")
    void encontrarCliente() throws Exception {
        when(service.encontrarClientePorId(cliente.getId())).thenReturn(cliente);
        when(modelMapper.map(cliente, ClienteResponse.class)).thenReturn(clienteResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/cliente/" + cliente.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clienteResponse.getId().toString()))
                .andExpect(jsonPath("$.nome").value(clienteResponse.getNome()))
                .andExpect(jsonPath("$.cpf").value(clienteResponse.getCpf()))
                .andExpect(jsonPath("$.email").value(clienteResponse.getEmail()));
    }

    @Test
    @DisplayName("Teste de busca de cliente por email")
    void encontrarClienteEmail() throws Exception {
        when(service.encontrarClientePorEmail(cliente.getEmail())).thenReturn(cliente);
        when(modelMapper.map(cliente, ClienteResponse.class)).thenReturn(clienteResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/cliente/?email=" + cliente.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clienteResponse.getId().toString()))
                .andExpect(jsonPath("$.nome").value(clienteResponse.getNome()))
                .andExpect(jsonPath("$.cpf").value(clienteResponse.getCpf()))
                .andExpect(jsonPath("$.email").value(clienteResponse.getEmail()));
    }

    @Test
    @DisplayName("Teste de busca de clientes por parte do nome")
    void encontrarClientesParteNome() throws Exception {
        var subStringNome = cliente.getNome().substring(0, 3);
        when(service.encontrarClienteParteNome(eq(subStringNome), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(cliente)));
        mockMvc.perform(MockMvcRequestBuilders.get("/cliente?nome=" + subStringNome))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Teste de busca de todos os clientes")
    void encontrarTodosClientes() throws Exception {
        when(service.encontrarTodosClientes(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(cliente)));
        mockMvc.perform(MockMvcRequestBuilders.get("/cliente/todos")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Teste de exclusão de cliente")
    void deletarCliente() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/cliente/" + cliente.getId())).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Teste de atualização de cliente")
    void atualizarCliente() throws Exception {
        clienteRequest.setNome("nome atualizado");
        clienteRequest.setEmail("email@atualizado.com");
        var clienteAtualizado = new Cliente(1L, clienteRequest.getNome(), clienteRequest.getCpf(), clienteRequest.getEmail(), null, null);
        when(service.atualizarCliente(cliente.getId(), clienteRequest)).thenReturn(clienteAtualizado);
        clienteResponse.setNome(clienteAtualizado.getNome());
        clienteResponse.setEmail(clienteAtualizado.getEmail());
        when(modelMapper.map(clienteAtualizado, ClienteResponse.class)).thenReturn(clienteResponse);
        mockMvc.perform(MockMvcRequestBuilders.put("/cliente/" + cliente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(clienteRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clienteResponse.getId().toString()))
                .andExpect(jsonPath("$.nome").value(clienteResponse.getNome()))
                .andExpect(jsonPath("$.cpf").value(clienteResponse.getCpf()))
                .andExpect(jsonPath("$.email").value(clienteResponse.getEmail()));
    }
}