package ar.edu.utn.frba.dds.services.impl;


import ar.edu.utn.frba.dds.entities.AgregadorData;
import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.fuentes.IFuente;
import ar.edu.utn.frba.dds.repositories.IAgregadorDataRepository;
import ar.edu.utn.frba.dds.services.IColeccionService;
import ar.edu.utn.frba.dds.services.IHechoService;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AgregadorService {
  private final List<IFuente> fuentes;
  private final IHechoService hechoService;
  private final IAgregadorDataRepository agregadorDataRepository;
  private final IColeccionService coleccionService;
  private static final Logger logger = LoggerFactory.getLogger(AgregadorService.class);

  public AgregadorService(List<IFuente> fuentes, IHechoService hechoService, IAgregadorDataRepository agregadorDataRepository, IColeccionService coleccionService) {
    this.fuentes = fuentes;
    this.hechoService = hechoService;
    this.agregadorDataRepository = agregadorDataRepository;
    this.coleccionService = coleccionService;
  }

  public void importarHechosYActualizarColecciones() {
    logger.info("Iniciando Importacion de las fuentes");

    AgregadorData state = agregadorDataRepository.getOrCreate();
    LocalDateTime maxProcesado = state.getFechaImportacion() != null
        ? state.getFechaImportacion()
        : LocalDateTime.of(1900, 1, 1, 0, 0,0);

    for (IFuente fuente : fuentes) {
      for (List<Hecho> hechos : fuente.importarHechosPaginado(maxProcesado)) {
        if (hechos == null || hechos.isEmpty()) continue;
        hechoService.guardarTodos(hechos);

      }
      logger.info("Importacion de hecho exitosa: {}", fuente.get());
    }
    state.setFechaImportacion(LocalDateTime.now());
    agregadorDataRepository.save(state);
    logger.info("Importacion finalizada");
    coleccionService.actualizarColecciones();
    logger.info("Actualizando Colecciones");

  }
}
