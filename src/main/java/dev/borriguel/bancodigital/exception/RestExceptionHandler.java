package dev.borriguel.bancodigital.exception;

import dev.borriguel.bancodigital.exception.custom.ErroTransacaoException;
import dev.borriguel.bancodigital.exception.custom.RecursoDuplicadoException;
import dev.borriguel.bancodigital.exception.custom.RecursoInvalidoException;
import dev.borriguel.bancodigital.exception.custom.SaldoInsuficienteException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(RecursoInvalidoException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public DetalhesException recursoInvalido(RecursoInvalidoException e, HttpServletRequest http) {
        return DetalhesException.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .erro("Entidade não encontrada")
                .diretorio(http.getRequestURI())
                .mensagem(e.getMessage())
                .timestamp(Instant.now())
                .build();
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public DetalhesException recursoDuplicado(RecursoDuplicadoException e, HttpServletRequest http) {
        return DetalhesException.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .erro("Dados duplicados")
                .diretorio(http.getRequestURI())
                .mensagem(e.getMessage())
                .timestamp(Instant.now())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidacaoException validation(MethodArgumentNotValidException e, HttpServletRequest http) {
        ValidacaoException exception = ValidacaoException.builder()
                .timestamp(Instant.now())
                .erro("Erro de validação")
                .mensagem("Verifique o campo erros para mais detalhes")
                .diretorio(http.getRequestURI())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        for (FieldError f : e.getBindingResult().getFieldErrors()) {
            exception.adicionarErro(f.getField(), f.getDefaultMessage());
        }
        return exception;
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DetalhesException saldoInsuficiente(SaldoInsuficienteException e, HttpServletRequest http) {
        return DetalhesException.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .erro("Saldo insuficiente")
                .diretorio(http.getRequestURI())
                .mensagem(e.getMessage())
                .timestamp(Instant.now())
                .build();
    }

    @ExceptionHandler(ErroTransacaoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DetalhesException erroTransacao(ErroTransacaoException e, HttpServletRequest http) {
        return DetalhesException.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .erro("Erro ao efetuar transação")
                .diretorio(http.getRequestURI())
                .mensagem(e.getMessage())
                .timestamp(Instant.now())
                .build();
    }
}
