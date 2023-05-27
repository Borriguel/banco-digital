package dev.borriguel.bancodigital.controller;

import dev.borriguel.bancodigital.controller.dto.LojistaDTO;
import dev.borriguel.bancodigital.controller.dto.LojistaPost;
import dev.borriguel.bancodigital.service.LojistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/conta/lojista")
@RequiredArgsConstructor
@Tag(name = "Conta Lojista", description = "Endpoints do controlador conta lojista.")
public class LojistaController {
    private final LojistaService service;

    @PostMapping("/{id}")
    @Operation(summary = "Cria conta do tipo lojista para o cliente com id informado.")
    public ResponseEntity<LojistaDTO> criarConta(@Valid @PathVariable Long id, @RequestBody LojistaPost lojistaPost) {
        var lojista = service.criarConta(id, lojistaPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(lojista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Encontra uma conta lojista pelo id.")
    public ResponseEntity<LojistaDTO> encontrarContaLojistaPorId(@PathVariable UUID id) {
        var lojista = service.encontrarContaLojistaPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(lojista);
    }
}
