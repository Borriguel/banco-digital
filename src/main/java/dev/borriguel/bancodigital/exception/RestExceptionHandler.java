package dev.borriguel.bancodigital.exception;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import dev.borriguel.bancodigital.exception.custom.ErroTransacaoException;
import dev.borriguel.bancodigital.exception.custom.RecursoDuplicadoException;
import dev.borriguel.bancodigital.exception.custom.RecursoInvalidoException;
import dev.borriguel.bancodigital.exception.custom.SaldoInsuficienteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(RecursoInvalidoException.class)
    public ResponseEntity<DetalhesException> recursoInvalido(RecursoInvalidoException e, HttpServletRequest http) {
        DetalhesException err = DetalhesException.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .erro("Entidade não encontrada")
                .diretorio(http.getRequestURI())
                .mensagem(e.getMessage())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<DetalhesException> recursoDuplicado(RecursoDuplicadoException e, HttpServletRequest http) {
        DetalhesException err = DetalhesException.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .erro("Dados duplicados")
                .diretorio(http.getRequestURI())
                .mensagem(e.getMessage())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidacaoException> validation(MethodArgumentNotValidException e, HttpServletRequest http) {
        ValidacaoException err = ValidacaoException.builder()
                .timestamp(Instant.now())
                .erro("Erro de validação")
                .mensagem("Verifique o campo erros para mais detalhes")
                .diretorio(http.getRequestURI())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        for (FieldError f : e.getBindingResult().getFieldErrors()) {
            err.adicionarErro(f.getField(), f.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<DetalhesException> saldoInsuficiente(SaldoInsuficienteException e, HttpServletRequest http) {
        DetalhesException err = DetalhesException.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .erro("Saldo insuficiente")
                .diretorio(http.getRequestURI())
                .mensagem(e.getMessage())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(ErroTransacaoException.class)
    public ResponseEntity<DetalhesException> erroTransacao(ErroTransacaoException e, HttpServletRequest http) {
        DetalhesException err = DetalhesException.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .erro("Erro ao efetuar transação")
                .diretorio(http.getRequestURI())
                .mensagem(e.getMessage())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }
}
