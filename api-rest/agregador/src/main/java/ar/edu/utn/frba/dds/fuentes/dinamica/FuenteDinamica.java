package ar.edu.utn.frba.dds.fuentes.dinamica;
import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.TipoFuente;
import ar.edu.utn.frba.dds.entities.dto.input.HechoDinamicoDto;
import ar.edu.utn.frba.dds.entities.dto.input.MetadataDTO;
import ar.edu.utn.frba.dds.entities.dto.input.PageResponse;
import ar.edu.utn.frba.dds.entities.dto.output.NuevoHechoDinamicaDTO;
import ar.edu.utn.frba.dds.fuentes.IFuente;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class FuenteDinamica implements IFuente {
    private final ClienteDinamica fuente;
    private final MapeadorDinamico mapper;
    private static final Logger logger = LoggerFactory.getLogger(FuenteDinamica.class);

    public FuenteDinamica(ClienteDinamica fuente, MapeadorDinamico mapper) {
        this.fuente = fuente;
        this.mapper = mapper;
    }

    @Override
    public TipoFuente get() {
        return TipoFuente.CONTRIBUYENTE;
    }

    @Override
    public Mono<List<Hecho>> importarHechos() {
        return fuente.buscarHechos()
                .map(dtos -> dtos.stream()
                        .map(mapper::aDominio)
                        .toList());
    }

    @Override
    public Iterable<List<Hecho>> importarHechosPaginado(LocalDateTime fecha) {
        return () -> new Iterator<>() {
            private int page = 0;
            private boolean finished = false;

            @Override
            public boolean hasNext() {
                return !finished;
            }

            @Override
            public List<Hecho> next() {
                if (finished) {
                    throw new java.util.NoSuchElementException("No hay más páginas");
                }

                PageResponse<HechoDinamicoDto> pagina = fuente.buscarHechosPaginable(page, 100, fecha);
                if (pagina.getContent() == null || pagina.getContent().isEmpty()) {
                    finished = true;
                    logger.info("FUENTE_DINAMICA: no hay hechos a importar");
                    return List.of();
                }

                List<HechoDinamicoDto> content = pagina.getContent();
                List<Hecho> hechos = content.stream()
                    .map(mapper::aDominio)
                    .toList();


                boolean last = pagina.isLast();
                page++;

                if (last) {
                    finished = true;
                }

                return hechos;
            }
        };
    }

    @Override
    public MetadataDTO getMetadata() {
        return fuente.buscarMetadata();
    }

    public Mono<Hecho> crearHecho(NuevoHechoDinamicaDTO hecho){
        return fuente.crearHecho(hecho)
                .map(mapper::aDominio);
    }
    public Mono<Hecho> editarHecho(Long id, NuevoHechoDinamicaDTO hecho){
        return fuente.editarHecho(id, hecho)
                .map(mapper::aDominio);
    }
}
