package dev.borriguel.bancodigital.Util;

import dev.borriguel.bancodigital.controller.dto.TransacaoRequest;
import dev.borriguel.bancodigital.controller.dto.TransacaoResponse;
import dev.borriguel.bancodigital.entity.Transacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class TransacaoFactory {
    public static Transacao criarTransacao() {
        var transacao = new Transacao();
        transacao.setId(1L);
        transacao.setValorTransacao(BigDecimal.valueOf(100));
        transacao.setIdPagador(UUID.fromString("e3ad3bc3-9d5d-49d8-a928-634c3a6e2596"));
        transacao.setIdDeposito(UUID.fromString("b3047e67-343f-4c4f-a37c-d7da58512ebf"));
        transacao.setData(LocalDate.parse("2020-06-24"));
        transacao.setHora(LocalTime.MIDNIGHT);
        return transacao;
    }

    public static TransacaoRequest criarTransacaoRequest() {
        var request = new TransacaoRequest();
        request.setValorTransacao(BigDecimal.valueOf(100));
        request.setIdPagador(UUID.fromString("e3ad3bc3-9d5d-49d8-a928-634c3a6e2596"));
        request.setIdDeposito(UUID.fromString("b3047e67-343f-4c4f-a37c-d7da58512ebf"));
        return request;
    }

    public static TransacaoResponse criarTransacaoResponse() {
        var response = new TransacaoResponse();
        response.setId(1L);
        response.setValorTransacao(BigDecimal.valueOf(100));
        response.setIdPagador(UUID.fromString("e3ad3bc3-9d5d-49d8-a928-634c3a6e2596"));
        response.setIdDeposito(UUID.fromString("b3047e67-343f-4c4f-a37c-d7da58512ebf"));
        response.setData(LocalDate.parse("2020-06-24"));
        response.setHora(LocalTime.MIDNIGHT);
        return response;
    }
}
