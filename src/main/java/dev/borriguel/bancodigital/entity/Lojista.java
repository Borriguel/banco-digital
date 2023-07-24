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
    private BigDecimal saldo = BigDecimal.valueOf(5000);

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Lojista lojista = (Lojista) o;
        return getId() != null && Objects.equals(getId(), lojista.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
