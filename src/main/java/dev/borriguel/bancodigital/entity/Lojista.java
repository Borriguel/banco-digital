package dev.borriguel.bancodigital.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tb_lojistas")
public class Lojista {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private UUID id;
    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;
    @Column(nullable = false)
    private String nomeEmpresa;
    @Column(nullable = false)
    private BigDecimal saldo;
}
