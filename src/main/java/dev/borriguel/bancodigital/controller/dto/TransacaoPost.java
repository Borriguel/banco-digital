package dev.borriguel.bancodigital.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TransacaoPost {
    @JsonIgnore
    private Long id;
    @NotBlank(message = "Insira o id da conta do pagador.")
    @JsonProperty(value = "id_pagador")
    private UUID idPagador;
    @JsonProperty(value = "valor_transacao")
    private BigDecimal valorTransacao;
    @NotBlank(message = "Insira o id da conta para depósito.")
    @JsonProperty(value = "id_deposito")
    private UUID idDeposito;
}
