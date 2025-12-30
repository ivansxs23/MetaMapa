package ar.edu.utn.frba.dds.webclient.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class HechoDTO {

  @NotBlank(message = "El título es obligatorio")
  @Length(max = 100, message = "El título no puede superar los 100 caracteres")
  private String titulo;

  @NotBlank(message = "La descripción es obligatoria")
  @Length(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
  private String descripcion;

  @NotBlank(message = "La fecha del acontecimiento es obligatoria")
  private String fechaAcontecimiento;

  @NotNull(message = "La categoría es obligatoria")
  @Positive(message = "El id de categoría debe ser un número positivo")
  private Long idCategoria;

  @NotNull(message = "La latitud es obligatoria")
  @DecimalMin(value = "-90.0", message = "La latitud mínima es -90.0")
  @DecimalMax(value = "90.0", message = "La latitud máxima es 90.0")
  private Double latitud;

  @NotNull(message = "La longitud es obligatoria")
  @DecimalMin(value = "-180.0", message = "La longitud mínima es -180.0")
  @DecimalMax(value = "180.0", message = "La longitud máxima es 180.0")
  private Double longitud;


  private Boolean esAnonimo = false;

  private List<ArchivoOutputDTO> archivos;

  public HechoDTO() {
    this.archivos = new ArrayList<>();
  }
}

