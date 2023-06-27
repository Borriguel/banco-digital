package dev.borriguel.bancodigital.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class ClienteRequest {
    @NotBlank(message = "Campo nome obrigatório")
    @Size(max = 50, min = 5, message = "Campo nome deve conter no máximo {max} e no mínimo {min} caracteres")
    private String nome;
    @Pattern(regexp = "(\\d{11})", message = "Insira um CPF sem pontuação.")
    @NotBlank(message = "Campo CPF obrigatório.")
    private String cpf;
    @NotBlank(message = "Campo email obrigatório")
    @Email(message = "Insira um email válido")
    private String email;
}

