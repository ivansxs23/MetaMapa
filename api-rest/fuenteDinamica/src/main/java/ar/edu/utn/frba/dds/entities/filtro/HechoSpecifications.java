package ar.edu.utn.frba.dds.entities.filtro;

import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.Ubicacion;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class HechoSpecifications {

  public static Specification<Hecho> conFiltros(FiltroHechoDTO f) {
    return (root, query, cb) -> {
      if (f == null) return cb.conjunction();

      List<Predicate> p = new ArrayList<>();

      if (f.getVigente() != null) {
        p.add(cb.equal(root.get("vigente"), f.getVigente()));
      }

      if (f.getIdCategoria() != null) {
        p.add(cb.equal(root.get("idCategoria"), f.getIdCategoria()));
      }

      if (f.getUsername() != null && !f.getUsername().isBlank()) {
        p.add(cb.equal(root.get("username"), f.getUsername().trim()));
      }

      if (f.getEsAnonimo() != null) {
        p.add(cb.equal(root.get("esAnonimo"), f.getEsAnonimo()));
      }

      if (f.getEsEditable() != null) {
        p.add(cb.equal(root.get("esEditable"), f.getEsEditable()));
      }

      if (f.getEstado() != null) {
        p.add(cb.equal(root.get("estado"), f.getEstado()));
      }

      if (f.getTitulo() != null && !f.getTitulo().isBlank()) {
        String like = "%" + f.getTitulo().trim().toLowerCase() + "%";
        p.add(cb.or(
            cb.like(cb.lower(root.get("titulo")), like)
        ));
      }

      // RANGOS de fechas
      rangoFecha(p, cb, root.get("fechaAcontecimiento"),
          f.getFechaAcontecimientoDesde(), f.getFechaAcontecimientoHasta());

      rangoFecha(p, cb, root.get("fechaCarga"),
          f.getFechaCargaDesde(), f.getFechaCargaHasta());

      rangoFecha(p, cb, root.get("ultimaEdicion"),
          f.getUltimaEdicionDesde(), f.getUltimaEdicionHasta());

      // Ubicación (join)
      if (necesitaJoinUbicacion(f)) {
        Join<Hecho, Ubicacion> u = root.join("ubicacion", JoinType.LEFT);

        if (f.getProvincia() != null && !f.getProvincia().isBlank()) {
          p.add(cb.equal(cb.lower(u.get("provincia")), f.getProvincia().trim().toLowerCase()));
        }
        if (f.getLat() != null) {
          p.add(cb.equal(u.get("lat"), f.getLat()));
        }
        if (f.getLon() != null) {
          p.add(cb.equal(u.get("lon"), f.getLon()));
        }
      }

      return cb.and(p.toArray(new jakarta.persistence.criteria.Predicate[0]));
    };
  }

  private static boolean necesitaJoinUbicacion(FiltroHechoDTO f) {
    return (f.getProvincia() != null && !f.getProvincia().isBlank())
        || f.getLat() != null
        || f.getLon() != null;
  }

  private static <T extends Comparable<? super T>> void rangoFecha(
      List<Predicate> p,
      CriteriaBuilder cb,
      Path<T> path,
      T desde,
      T hasta
  ) {
    if (desde != null) p.add(cb.greaterThanOrEqualTo(path, desde));
    if (hasta != null) p.add(cb.lessThanOrEqualTo(path, hasta));
  }
}

