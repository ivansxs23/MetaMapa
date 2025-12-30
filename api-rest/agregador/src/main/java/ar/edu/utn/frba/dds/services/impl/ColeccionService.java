package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.async.ColeccionActualizadaEvent;
import ar.edu.utn.frba.dds.entities.*;
import ar.edu.utn.frba.dds.entities.algoritmoDeConsenso.AlgoritmoDeConsenso;
import ar.edu.utn.frba.dds.entities.criterios.Criterio;
import ar.edu.utn.frba.dds.entities.dto.input.FiltroBusquedaHechosDTO;
import ar.edu.utn.frba.dds.entities.dto.input.coleccion.ColeccionDTO;
import ar.edu.utn.frba.dds.entities.dto.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.entities.dto.output.HechoOutputDto;
import ar.edu.utn.frba.dds.excepciones.EntityNotFoundException;
import ar.edu.utn.frba.dds.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.services.IHechoService;
import ar.edu.utn.frba.dds.utils.*;
import ar.edu.utn.frba.dds.repositories.IColeccionRepository;
import ar.edu.utn.frba.dds.services.IColeccionService;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ColeccionService implements IColeccionService {

  private final ColeccionFactory coleccionFactory;
  private final IColeccionRepository coleccionRepository;
  private final IHechoService hechoService;
  private final ColeccionMapper coleccionMapper;
  private final AlgoritmoFactory algoritmoFactory;
  private final IHechoRepository hechoRepository;
  private final CriterioFactory criterioFactory;
  private final ApplicationEventPublisher applicationEventPublisher;
  private static final Logger logger = LoggerFactory.getLogger(ColeccionService.class);

  public ColeccionService(ColeccionFactory coleccionFactory, IColeccionRepository coleccionRepository, IHechoService hechoService, ColeccionMapper coleccionMapper, AlgoritmoFactory algoritmoFactory, IHechoRepository hechoRepository, CriterioFactory criterioFactory, ApplicationEventPublisher applicationEventPublisher) {
    this.coleccionFactory = coleccionFactory;
    this.coleccionRepository = coleccionRepository;
    this.hechoService = hechoService;
    this.coleccionMapper = coleccionMapper;
    this.algoritmoFactory = algoritmoFactory;
    this.hechoRepository = hechoRepository;
    this.criterioFactory = criterioFactory;
    this.applicationEventPublisher = applicationEventPublisher;
  }
  @Override
  @Transactional
  public List<ColeccionOutputDTO> obtenerTodasColecciones() {
    List<ColeccionOutputDTO> coleccionOutputDTO = new ArrayList<>();
    List<Coleccion> coleccion = coleccionRepository.findAll();
    for (Coleccion c : coleccion) {
      coleccionOutputDTO.add(coleccionMapper.aDto(c));
    }
    return coleccionOutputDTO;
  }

  public Coleccion buscarColeccionePorId(Long id) {
    return coleccionRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Coleccion con id: " + id + " no encontrada"));
  }
  public ColeccionOutputDTO obtenerColeccionPorId(Long id) {
    Coleccion coleccion = buscarColeccionePorId(id);
    return coleccionMapper.aDto(coleccion);
  }
  @Override
  public Page<HechoOutputDto> obtenerHechosDeColeccion(Long idColeccion, FiltroBusquedaHechosDTO filtrosDTO, Pageable pageable) {

    return hechoService.obtenerTodosHechos(filtrosDTO,idColeccion, pageable);

  }

  @Override
  @Transactional
  public void guardarColeccion(ColeccionDTO coleccionDto) {
    Coleccion coleccion = coleccionFactory.aDominio(coleccionDto);
    Coleccion coleccionCreado = coleccionRepository.save(coleccion);
    applicationEventPublisher.publishEvent(
        new ColeccionActualizadaEvent(coleccionCreado.getId())
    );
  }
  @Transactional
  public Coleccion editarColeccion(Long idColeccion, ColeccionDTO coleccionDto) {
    Coleccion coleccionExistente = buscarColeccionePorId(idColeccion);

    if (coleccionDto.getTitulo() != null) {
      coleccionExistente.setTitulo(coleccionDto.getTitulo());
    }

    if (coleccionDto.getDescripcion() != null) {
      coleccionExistente.setDescripcion(coleccionDto.getDescripcion());
    }

    if (coleccionDto.getAlgoritmo() != null) {
      AlgoritmoDeConsenso algoritmo = algoritmoFactory.aDominio(coleccionDto.getAlgoritmo());
      coleccionExistente.setAlgoritmo(algoritmo);
    }

    if (coleccionDto.getFuentes() != null) {
      if (!coleccionDto.getFuentes().isEmpty()) {
        coleccionExistente.getTiposFuente().clear();
        coleccionExistente.getTiposFuente().addAll(coleccionDto.getFuentes());
      }
    }
    // Criterios
    coleccionExistente.getCriterioDePertenencia().clear();
    if (coleccionDto.getCriterios() != null) {
      if (!coleccionDto.getCriterios().isEmpty()) {
        List<Criterio> nuevosCriterios = coleccionDto.getCriterios().stream()
            .map(criterioFactory::aDominio)
            .toList();
        coleccionExistente.getCriterioDePertenencia().addAll(nuevosCriterios);
      }
    }
    Coleccion coleccion = coleccionRepository.save(coleccionExistente);
    applicationEventPublisher.publishEvent(
        new ColeccionActualizadaEvent(coleccion.getId())
    );
    return coleccion;
  }

  @Override
  public void eliminarColeccion(Long idColeccion) {
    Coleccion coleccion = buscarColeccionePorId(idColeccion);
    coleccionRepository.delete(coleccion);
  }

  @Override
  @Transactional
  public void actualizarColecciones(){
    List<Long> colecciones = coleccionRepository.findAllIds();
    for (Long coleccionId : colecciones) {
      applicationEventPublisher.publishEvent(
          new ColeccionActualizadaEvent(coleccionId)
      );
    }
    System.out.println("Actualizacion finalizada");
  }

  @Transactional
  public void actualizarColeccion(Long coleccionId) {
    Coleccion coleccion = coleccionRepository.findById(coleccionId)
        .orElseThrow();

    // 1) candidatos: hechos cambiados desde la última importación (DB)
    Specification<Hecho> spec = Specification
        .where(HechoSpecifications.activo(true))
        .and(HechoSpecifications.origenTipoIn(coleccion.getTiposFuente()));

    List<Hecho> hechosCambiados = hechoRepository.findAll(spec);

    // 3) sincronizar pertenencia
    for (Hecho hecho : hechosCambiados) {
      boolean cumple = coleccion.getCriterioDePertenencia().isEmpty()
          || coleccion.getCriterioDePertenencia().stream().allMatch(c -> c.cumple(hecho));

      if (cumple) {
        coleccion.agregarHecho(hecho);   // asegurate que no duplique ColeccionHecho
      }
    }

    // 4) limpiar obsoletos (según tu regla)
    coleccion.eliminarHechosObsoletos();

    // 5) marcar importación
    coleccion.setUltimaFechaImportacion(LocalDateTime.now());
    coleccionRepository.save(coleccion);
    logger.info("Colecion {} actualizada", coleccionId);
  }
  @Transactional
  public void consensuarHechosDeColeccion(Long id) {
    Coleccion coleccion = coleccionRepository.findById(id)
        .orElseThrow();

    coleccion.getAlgoritmo().consensuarHechos(coleccion);
  }

}
