package ar.edu.utn.frba.dds.entities.dto.input;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class UbicacionDTO {
  private Long id;
  private Double latitud;
  private Double longitud;
  private String provincia;

  public UbicacionDTO(){
    this.provincia = "Desconocido";
  }
}
