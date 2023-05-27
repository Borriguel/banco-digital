package dev.borriguel.bancodigital.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class LojistaPost {
    @Pattern(regexp = "(\\d{14})", message = "Insira um CNPJ sem pontuação.")
    @NotBlank(message = "Campo CNPJ obrigatório.")
    private String cnpj;
    @NotBlank
    @JsonProperty(value = "nome_empresa")
    private String nomeEmpresa;
}
