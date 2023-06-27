package dev.borriguel.bancodigital.controller;

import dev.borriguel.bancodigital.controller.dto.TransacaoResponse;
import dev.borriguel.bancodigital.controller.dto.TransacaoRequest;
import dev.borriguel.bancodigital.service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacao")
@RequiredArgsConstructor
@Tag(name = "Transação", description = "Endpoints do controlador transação.")
public class TransacaoController {
    private final TransacaoService service;

    @PostMapping
    @Operation(summary = "Cria uma nova transação.", description = "Observação: Apenas contas do tipo comum podem realizar transações, contas lojistas apenas recebem transações.")
    public ResponseEntity<TransacaoResponse> transacao(@RequestBody TransacaoRequest transacaoRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(service.criarTransacao(transacaoRequest));
    }

    @GetMapping
    @Operation(summary = "Encontra transações pela data mínima, caso não seja informada o valor padrão será de 7 dias atrás e data máxima que caso não seja informada será a data atual.")
    public ResponseEntity<Page<TransacaoResponse>> encontrarTransacoes(
            @RequestParam(value = "min", defaultValue = "") String min,
            @RequestParam(value = "max", defaultValue = "") String max, @ParameterObject Pageable page) {
        return ResponseEntity.status(HttpStatus.OK).body(service.encontrarTransacoes(min, max, page));
    }
}
