package dev.borriguel.bancodigital.Util;

import dev.borriguel.bancodigital.controller.dto.LojistaRequest;
import dev.borriguel.bancodigital.controller.dto.LojistaResponse;
import dev.borriguel.bancodigital.entity.Lojista;

import java.math.BigDecimal;
import java.util.UUID;

public class LojistaFactory {
    public static Lojista criarContaLojista() {
        var lojista = new Lojista();
        lojista.setId(UUID.fromString("4924139c-380e-4f9f-8661-b4d9214707ea"));
        lojista.setCnpj("09876543211");
        lojista.setNomeEmpresa("nome fantasia");
        lojista.setSaldo(BigDecimal.valueOf(5000));
        return lojista;
    }

    public static LojistaRequest criarContaLojistaRequest() {
        var request = new LojistaRequest();
        request.setNomeEmpresa("nome fantasia");
        request.setCnpj("09876543211");
        return request;
    }

    public static LojistaResponse criarContaLojistaResponse() {
        var response = new LojistaResponse();
        response.setId(UUID.fromString("4924139c-380e-4f9f-8661-b4d9214707ea"));
        response.setCnpj("09876543211");
        response.setNomeEmpresa("nome fantasia");
        response.setSaldo(BigDecimal.valueOf(5000));
        return response;
    }
}
