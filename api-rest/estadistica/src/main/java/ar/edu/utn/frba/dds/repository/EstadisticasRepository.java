package ar.edu.utn.frba.dds.repository;

import ar.edu.utn.frba.dds.entities.Estadistica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface EstadisticasRepository extends JpaRepository<Estadistica, Long> {
}

