package dev.borriguel.bancodigital.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClienteDTO {
    private Long id;
    private String nome;
    private String cpf;
    private String email;
}
