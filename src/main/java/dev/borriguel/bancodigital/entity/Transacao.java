package dev.borriguel.bancodigital.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tb_transacoes")
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "uuid", nullable = false)
    private UUID idPagador;
    @Column(nullable = false)
    private BigDecimal valorTransacao;
    @Column(columnDefinition = "uuid", nullable = false)
    private UUID idDeposito;
    @Column(nullable = false)
    private LocalDate data;
    @Column(nullable = false)
    private LocalTime hora;

}
