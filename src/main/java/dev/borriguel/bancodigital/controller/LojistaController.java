package dev.borriguel.bancodigital.controller;

import dev.borriguel.bancodigital.controller.dto.LojistaResponse;
import dev.borriguel.bancodigital.controller.dto.LojistaRequest;
import dev.borriguel.bancodigital.service.LojistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(LojistaController.class);
    private final LojistaService service;
    private final ModelMapper modelMapper;

    @PostMapping("/{id}")
    @Operation(summary = "Cria conta do tipo lojista para o cliente com id informado.")
    public ResponseEntity<LojistaResponse> criarConta(@Valid @PathVariable Long id, @RequestBody LojistaRequest lojistaRequest) {
        var lojista = service.criarConta(id, lojistaRequest);
        logger.info("Conta lojista criada -> {}", lojista);
        var lojistaResponse = modelMapper.map(lojista, LojistaResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(lojistaResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Encontra uma conta lojista pelo id.")
    public ResponseEntity<LojistaResponse> encontrarContaLojistaPorId(@PathVariable UUID id) {
        var lojista = service.encontrarContaLojistaPorId(id);
        logger.info("Conta lojista encontrada -> {}", lojista);
        var lojistaResponse = modelMapper.map(lojista, LojistaResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(lojistaResponse);
    }
}
