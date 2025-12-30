package ar.edu.utn.frba.dds.entities.dto.input.ProxyDto;

import lombok.Data;

@Data
public class UbicacionProxyDto {
  private Long id;
  private Double latitud;
  private Double longitud;
  private String provincia;

  public UbicacionProxyDto(){
    this.provincia = "Desconocido";
  }
}
