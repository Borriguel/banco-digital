package dev.borriguel.bancodigital.exception.custom;

public class RecursoInvalidoException extends RuntimeException {

    /**
     * RecursoInvalidoException é lançado quando um id inexistente no banco de dados
     * é passado como argumento.
     * 
     * @param mensagem breve explicação do erro que desencadeou a exceção.
     */
    public RecursoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
