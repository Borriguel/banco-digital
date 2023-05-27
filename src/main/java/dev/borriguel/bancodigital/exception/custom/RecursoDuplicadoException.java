package dev.borriguel.bancodigital.exception.custom;

public class RecursoDuplicadoException extends RuntimeException {
    /**
     * RecursoDuplicadoException é lançado quando uma informação que já consta no
     * banco de dados é passado como argumento Ex.: CPF e email.
     * 
     * @param mensagem
     */
    public RecursoDuplicadoException(String mensagem) {
        super(mensagem);
    }
}
