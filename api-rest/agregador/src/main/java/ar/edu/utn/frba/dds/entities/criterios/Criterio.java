package ar.edu.utn.frba.dds.entities.criterios;
import ar.edu.utn.frba.dds.entities.Hecho;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="criterio")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo")
@Getter @Setter @NoArgsConstructor
public abstract class Criterio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="tipo", insertable = false, updatable = false)
    public String tipo;

    protected Criterio (String tipo){
        this.tipo = tipo;
    }

    public abstract Boolean cumple(Hecho hecho);

}
