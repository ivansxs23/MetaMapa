package ar.edu.utn.frba.dds.entities.detectorDeSpam;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "documentos_corpus")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 5000)
    private String texto;

    public Documento() {}

    public Documento(String texto) {
        this.texto = texto;
    }

  public void setTexto(String texto) {
        this.texto = texto;
    }
}
