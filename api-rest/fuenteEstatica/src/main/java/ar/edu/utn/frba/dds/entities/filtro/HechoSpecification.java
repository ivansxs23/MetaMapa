package ar.edu.utn.frba.dds.entities.filtro;

import ar.edu.utn.frba.dds.entities.Categoria;
import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.Ubicacion;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class HechoSpecification {

  public static Specification<Hecho> conFiltros(FiltroDTO f) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (f.getId() != null) {
        predicates.add(cb.equal(root.get("id"), f.getId()));
      }

      if (f.getTitulo() != null) {
        predicates.add(
            cb.like(
                cb.lower(root.get("titulo")),
                "%" + f.getTitulo().toLowerCase() + "%"
            )
        );
      }

      if (f.getCategoriaId() != null) {
        Join<Hecho, Categoria> categoriaJoin = root.join("categoria");
        predicates.add(
            cb.equal(categoriaJoin.get("id"), f.getCategoriaId())
        );
      }

      if (f.getUbicacionId() != null) {
        predicates.add(
            cb.equal(root.get("ubicacion").get("id"), f.getUbicacionId())
        );
      }
      if (f.getProvincia() != null) {
        Join<Hecho, Ubicacion> ubicacionJoin =
            root.join("ubicacion", JoinType.LEFT);

        predicates.add(
            cb.equal(
                cb.lower(ubicacionJoin.get("provincia")),
                f.getProvincia().toLowerCase()
            )
        );
      }


      if (f.getFechaHechoDesde() != null) {
        predicates.add(
            cb.greaterThanOrEqualTo(
                root.get("fechaHecho"),
                f.getFechaHechoDesde()
            )
        );
      }
      if (f.getFechaHechoHasta() != null) {
        predicates.add(
            cb.lessThanOrEqualTo(
                root.get("fechaHecho"),
                f.getFechaHechoHasta()
            )
        );
      }
      if (f.getFechaModificacionDesde() != null) {
        predicates.add(
            cb.greaterThanOrEqualTo(
                root.get("fechaModificacion"),
                f.getFechaModificacionDesde()
            )
        );
      }
      if (f.getOrigen() != null) {
        predicates.add(cb.equal(root.get("origen"), f.getOrigen()));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}

