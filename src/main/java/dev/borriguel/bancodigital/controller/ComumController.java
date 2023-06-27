package dev.borriguel.bancodigital.controller;

import dev.borriguel.bancodigital.controller.dto.ComumResponse;
import dev.borriguel.bancodigital.service.ComumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/conta/comum")
@RequiredArgsConstructor
@Tag(name = "Conta Comum", description = "Endpoints do controlador conta comum.")
public class ComumController {
    private static final Logger logger = LoggerFactory.getLogger(ComumResponse.class);
    private final ComumService service;
    private final ModelMapper modelMapper;

    @PostMapping("/{id}")
    @Operation(summary = "Cria conta do tipo comum para o cliente com id informado.")
    public ResponseEntity<ComumResponse> criarConta(@PathVariable Long id) {
        var comum = service.criarConta(id);
        logger.info("Conta comum criada -> {}", comum);
        var comumResponse = modelMapper.map(comum, ComumResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(comumResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Encontra uma conta comum pelo id.")
    public ResponseEntity<ComumResponse> encontrarContaComumPorId(@PathVariable UUID id) {
        var comum = service.encontrarContaComumPorId(id);
        logger.info("Conta comum encontrada -> {}", comum);
        var comumResponse = modelMapper.map(comum, ComumResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(comumResponse);
    }
}
