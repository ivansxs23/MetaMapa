package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.TipoFuente;
import ar.edu.utn.frba.dds.entities.dto.input.FuenteResponse;
import ar.edu.utn.frba.dds.entities.dto.input.MetadataDTO;
import java.time.LocalDateTime;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IFuente {
    TipoFuente get();
    Mono<List<Hecho>> importarHechos();
    Iterable<List<Hecho>> importarHechosPaginado(LocalDateTime fecha);
    MetadataDTO getMetadata();
}
