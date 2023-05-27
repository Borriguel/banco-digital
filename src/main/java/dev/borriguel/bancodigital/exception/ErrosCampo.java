package dev.borriguel.bancodigital.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrosCampo {
    private String campo;
    private String mensagem;

    public ErrosCampo(String campo, String mensagem){
        this.campo = campo;
        this.mensagem = mensagem;
    }
}
