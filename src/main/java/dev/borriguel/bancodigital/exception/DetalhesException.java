package dev.borriguel.bancodigital.exception;

import java.time.Instant;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class DetalhesException {
    private Instant timestamp;
    private Integer status;
    private String erro;
    private String mensagem;
    private String diretorio;
}
