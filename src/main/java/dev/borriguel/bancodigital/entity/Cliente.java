package dev.borriguel.bancodigital.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tb_clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String nome;

    @Column(unique = true, length = 11, nullable = false)
    private String cpf;

    @Column(length = 100, unique = true, nullable = false)
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    private Comum contaComum;

    @OneToOne(cascade = CascadeType.ALL)
    private Lojista contaLojista;

}
