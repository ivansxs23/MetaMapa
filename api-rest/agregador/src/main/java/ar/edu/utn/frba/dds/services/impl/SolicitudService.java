package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.Origen;
import ar.edu.utn.frba.dds.entities.TipoFuente;
import ar.edu.utn.frba.dds.entities.detectorDeSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.entities.dto.input.FiltroSolicitudDTO;
import ar.edu.utn.frba.dds.entities.dto.input.SolicitudInputDTO;
import ar.edu.utn.frba.dds.entities.dto.output.NuevoHechoDinamicaDTO;
import ar.edu.utn.frba.dds.entities.dto.output.SolicitudOutputDTO;
import ar.edu.utn.frba.dds.entities.solicitud.EstadoSolicitud;
import ar.edu.utn.frba.dds.entities.solicitud.Solicitud;
import ar.edu.utn.frba.dds.entities.solicitud.TipoSolicitud;
import ar.edu.utn.frba.dds.excepciones.EntityNotFoundException;
import ar.edu.utn.frba.dds.excepciones.RequestAlreadyResolvedException;
import ar.edu.utn.frba.dds.fuentes.dinamica.FuenteDinamica;
import ar.edu.utn.frba.dds.repositories.ISolicitudRepository;
import ar.edu.utn.frba.dds.services.ISolicitudService;
import ar.edu.utn.frba.dds.utils.HechoMapper;
import ar.edu.utn.frba.dds.utils.SolicitudMapper;
import jakarta.persistence.criteria.JoinType;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class SolicitudService implements ISolicitudService {
  private final ISolicitudRepository solicitudRepository;
  private final DetectorDeSpam detectorDeSpam;
  private final SolicitudMapper solicitudMapper;
  private final FuenteDinamica fuenteDinamicaClient;
  private final HechoService hechoService;
  private final HechoMapper hechoMapper;

  public SolicitudService(ISolicitudRepository solicitudRepository, DetectorDeSpam detectorDeSpam, SolicitudMapper solicitudMapper, FuenteDinamica fuenteDinamica, HechoService hechoService, HechoMapper hechoMapper) {
    this.solicitudRepository = solicitudRepository;
    this.detectorDeSpam = detectorDeSpam;
    this.solicitudMapper = solicitudMapper;
    this.fuenteDinamicaClient = fuenteDinamica;
    this.hechoService = hechoService;
    this.hechoMapper = hechoMapper;
  }

  private Solicitud findById(Long id) {
    return solicitudRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Solicitud con id: " + id + " no encontrada"));
  }

  @Override
  public void aprobarSolicitud(Long id) {
    Solicitud solicitud = findById(id);

    if(solicitud.getEstado() == EstadoSolicitud.APROBADA) {
      throw new RequestAlreadyResolvedException("La solicitud ya ha sido aprobada");
    }
    try {
      switch (solicitud.getTipo()) {

        case CREACION -> aplicarCreacion(solicitud);

        case EDICION -> aplicarEdicion(solicitud);

        case ELIMINACION -> aplicarEliminacion(solicitud);

        default -> throw new IllegalArgumentException("Tipo de solicitud inválido");
      }
    } catch (Exception e) {
      throw new RuntimeException("Error al aprobar la solicitud: " + e.getMessage());
    }
    solicitud.setEstado(EstadoSolicitud.APROBADA);
    solicitud.setAdministrador(obtenerUsuarioActual());
    solicitud.setFechaResolucion(LocalDateTime.now());
    solicitudRepository.save(solicitud);
  }

  private void aplicarCreacion(Solicitud s) {

    if (s.getDatosNuevos() == null) {
      throw new IllegalArgumentException("datosNuevos es obligatorio para creación");
    }
    NuevoHechoDinamicaDTO nuevoHechoDinamicaDTO = hechoMapper.parsearANuevoHechoDinamicaDTO(s.getDatosNuevos());
    nuevoHechoDinamicaDTO.setUsuario(s.getSolicitante());
    Hecho hecho = fuenteDinamicaClient.crearHecho(nuevoHechoDinamicaDTO).block();
    if(hecho == null) {
      throw new RuntimeException("Error al crear el hecho en la fuente dinámica");
    }
    hechoService.guardar(hecho);

  }

  private void aplicarEdicion(Solicitud s) {
    if (s.getHecho() == null) throw new IllegalArgumentException("Solicitud de edición debe referenciar un hecho");
    if (s.getDatosNuevos() == null) throw new IllegalArgumentException("datosNuevos es obligatorio para edición");

    Hecho hechoExistente = hechoService.obtenerHechoPorId(s.getHecho().getId());

    Long hechoFuenteId = hechoExistente.getOrigen().stream()
        .filter(o -> o.getTipoFuente() == TipoFuente.CONTRIBUYENTE && o.getIdEnFuente() != null)
        .findFirst()
        .map(Origen::getIdEnFuente)
        .orElseThrow(() -> new IllegalStateException("Este hecho no es editable: no tiene origen DINAMICA"));

    NuevoHechoDinamicaDTO dto = hechoMapper.parsearANuevoHechoDinamicaDTO(s.getDatosNuevos());
    dto.setUsuario(s.getSolicitante());
    dto.setArchivos(Collections.emptyList()); // o no setear

    Hecho actualizado;
    try {
      actualizado = fuenteDinamicaClient.editarHecho(hechoFuenteId, dto).block();
    } catch (WebClientResponseException ex) {
      throw new RuntimeException("Error al editar en dinámica. status=" + ex.getStatusCode()
          + " body=" + ex.getResponseBodyAsString(), ex);
    }

    if (actualizado == null) throw new RuntimeException("Error al editar en dinámica: respuesta null");

    // Elegí una:
    hechoService.guardarTodos(List.of(actualizado));
    // o merge y guardar hechoExistente
  }


  private void aplicarEliminacion(Solicitud s) {

    if (s.getHecho() == null) {
      throw new IllegalArgumentException("Solicitud de eliminación debe referenciar un hecho");
    }
    if (s.getMotivo() == null || s.getMotivo().isBlank()) {
      throw new IllegalArgumentException("motivo es obligatorio para eliminación");
    }

    hechoService.eliminarHecho(s.getHecho().getId());
  }
  @Override
  public void denegarSolicitud(Long id) {
    Solicitud solicitud = findById(id);
    if(solicitud.getEstado() == EstadoSolicitud.DENEGADA) {
      throw new RequestAlreadyResolvedException("La solicitud ya ha sido denegada");
    }
    solicitud.setEstado(EstadoSolicitud.DENEGADA);
    solicitud.setAdministrador(obtenerUsuarioActual());
    solicitud.setFechaResolucion(LocalDateTime.now());
    solicitudRepository.save(solicitud);
  }

  private boolean esSpam(String motivo) {
    return detectorDeSpam.esSpam(motivo);
  }
  private String obtenerUsuarioActual() {
    var auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth == null || auth instanceof AnonymousAuthenticationToken) {
      return "anonymousUser";
    }

    return auth.getName();
  }
  @Override
  public void crearSolicitud(SolicitudInputDTO dto) {
    TipoSolicitud tipo = dto.getTipo();

    switch (tipo) {
      case CREACION -> crearSolicitudCreacion(dto);
      case EDICION -> crearSolicitudEdicion(dto);
      case ELIMINACION -> crearSolicitudEliminacion(dto);
      default -> throw new IllegalArgumentException("Tipo de solicitud inválido");
    }
  }

  private void crearSolicitudCreacion(SolicitudInputDTO dto) {

    if (dto.getDatosNuevos() == null)
      throw new IllegalArgumentException("datosNuevos es obligatorio para creacion");

    Solicitud solicitud = new Solicitud();
    solicitud.setTipo(TipoSolicitud.CREACION);
    solicitud.setHecho(null);
    solicitud.setDatosNuevos(hechoMapper.parsearAJson(dto.getDatosNuevos()));
    solicitud.setMotivo(null);
    solicitud.setSolicitante(obtenerUsuarioActual());
    solicitud.setEstado(EstadoSolicitud.PENDIENTE);
    solicitud.setFechaCreacion(LocalDateTime.now());

    solicitudRepository.save(solicitud);
  }

  private void crearSolicitudEdicion(SolicitudInputDTO dto) {

    if (dto.getIdHecho() == null)
      throw new IllegalArgumentException("hechoId es obligatorio para edición");

    if (dto.getDatosNuevos() == null)
      throw new IllegalArgumentException("datosNuevos es obligatorio para edición");

    Hecho hecho = hechoService.obtenerHechoPorId(dto.getIdHecho());
    if (esEditable(hecho, obtenerUsuarioActual())) {
      Solicitud solicitud = new Solicitud();
      solicitud.setTipo(TipoSolicitud.EDICION);
      solicitud.setHecho(hecho);
      solicitud.setDatosNuevos(hechoMapper.parsearAJson(dto.getDatosNuevos()));
      solicitud.setMotivo(null);
      solicitud.setSolicitante(obtenerUsuarioActual());
      solicitud.setEstado(EstadoSolicitud.PENDIENTE);
      solicitud.setFechaCreacion(LocalDateTime.now());

      solicitudRepository.save(solicitud);
    }else{
      throw new RuntimeException("El hecho solo se puede editar por el autor");
    }
  }
  private void crearSolicitudEliminacion(SolicitudInputDTO dto) {
    System.out.println("Creando Solicitud de eliminacion");
    if (dto.getIdHecho() == null)
      throw new IllegalArgumentException("hechoId es obligatorio para eliminación");

    if (dto.getMotivo() == null || dto.getMotivo().isBlank())
      throw new IllegalArgumentException("motivo es obligatorio para eliminación");

    Hecho hecho = hechoService.obtenerHechoPorId(dto.getIdHecho());
    Solicitud solicitud = new Solicitud();
    solicitud.setTipo(TipoSolicitud.ELIMINACION);
    solicitud.setHecho(hecho);
    solicitud.setMotivo(dto.getMotivo());
    solicitud.setDatosNuevos(null);
    solicitud.setSolicitante(obtenerUsuarioActual());
    solicitud.setFechaCreacion(LocalDateTime.now());
    if(esSpam(dto.getMotivo())){
      solicitud.setEstado(EstadoSolicitud.DENEGADA);
      solicitud.setEsSpam(true);
    } else {
      solicitud.setEstado(EstadoSolicitud.PENDIENTE);
      solicitud.setEsSpam(false);
    }

    solicitudRepository.save(solicitud);
  }

@Override
  public List<SolicitudOutputDTO> obtenerTodas(FiltroSolicitudDTO f) {
      Specification<Solicitud> spec = Specification.where(null);

      if (f.getId() != null) {
        spec = spec.and((root, q, cb) -> cb.equal(root.get("id"), f.getId()));
      }
      if (f.getTipo() != null) {
        spec = spec.and((root, q, cb) -> cb.equal(root.get("tipo"), f.getTipo()));
      }
      if (f.getEstado() != null) {
        spec = spec.and((root, q, cb) -> cb.equal(root.get("estado"), f.getEstado()));
      }
      if (f.getHechoId() != null) {
        spec = spec.and((root, q, cb) ->
            cb.equal(root.join("hecho", JoinType.LEFT).get("id"), f.getHechoId()));
        // o root.get("hecho").get("id") si no querés join explícito
      }
      if (f.getEsSpam() != null) {
        spec = spec.and((root, q, cb) -> cb.equal(root.get("esSpam"), f.getEsSpam()));
      }

      if (f.getSolicitante() != null && !f.getSolicitante().isBlank()) {
        String like = "%" + f.getSolicitante().trim().toLowerCase() + "%";
        spec = spec.and((root, q, cb) ->
            cb.like(cb.lower(root.get("solicitante")), like));
      }

      if (f.getAdministrador() != null && !f.getAdministrador().isBlank()) {
        String like = "%" + f.getAdministrador().trim().toLowerCase() + "%";
        spec = spec.and((root, q, cb) ->
            cb.like(cb.lower(root.get("administrador")), like));
      }
      List<Solicitud> solicitudes = solicitudRepository.findAll(spec);
      return solicitudes.stream().map(solicitudMapper::aDto).toList();
    }
  private Boolean esEditable(Hecho hecho, String usuarioActual) {
    if (usuarioActual == null) {
      return false;
    }

    // ¿El usuario participa como origen contribuyente?
    boolean esCreador = hecho.getOrigen().stream()
        .anyMatch(origen ->
            origen.getTipoFuente().equals(TipoFuente.CONTRIBUYENTE) && !origen.getRaiz().equals("anonymousUser") &&
                origen.getRaiz().equals(usuarioActual)
        );

    // ¿Todavía no pasaron 7 días desde que se creó?
    boolean dentroDelTiempo = hecho.getFechaCreacion()
        .plusDays(7)
        .isAfter(LocalDateTime.now());

    return esCreador && dentroDelTiempo;
  }
  }

