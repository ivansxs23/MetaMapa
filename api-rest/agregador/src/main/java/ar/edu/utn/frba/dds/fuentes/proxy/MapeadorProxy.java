package ar.edu.utn.frba.dds.fuentes.proxy;

import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.Origen;
import ar.edu.utn.frba.dds.entities.TipoFuente;
import ar.edu.utn.frba.dds.entities.Ubicacion;
import ar.edu.utn.frba.dds.entities.dto.input.ProxyDto.HechoProxyDto;
import ar.edu.utn.frba.dds.utils.HechosUtils;
import java.time.OffsetDateTime;

import ar.edu.utn.frba.dds.services.impl.CategoriaService;
import org.springframework.stereotype.Component;

@Component
public class MapeadorProxy {
    private final CategoriaService categoriaService;

    public MapeadorProxy(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    public Hecho aDominio(HechoProxyDto dto) {
        Hecho h = new Hecho();
        h.setActivo(true);
        h.setTitulo(dto.getTitulo());
        h.setDescripcion(dto.getDescripcion());
        h.setCategoria(categoriaService.buscarOCrearCategoria(dto.getCategoria()));

        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLatitud(dto.getUbicacion().getLatitud());
        ubicacion.setLongitud(dto.getUbicacion().getLongitud());
        ubicacion.setProvincia(dto.getUbicacion().getProvincia());
        h.setUbicacion(ubicacion);

        h.setFechaAcontecimiento(OffsetDateTime.parse(dto.getFecha_hecho()).toLocalDateTime());
        h.setFechaCreacion(OffsetDateTime.parse(dto.getCreated_at()).toLocalDateTime());
        h.setUltimaFechaModificacion(OffsetDateTime.parse(dto.getUpdated_at()).toLocalDateTime());
        h.setEsAnonimo(false);
        h.getOrigen().add(new Origen(TipoFuente.PROXY, dto.getId(), dto.getOrigen()));

        h.setIdentificador(HechosUtils.generarIdentificador(h));
        return h;
    }
}
