package dev.borriguel.bancodigital.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class LojistaRequest {
    @Pattern(regexp = "(\\d{14})", message = "Insira um CNPJ sem pontuação.")
    @NotBlank(message = "Campo CNPJ obrigatório.")
    private String cnpj;
    @NotBlank
    @JsonProperty(value = "nome_empresa")
    private String nomeEmpresa;
}
