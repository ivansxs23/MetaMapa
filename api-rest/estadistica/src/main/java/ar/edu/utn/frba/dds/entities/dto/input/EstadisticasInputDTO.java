package ar.edu.utn.frba.dds.entities.dto.input;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.entities.dto.CategoriaCantidadDTO;
import ar.edu.utn.frba.dds.entities.dto.HoraCantidadDTO;
import ar.edu.utn.frba.dds.entities.dto.ProvinciaCantidadCategoriaDTO;
import ar.edu.utn.frba.dds.entities.dto.ProvinciaCantidadDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstadisticasInputDTO {

    private List<ProvinciaCantidadDTO> topProvinciaPorColecciones;
    private CategoriaCantidadDTO topCategoria;
    private List<ProvinciaCantidadCategoriaDTO> topProvinciaPorCategoria;
    private List<HoraCantidadDTO> horaPicoPorCategoria;
    private Long cantidadSpam;

    public EstadisticasInputDTO(){
        this.topProvinciaPorColecciones = new ArrayList<>();
        this.topProvinciaPorCategoria = new ArrayList<>();
        this.horaPicoPorCategoria = new ArrayList<>();
    }
}