package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.entities.Etiqueta;
import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.Archivo;
import ar.edu.utn.frba.dds.entities.HechoSpecifications;
import ar.edu.utn.frba.dds.entities.Origen;
import ar.edu.utn.frba.dds.entities.TipoFuente;
import ar.edu.utn.frba.dds.entities.dto.input.FiltroBusquedaHechosDTO;
import ar.edu.utn.frba.dds.entities.dto.output.HechoOutputDto;
import ar.edu.utn.frba.dds.excepciones.EntityNotFoundException;
import ar.edu.utn.frba.dds.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.services.IHechoService;
import ar.edu.utn.frba.dds.utils.HechoMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class HechoService implements IHechoService {

  private final IHechoRepository hechoRepository;
  private final GeoServiceClassic geoService;
  private final HechoMapper hechoMapper;

  public HechoService(IHechoRepository hechoRepository, GeoServiceClassic geoService, HechoMapper hechoMapper) {
    this.hechoRepository = hechoRepository;
    this.geoService = geoService;
    this.hechoMapper = hechoMapper;
  }

  @Override
  public Hecho obtenerHechoPorId(Long id) {
    return hechoRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Hecho con id: " + id + " no encontrado"));
  }
  @Override
  public HechoOutputDto obtenerHechoOutputDtoPorId(Long id) {
    Hecho hecho = obtenerHechoPorId(id);
    String usuarioActual = obtenerUsuarioActual();
    return hechoMapper.aDto(hecho, usuarioActual);
  }

  @Override
  public void eliminarHecho(Long id) {
    Hecho hecho = obtenerHechoPorId(id);
    hecho.setActivo(false);
    hecho.setUltimaFechaModificacion(LocalDateTime.now());
    hechoRepository.save(hecho);
  }

  @Override
  public Page<HechoOutputDto> obtenerTodosHechos(FiltroBusquedaHechosDTO filtrosDTO,Long idCollecion, Pageable pageable) {
    Page<Hecho> pagina = hechoRepository.findAll(HechoSpecifications.fromFiltro(filtrosDTO, idCollecion), pageable);
    String usuarioActual = obtenerUsuarioActual();

    List<HechoOutputDto> dtos = pagina.getContent().stream()
        .map(hecho -> hechoMapper.aDto(hecho, usuarioActual)) // <- esto requiere que DEVUELVA dto
        .toList();

    return new PageImpl<>(dtos, pageable, pagina.getTotalElements());
  }

  private void actualizarHecho(Hecho existente, Hecho nuevo) {
    existente.setTitulo(nuevo.getTitulo());
    existente.setDescripcion(nuevo.getDescripcion());
    existente.setCategoria(nuevo.getCategoria());
    String provincia = geoService.obtenerProvincia(nuevo.getUbicacion().getLatitud(), nuevo.getUbicacion().getLongitud());
    nuevo.getUbicacion().setProvincia(provincia);
    existente.setUbicacion(nuevo.getUbicacion());
    existente.setFechaAcontecimiento(nuevo.getFechaAcontecimiento());
    existente.setUltimaFechaModificacion(nuevo.getUltimaFechaModificacion());
    fusionarMedias(existente, nuevo);
  }
  private void fusionar(Hecho existente, Hecho nuevo) {
    for (Etiqueta etiqueta : nuevo.getEtiquetas()) {
      existente.agregarEtiqueta(new Etiqueta(etiqueta.getNombre()));
    }

    for (Origen origen : nuevo.getOrigen()) {
      existente.agregarOrigen(
          new Origen(origen.getTipoFuente(), origen.getIdEnFuente(), origen.getRaiz())
      );
    }
    fusionarMedias(existente, nuevo);
  }
  private void fusionarMedias(Hecho existente, Hecho nuevo) {

    // Por cada media importada…
    for (Archivo mNueva : nuevo.getArchivos()) {

      boolean yaExiste = existente.getArchivos().stream()
          .anyMatch(m -> Objects.equals(m.getExternalId(), mNueva.getExternalId()));

      if (!yaExiste) {
        Archivo m = new Archivo();
        m.setExternalId(mNueva.getExternalId());
        m.setUrl(mNueva.getUrl());
        m.setTipo(mNueva.getTipo());
        m.setHecho(existente);
        existente.getArchivos().add(m);
      }
    }
  }
  private String obtenerUsuarioActual() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
      return "anonymousUser";
    }
    return authentication.getName();
  }
  @Override
  @Transactional
  public Hecho guardar(Hecho importado) {
    if (importado == null) {
      throw new IllegalArgumentException("El hecho importado no puede ser null");
    }
    if (importado.getOrigen() == null || importado.getOrigen().isEmpty()) {
      throw new IllegalArgumentException("El hecho importado debe tener al menos un origen");
    }

    // 0) Recalcular identificador del importado (no asumir que cambia o no)
    String identificadorNuevo = importado.getIdentificador();
    importado.setIdentificador(identificadorNuevo);

    // Tomamos el "origen principal" (según tu diseño actual)
    Origen origenPrincipal = importado.getOrigen().get(0);
    TipoFuente tipo = origenPrincipal.getTipoFuente();
    Long idEnFuente = origenPrincipal.getIdEnFuente();

    // 1) Buscar por origen (tipoFuente + idEnFuente)
    Optional<Hecho> optViejo = hechoRepository
        .findByOrigen_TipoFuenteAndOrigen_IdEnFuente(tipo, idEnFuente);

    // Caso A: no existe por origen -> fusionar por identificador o crear nuevo
    if (optViejo.isEmpty()) {
      return fusionarPorIdentificadorOCrearNuevo(importado);
    }

    Hecho viejo = optViejo.get();

    // 2) Buscar destino por identificador (el "lugar" donde debería vivir este hecho)
    Optional<Hecho> optDestino = hechoRepository.findByIdentificador(identificadorNuevo);

    // 2.1) Si el destino es el mismo hecho, es update in-place
    if (optDestino.isPresent() && optDestino.get().getId().equals(viejo.getId())) {
      actualizarHecho(viejo, importado);     // pisa campos y mergea medias/etiquetas según tu estrategia
      // Aseguramos que el origen siga existiendo (por si vino incompleto)
      asegurarOrigen(viejo, origenPrincipal);
      return hechoRepository.save(viejo);
    }

    // 2.2) Si existe otro destino (hecho distinto), hay que mover el origen del viejo al destino y fusionar
    if (optDestino.isPresent()) {
      Hecho destino = optDestino.get();

      // Movemos el origen: sacarlo del viejo y agregarlo al destino
      eliminarOrigenExacto(viejo, tipo, idEnFuente);
      asegurarOrigen(destino, origenPrincipal);

      // Elegí acá qué “gana”: normalmente el importado trae la versión actual del hecho desde esa fuente
      actualizarHecho(destino, importado);

      // Persistimos ambos: destino actualizado, viejo con origen removido
      hechoRepository.save(viejo);
      return hechoRepository.save(destino);
    }

    // 2.3) No existe destino -> crear un hecho nuevo con ese identificador,
    //      pero antes hay que quitar el origen del viejo para que no quede duplicado el vínculo a la fuente
    eliminarOrigenExacto(viejo, tipo, idEnFuente);
    hechoRepository.save(viejo);

    // Completar datos del nuevo y persistir
    setearProvinciaSiHaceFalta(importado);
    return hechoRepository.save(importado);
  }

// ---------------- helpers (mínimos, alineados a tu modelo) ----------------

  private Hecho fusionarPorIdentificadorOCrearNuevo(Hecho importado) {
    Optional<Hecho> porIdentificador = hechoRepository.findByIdentificador(importado.getIdentificador());

    if (porIdentificador.isPresent()) {
      Hecho existente = porIdentificador.get();
      fusionar(existente, importado);
      return hechoRepository.save(existente);
    }

    setearProvinciaSiHaceFalta(importado);
    return hechoRepository.save(importado);
  }

  private void setearProvinciaSiHaceFalta(Hecho hecho) {
    if (hecho.getUbicacion() == null) return;
    if (hecho.getUbicacion().getProvincia() != null && !hecho.getUbicacion().getProvincia().isBlank() && !hecho.getUbicacion().getProvincia().equals("Desconocido")) return;

    String provincia = geoService.obtenerProvincia(
        hecho.getUbicacion().getLatitud(),
        hecho.getUbicacion().getLongitud()
    );
    hecho.getUbicacion().setProvincia(provincia);
  }

  private void asegurarOrigen(Hecho hecho, Origen origen) {
    boolean existe = hecho.getOrigen().stream()
        .anyMatch(o -> o.getTipoFuente() == origen.getTipoFuente()
            && Objects.equals(o.getIdEnFuente(), origen.getIdEnFuente()));

    if (!existe) {
      hecho.agregarOrigen(new Origen(origen.getTipoFuente(), origen.getIdEnFuente(), origen.getRaiz()));
    }
  }

  private void eliminarOrigenExacto(Hecho hecho, TipoFuente tipo, Long idEnFuente) {
    if (hecho.getOrigen() == null) return;
    hecho.getOrigen().removeIf(o -> o.getTipoFuente() == tipo && Objects.equals(o.getIdEnFuente(), idEnFuente));
  }
  @PersistenceContext
  private EntityManager em;

  @Transactional
  public void guardarTodos(List<Hecho> importados) {
    if (importados == null || importados.isEmpty()) return;

    List<Hecho> toSave = new ArrayList<>();

    for (Hecho importado : importados) {
      guardarSinPersistir(importado, toSave);
    }

    if (!toSave.isEmpty()) {
      hechoRepository.saveAll(toSave);
      hechoRepository.flush();
      em.clear();
    }
  }
  private Hecho guardarSinPersistir(Hecho importado, List<Hecho> toSave) {

    if (importado == null) throw new IllegalArgumentException("El hecho importado no puede ser null");
    if (importado.getOrigen() == null || importado.getOrigen().isEmpty())
      throw new IllegalArgumentException("El hecho importado debe tener al menos un origen");

    String identificadorNuevo = importado.getIdentificador();
    importado.setIdentificador(identificadorNuevo);

    Origen origenPrincipal = importado.getOrigen().get(0);
    TipoFuente tipo = origenPrincipal.getTipoFuente();
    Long idEnFuente = origenPrincipal.getIdEnFuente();

    Optional<Hecho> optViejo =
        hechoRepository.findByOrigen_TipoFuenteAndOrigen_IdEnFuente(tipo, idEnFuente);

    if (optViejo.isEmpty()) {
      return fusionarPorIdentificadorOCrearNuevoSinPersistir(importado, toSave);
    }

    Hecho viejo = optViejo.get();
    Optional<Hecho> optDestino = hechoRepository.findByIdentificador(identificadorNuevo);

    if (optDestino.isPresent() && optDestino.get().getId().equals(viejo.getId())) {
      actualizarHecho(viejo, importado);
      asegurarOrigen(viejo, origenPrincipal);
      toSave.add(viejo);
      return viejo;
    }

    actualizarHecho(viejo, importado);
    asegurarOrigen(viejo, origenPrincipal);
    toSave.add(viejo);
    return viejo;

  }

  private Hecho fusionarPorIdentificadorOCrearNuevoSinPersistir(Hecho importado, List<Hecho> toSave) {
    Optional<Hecho> porIdentificador = hechoRepository.findByIdentificador(importado.getIdentificador());

    if (porIdentificador.isPresent()) {
      Hecho existente = porIdentificador.get();
      fusionar(existente, importado);
      toSave.add(existente);
      return existente;
    }

    setearProvinciaSiHaceFalta(importado);
    toSave.add(importado);
    return importado;
  }



}
