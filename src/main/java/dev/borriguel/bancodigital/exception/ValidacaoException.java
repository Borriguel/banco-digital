package dev.borriguel.bancodigital.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ValidacaoException extends DetalhesException {
    @Builder.Default
    private List<ErrosCampo> erros = new ArrayList<>();

    public void adicionarErro(String campo, String mensagem) {
        erros.add(new ErrosCampo(campo, mensagem));
    }
}
