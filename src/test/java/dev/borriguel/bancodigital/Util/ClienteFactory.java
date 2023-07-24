package dev.borriguel.bancodigital.Util;

import dev.borriguel.bancodigital.controller.dto.ClienteRequest;
import dev.borriguel.bancodigital.controller.dto.ClienteResponse;
import dev.borriguel.bancodigital.entity.Cliente;

public class ClienteFactory {
    public static Cliente criarCliente() {
        var cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("teste");
        cliente.setCpf("12345678900");
        cliente.setEmail("email@test.com");
        return cliente;
    }

    public static ClienteResponse criarClienteResponse() {
        var response = new ClienteResponse();
        response.setId(1L);
        response.setNome("teste");
        response.setCpf("12345678900");
        response.setEmail("email@test.com");
        return response;
    }

    public static ClienteRequest criarClienteRequest() {
        var request = new ClienteRequest();
        request.setNome("teste");
        request.setEmail("email@test.com");
        request.setCpf("12345678900");
        return request;
    }
}
