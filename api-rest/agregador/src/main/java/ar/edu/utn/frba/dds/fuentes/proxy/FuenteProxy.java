package ar.edu.utn.frba.dds.fuentes.proxy;

import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.TipoFuente;
import ar.edu.utn.frba.dds.entities.dto.input.MetadataDTO;
import ar.edu.utn.frba.dds.entities.dto.input.ProxyDto.ProxyResponse;
import ar.edu.utn.frba.dds.fuentes.IFuente;
import ar.edu.utn.frba.dds.fuentes.estatica.FuenteEstatica;
import java.time.LocalDateTime;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class FuenteProxy implements IFuente {
    private final ClienteProxy fuente;
    private final MapeadorProxy mapper;
    private static final Logger logger = LoggerFactory.getLogger(FuenteProxy.class);

    @Override
    public TipoFuente get() {
        return TipoFuente.PROXY;
    }
    
    public FuenteProxy(ClienteProxy fuente, MapeadorProxy mapper) {
        this.fuente = fuente;
        this.mapper = mapper;
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

                ProxyResponse pagina = fuente.buscarHechosPaginable(page, 100, fecha);
                if (pagina.getData() == null || pagina.getData().isEmpty()) {
                    finished = true;
                    logger.info("FUENTE_PROXY: no hay hechos a importar");
                    return List.of();
                }

                List<Hecho> hechos = pagina.getData().stream()
                    .map(mapper::aDominio)
                    .toList();


                boolean last = (pagina.getNext_page_url()==null);
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
        return null;
    }
}
