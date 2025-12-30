package ar.edu.utn.frba.dds.entities;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class HechoCsvData {

  @CsvBindByPosition(position = 0)
  private String titulo;

  @CsvBindByPosition(position = 1)
  private String descripcion;

  @CsvBindByPosition(position = 2)
  private String categoriaString;

  @CsvBindByPosition(position = 3)
  private double latitud;

  @CsvBindByPosition(position = 4)
  private double longitud;

  @CsvDate("dd/MM/yyyy")
  @CsvBindByPosition(position = 5)
  private LocalDate fechaAcontecimiento;
}