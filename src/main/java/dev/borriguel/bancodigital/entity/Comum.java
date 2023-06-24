package dev.borriguel.bancodigital.entity;

import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_comuns")
public class Comum {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private UUID id;
    @Column(nullable = false)
    private BigDecimal saldo;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Comum comum = (Comum) o;
        return getId() != null && Objects.equals(getId(), comum.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
