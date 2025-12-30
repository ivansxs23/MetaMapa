package ar.edu.utn.frba.dds.entities.dto.output;

import ar.edu.utn.frba.dds.entities.TipoFuente;
import ar.edu.utn.frba.dds.entities.criterios.Criterio;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColeccionConHechoOutputDTo {
  public ColeccionConHechoOutputDTo(){
    this.fuentes = new ArrayList<>();
    this.criterioDePertenencia = new ArrayList<>();
    this.hechos = new ArrayList<>();
  }

  private Long id;
  private String handle;
  private String titulo;
  private String descripcion;
  private List<TipoFuente> fuentes;
  private List<Criterio> criterioDePertenencia;
  private String algoritmo;
  private List<HechoOutputDto> hechos;
}