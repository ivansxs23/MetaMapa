package ar.edu.utn.frba.dds.webclient.dto;

import lombok.Data;
import java.util.List;
@Data
public class EstadisticaDTO {
    private Long id;
    private List<ProvinciaCantidad> topProvincia;
    private CategoriaCantidad topCategoria;
    private List<ProvinciaCantidadCategoria> provinciaPorCategoria;
    private List<HoraCantidad> horaPicoPorCategoria;
    private Long cantidadSpam;
}