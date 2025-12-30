package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.Exceptions.NotEditableException;
import ar.edu.utn.frba.dds.entities.Archivo;
import ar.edu.utn.frba.dds.entities.ArchivoDTO;
import ar.edu.utn.frba.dds.entities.Estado;
import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.InputHechoDto;
import ar.edu.utn.frba.dds.entities.Ubicacion;
import ar.edu.utn.frba.dds.entities.filtro.FiltroHechoDTO;
import ar.edu.utn.frba.dds.entities.filtro.HechoSpecifications;
import ar.edu.utn.frba.dds.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.services.IHechoService;
import ar.edu.utn.frba.dds.services.IMetadataService;
import ar.edu.utn.frba.dds.utils.HechoFactory;
import ar.edu.utn.frba.dds.utils.IWebClient;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class HechoService implements IHechoService {

  private final IHechoRepository hechoRepository;
  private final IMetadataService metadataService;
  private final IWebClient webClient;
  private final HechoFactory hechoFactory;

  public HechoService(IHechoRepository hechoRepository, IMetadataService metadataService, IWebClient webClient, HechoFactory hechoFactory) {
    this.hechoRepository = hechoRepository;
    this.metadataService = metadataService;
    this.webClient = webClient;
    this.hechoFactory = hechoFactory;
  }


  public Page<Hecho> buscar(FiltroHechoDTO filtro, Pageable pageable) {
    return hechoRepository.findAll(HechoSpecifications.conFiltros(filtro), pageable);
  }

  @Override
  public Hecho agregarHecho(InputHechoDto nuevoHecho) {
    String provincia = webClient.obtenerProvinciaPorCoordenada(nuevoHecho.getLatitud(), nuevoHecho.getLongitud());
    Hecho hechoACargar = hechoFactory.createFromDto(nuevoHecho, provincia);
    Hecho hecho = hechoRepository.save(hechoACargar);
    metadataService.actualizarMetadata();
    return hecho;
  }

  public Hecho modificarHecho(Long id, InputHechoDto nuevoHecho) {
    Hecho hechoExistente = hechoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Hecho no encontrado"));

    if (!hechoExistente.getUsername().equals(nuevoHecho.getUsuario())) {
      throw new AccessDeniedException("No puedes editar un hecho que no te pertenece");
    }

    if(!hechoExistente.getEsEditable()){
      throw new NotEditableException(hechoExistente.getId());
    }
    hechoExistente.setTitulo(nuevoHecho.getTitulo());
    hechoExistente.setDescripcion(nuevoHecho.getDescripcion());
    hechoExistente.setFechaAcontecimiento(nuevoHecho.getFechaAcontecimiento());
    hechoExistente.setUltimaEdicion(LocalDateTime.now());

    if(!Objects.equals(hechoExistente.getUbicacion().getLatitud(), nuevoHecho.getLatitud()) || !Objects.equals(hechoExistente.getUbicacion().getLongitud(), nuevoHecho.getLongitud())){
      String provincia = webClient.obtenerProvinciaPorCoordenada(nuevoHecho.getLatitud(), nuevoHecho.getLongitud());

      Ubicacion ubicacion = new Ubicacion();
      ubicacion.setLatitud(nuevoHecho.getLatitud());
      ubicacion.setLongitud(nuevoHecho.getLongitud());
      ubicacion.setProvincia(provincia);

      hechoExistente.setUbicacion(ubicacion);
    }

    if (nuevoHecho.getArchivos() != null) {
      hechoExistente.getArchivos().clear();
      for (ArchivoDTO archivoDto : nuevoHecho.getArchivos()) {
        Archivo archivo = new Archivo();
        archivo.setUrl(archivoDto.getUrl());
        archivo.setTipo(archivoDto.getTipo());

        hechoExistente.getArchivos().add(archivo);
      }
    }

    return hechoRepository.save(hechoExistente);
  }

  @Override
  public void aprobarHecho(Long id) {
    Hecho hechoExistente = hechoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Hecho no encontrado"));
    hechoExistente.setEstado(Estado.APROBADO);
    hechoExistente.setVigente(true);
    hechoRepository.save(hechoExistente);
  }
  @Override
  public void rechazarHecho(Long id) {
    Hecho hechoExistente = hechoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Hecho no encontrado"));
    hechoExistente.setEstado(Estado.RECHAZADO);
    hechoRepository.save(hechoExistente);
  }
}
