package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.repositories.IColeccionRepository;
import ar.edu.utn.frba.dds.services.IAlgoritmoService;
import ar.edu.utn.frba.dds.services.IColeccionService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AlgoritmoService implements IAlgoritmoService {
  private final IColeccionRepository coleccionRepository;
  private final IColeccionService coleccionService;
  private static final Logger logger = LoggerFactory.getLogger(AlgoritmoService.class);

  public AlgoritmoService(IColeccionRepository coleccionRepository, IColeccionService coleccionService) {
    this.coleccionRepository = coleccionRepository;
    this.coleccionService = coleccionService;
  }

  public void consensuarHechos() {
    logger.info("INICION DE CONSENSO");
    List<Long> ids = coleccionRepository.findAllIds();

    for (Long id : ids) {
      coleccionService.consensuarHechosDeColeccion(id);
      logger.info("hechos de coleccion {} consensuados", id);
    }
  }



}
