package ar.edu.utn.frba.dds.fuentes.dinamica;

import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.Archivo;
import ar.edu.utn.frba.dds.entities.Origen;
import ar.edu.utn.frba.dds.entities.TipoFuente;
import ar.edu.utn.frba.dds.entities.Ubicacion;
import ar.edu.utn.frba.dds.entities.dto.input.HechoDinamicoDto;
import ar.edu.utn.frba.dds.entities.dto.input.ArchivoDTO;
import ar.edu.utn.frba.dds.services.impl.CategoriaService;
import ar.edu.utn.frba.dds.utils.HechosUtils;
import org.springframework.stereotype.Component;

@Component
public class MapeadorDinamico {
    private final CategoriaService categoriaService;


    public MapeadorDinamico(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    public Hecho aDominio(HechoDinamicoDto dto) {
        Hecho h = new Hecho();
        h.setActivo(true);
        h.setTitulo(dto.getTitulo());
        h.setDescripcion(dto.getDescripcion());
        h.setCategoria(categoriaService.buscarPorId(dto.getIdCategoria()));

        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLatitud(dto.getUbicacion().getLatitud());
        ubicacion.setLongitud(dto.getUbicacion().getLongitud());
        ubicacion.setProvincia(dto.getUbicacion().getProvincia());

        h.setUbicacion(ubicacion);

        if (dto.getArchivos() != null && !dto.getArchivos().isEmpty()) {
            for (ArchivoDTO mDto : dto.getArchivos()) {
                Archivo archivo = new Archivo();
                archivo.setExternalId(mDto.getId());
                archivo.setUrl(mDto.getUrl());
                archivo.setTipo(mDto.getTipo());
                archivo.setHecho(h);
                h.getArchivos().add(archivo);
            }

        }

        h.setFechaAcontecimiento(dto.getFechaAcontecimiento());
        h.setFechaCreacion(dto.getFechaCarga());
        h.setUltimaFechaModificacion(dto.getUltimaEdicion());
        h.setEsAnonimo(dto.getEsAnonimo());
        h.getOrigen().add(new Origen(TipoFuente.CONTRIBUYENTE, dto.getId(), dto.getUsername()));
        h.setIdentificador(HechosUtils.generarIdentificador(h));
        return h;
    }
}
