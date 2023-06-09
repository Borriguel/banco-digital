package dev.borriguel.bancodigital.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ComumResponse {
    private UUID id;
    private BigDecimal saldo;
}
