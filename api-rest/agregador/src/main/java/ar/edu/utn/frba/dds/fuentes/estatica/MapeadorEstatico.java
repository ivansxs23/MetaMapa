package ar.edu.utn.frba.dds.fuentes.estatica;

import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.Origen;
import ar.edu.utn.frba.dds.entities.TipoFuente;
import ar.edu.utn.frba.dds.entities.Ubicacion;
import ar.edu.utn.frba.dds.entities.dto.input.HechoEstaticoDto;
import ar.edu.utn.frba.dds.services.impl.CategoriaService;
import ar.edu.utn.frba.dds.utils.HechosUtils;
import org.springframework.stereotype.Component;

@Component
public class MapeadorEstatico {
    private final CategoriaService categoriaService;

    public MapeadorEstatico(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    public Hecho aDominio(HechoEstaticoDto dto) {
        Hecho h = new Hecho();
        h.setActivo(true);
        h.setTitulo(dto.getTitulo());
        h.setDescripcion(dto.getDescripcion());
        h.setCategoria(categoriaService.buscarOCrearCategoria(dto.getCategoria().getNombre()));

        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLatitud(dto.getUbicacion().getLatitud());
        ubicacion.setLongitud(dto.getUbicacion().getLongitud());
        ubicacion.setProvincia(dto.getUbicacion().getProvincia());
        h.setUbicacion(ubicacion);

        h.setFechaAcontecimiento(dto.getFechaHecho());
        h.setFechaCreacion(dto.getFechaCreacion());
        h.setUltimaFechaModificacion(dto.getFechaModificacion());
        h.setEsAnonimo(false);
        h.getOrigen().add(new Origen(TipoFuente.DATASET, dto.getId(), dto.getOrigen()));

        h.setIdentificador(HechosUtils.generarIdentificador(h));
        return h;
    }
}
