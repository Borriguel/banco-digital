package dev.borriguel.bancodigital.controller;

import dev.borriguel.bancodigital.controller.dto.ComumDTO;
import dev.borriguel.bancodigital.service.ComumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/conta/comum")
@RequiredArgsConstructor
@Tag(name = "Conta Comum", description = "Endpoints do controlador conta comum.")
public class ComumController {
    private final ComumService service;

    @PostMapping("/{id}")
    @Operation(summary = "Cria conta do tipo comum para o cliente com id informado.")
    public ResponseEntity<ComumDTO> criarConta(@PathVariable Long id) {
        var comum = service.criarConta(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(comum);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Encontra uma conta comum pelo id.")
    public ResponseEntity<ComumDTO> encontrarContaComumPorId(@PathVariable UUID id) {
        var comum = service.encontrarContaComumPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(comum);
    }
}
