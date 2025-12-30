package ar.edu.utn.frba.dds.clienteUtils;

import ar.edu.utn.frba.dds.entities.*;
import ar.edu.utn.frba.dds.entities.dto.HoraCantidadDTO;
import ar.edu.utn.frba.dds.entities.dto.ProvinciaCantidadCategoriaDTO;
import ar.edu.utn.frba.dds.entities.dto.ProvinciaCantidadDTO;
import ar.edu.utn.frba.dds.entities.dto.input.EstadisticasInputDTO;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class EstadisticasMapper {
    public Estadistica aDominio(EstadisticasInputDTO estadisticasDTO) {
        Estadistica estadistica = new Estadistica();
        CategoriaCantidad topCategoriaDto = new CategoriaCantidad();

        estadistica.setTopProvincia(
                estadisticasDTO.getTopProvinciaPorColecciones().stream()
                        .map(this::mapProvinciaCantidad)
                        .toList()
        );

        topCategoriaDto.setCantidad(estadisticasDTO.getTopCategoria().getCantidad());
        topCategoriaDto.setCategoria(estadisticasDTO.getTopCategoria().getCategoria());

        estadistica.setTopCategoria(topCategoriaDto);

        List<ProvinciaCantidadCategoria> pcd = estadisticasDTO.getTopProvinciaPorCategoria().stream()
                .map(this::mapProvinciaCantidadCategoria)
                .toList();

        estadistica.getProvinciaPorCategoria().addAll(pcd);

        List<HoraCantidad> hc = estadisticasDTO.getHoraPicoPorCategoria().stream()
                .map(this::mapHoraCantidad)
                .toList();
        estadistica.getHoraPicoPorCategoria().addAll(hc);

        estadistica.setCantidadSpam(estadisticasDTO.getCantidadSpam());

        return estadistica;
    }

    private ProvinciaCantidadCategoria mapProvinciaCantidadCategoria(ProvinciaCantidadCategoriaDTO dto) {
        ProvinciaCantidadCategoria provinciaCantidadCategoria = new ProvinciaCantidadCategoria();
        provinciaCantidadCategoria.setProvincia(dto.getProvincia());
        provinciaCantidadCategoria.setCantidad(dto.getCantidad());
        provinciaCantidadCategoria.setCategoria(dto.getCategoria());
        return provinciaCantidadCategoria;
    }

    private ProvinciaCantidad mapProvinciaCantidad(ProvinciaCantidadDTO dto) {
        ProvinciaCantidad provinciaCantidad = new ProvinciaCantidad();
        provinciaCantidad.setIdColeccion(dto.getIdColeccion());
        provinciaCantidad.setTituloColeccion(dto.getTituloColeccion());
        provinciaCantidad.setProvincia(dto.getProvincia());
        provinciaCantidad.setCantidad(dto.getCantidad());
        return provinciaCantidad;
    }

    private HoraCantidad mapHoraCantidad(HoraCantidadDTO dto) {
        System.out.println(dto.getHora() + "," + dto.getCantidad() + "," + dto.getCategoria());
        HoraCantidad fechaCantidad = new HoraCantidad();
        fechaCantidad.setHora(dto.getHora());
        fechaCantidad.setCantidad(dto.getCantidad());
        fechaCantidad.setCategoria(dto.getCategoria());
        return fechaCantidad;
    }

}