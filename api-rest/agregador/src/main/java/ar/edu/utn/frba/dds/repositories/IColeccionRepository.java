package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.entities.Coleccion;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface IColeccionRepository extends JpaRepository<Coleccion, Long> {
  @Query("select c.id from Coleccion c")
  List<Long> findAllIds();
}
