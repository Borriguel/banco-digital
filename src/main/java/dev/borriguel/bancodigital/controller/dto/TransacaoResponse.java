package dev.borriguel.bancodigital.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TransacaoResponse {
    private Long id;
    private UUID idPagador;
    private BigDecimal valorTransacao;
    private UUID idDeposito;
    private LocalDate data;
    private LocalTime hora;
}
