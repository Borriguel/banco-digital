package dev.borriguel.bancodigital.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class LojistaDTO {
    private UUID id;
    private String cnpj;
    private String nomeEmpresa;
    private BigDecimal saldo;
}
