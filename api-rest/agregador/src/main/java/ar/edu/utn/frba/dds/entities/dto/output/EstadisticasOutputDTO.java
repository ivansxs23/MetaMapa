package ar.edu.utn.frba.dds.entities.dto.output;


import ar.edu.utn.frba.dds.entities.dto.input.estadistica.CategoriaCantidadDTO;
import ar.edu.utn.frba.dds.entities.dto.input.estadistica.HoraCantidadCategoriaDTO;
import ar.edu.utn.frba.dds.entities.dto.input.estadistica.ProvinciaCantidadCategoriaDTO;
import ar.edu.utn.frba.dds.entities.dto.input.estadistica.ProvinciaCantidadDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstadisticasOutputDTO {

    private List<ProvinciaCantidadDTO> topProvinciaPorColecciones;
    private CategoriaCantidadDTO topCategoria;
    private List<ProvinciaCantidadCategoriaDTO> topProvinciaPorCategoria;
    private List<HoraCantidadCategoriaDTO> horaPicoPorCategoria;
    private Long cantidadSpam;

    public EstadisticasOutputDTO(){
        this.topProvinciaPorColecciones = new ArrayList<>();
        this.topProvinciaPorCategoria = new ArrayList<>();
        this.horaPicoPorCategoria = new ArrayList<>();
    }
}
