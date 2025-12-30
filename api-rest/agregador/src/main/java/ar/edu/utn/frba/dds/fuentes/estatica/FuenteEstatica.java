package ar.edu.utn.frba.dds.fuentes.estatica;

import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.TipoFuente;
import ar.edu.utn.frba.dds.entities.dto.input.HechoEstaticoDto;
import ar.edu.utn.frba.dds.entities.dto.input.MetadataDTO;
import ar.edu.utn.frba.dds.entities.dto.input.PageResponse;
import ar.edu.utn.frba.dds.fuentes.IFuente;
import java.time.LocalDateTime;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class FuenteEstatica implements IFuente {
    private final ClienteEstatica fuente;
    private final MapeadorEstatico mapper;
    private static final Logger logger = LoggerFactory.getLogger(FuenteEstatica.class);

    public FuenteEstatica(ClienteEstatica fuente, MapeadorEstatico mapper) {
        this.fuente = fuente;
        this.mapper = mapper;
    }


    @Override
    public TipoFuente get() {
        return TipoFuente.DATASET;
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

                PageResponse<HechoEstaticoDto> pagina = fuente.buscarHechosPaginable(page, 100, fecha);
                if (pagina.getContent() == null || pagina.getContent().isEmpty()) {
                    finished = true;
                    logger.info("FUENTE_ESTATICA: no hay hechos a importar");
                    return List.of();
                }

                List<Hecho> hechos = pagina.getContent().stream()
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
}
