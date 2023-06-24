package dev.borriguel.bancodigital.entity;

import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Transacao transacao = (Transacao) o;
        return getId() != null && Objects.equals(getId(), transacao.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
