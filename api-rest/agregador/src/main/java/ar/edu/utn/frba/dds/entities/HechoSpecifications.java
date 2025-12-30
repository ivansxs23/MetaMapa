package ar.edu.utn.frba.dds.entities;

import ar.edu.utn.frba.dds.entities.dto.input.FiltroBusquedaHechosDTO;
import java.util.Collection;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.JoinType;
import java.time.LocalDateTime;

public class HechoSpecifications {

  public static Specification<Hecho> activo(Boolean activo) {
    if (activo == null) return Specification.where(null);
    return (root, query, cb) -> cb.equal(root.get("activo"), activo);
  }

  public static Specification<Hecho> ultimaFechaModificacionMayorA(LocalDateTime desde) {
    if (desde == null) return Specification.where(null);
    return (root, query, cb) -> cb.greaterThan(root.get("ultimaFechaModificacion"), desde);
  }

  public static Specification<Hecho> idCategoria(Long idCategoria) {
    if (idCategoria == null) return Specification.where(null);
    return (root, query, cb) -> cb.equal(root.get("categoria").get("id"), idCategoria);
  }

  public static Specification<Hecho> provincia(String provincia) {
    if (provincia == null || provincia.isBlank()) return Specification.where(null);
    return (root, query, cb) ->
        cb.equal(cb.lower(root.get("ubicacion").get("provincia")), provincia.toLowerCase());
  }

  public static Specification<Hecho> fuenteTipo(TipoFuente tipoFuente) {
    if (tipoFuente == null) return Specification.where(null);
    return (root, query, cb) -> {
      query.distinct(true);
      var o = root.join("origen", JoinType.INNER);
      return cb.equal(o.get("tipoFuente"), tipoFuente);
    };
  }

  public static Specification<Hecho> origenTipoIn(Collection<TipoFuente> tipos) {
    if (tipos == null || tipos.isEmpty()) return Specification.where(null);

    return (root, query, cb) -> {
      query.distinct(true);
      var o = root.join("origen", JoinType.INNER);
      return o.get("tipoFuente").in(tipos);
    };
  }

  /**
   * SPEC CORRECTO: usa UN solo join a coleccionHechos
   */
  public static Specification<Hecho> enColeccionYCurado(Long idColeccion, Boolean curado) {
    if (idColeccion == null && !Boolean.TRUE.equals(curado)) {
      return Specification.where(null);
    }

    return (root, query, cb) -> {
      query.distinct(true);
      var ch = root.join("coleccionHechos", JoinType.INNER);

      var predicates = cb.conjunction();

      if (idColeccion != null) {
        predicates = cb.and(
            predicates,
            cb.equal(ch.get("coleccion").get("id"), idColeccion)
        );
      }

      if (Boolean.TRUE.equals(curado)) {
        predicates = cb.and(
            predicates,
            cb.isTrue(ch.get("consensuado"))
        );
      }

      return predicates;
    };
  }

  public static Specification<Hecho> fechaAcontecimientoDesde(LocalDateTime desde) {
    if (desde == null) return Specification.where(null);
    return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("fechaAcontecimiento"), desde);
  }

  public static Specification<Hecho> fechaAcontecimientoHasta(LocalDateTime hasta) {
    if (hasta == null) return Specification.where(null);
    return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("fechaAcontecimiento"), hasta);
  }

  public static Specification<Hecho> esAnonimo(Boolean esAnonimo) {
    if (esAnonimo == null) return Specification.where(null);
    return (root, query, cb) -> cb.equal(root.get("esAnonimo"), esAnonimo);
  }

  public static Specification<Hecho> fromFiltro(
      FiltroBusquedaHechosDTO f, Long idColeccion) {

    TipoFuente tipoFuente = null;
    if (f != null && f.getFuente() != null && !f.getFuente().isBlank()) {
      tipoFuente = TipoFuente.valueOf(f.getFuente().trim().toUpperCase());
    }

    return Specification
        .where(activo(f != null ? f.getActivo() : null))
        .and(enColeccionYCurado(idColeccion, f.getCurado()))
        .and(ultimaFechaModificacionMayorA(f.getUltimaFechaModificacionDesde()))
        .and(idCategoria(f != null ? f.getIdCategoria() : null))
        .and(provincia(f != null ? f.getProvincia() : null))
        .and(fuenteTipo(tipoFuente))
        .and(fechaAcontecimientoDesde(f != null ? f.getFechaAcontecimientoDesde() : null))
        .and(fechaAcontecimientoHasta(f != null ? f.getFechaAcontecimientoHasta() : null))
        .and(esAnonimo(f != null ? f.getEsAnonimo() : null));
  }
}
