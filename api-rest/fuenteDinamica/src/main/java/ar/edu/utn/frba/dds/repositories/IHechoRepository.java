package ar.edu.utn.frba.dds.repositories;
import ar.edu.utn.frba.dds.entities.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IHechoRepository extends JpaRepository<Hecho, Long>, JpaSpecificationExecutor<Hecho> {
  List<Hecho> findByVigenteTrue();
}
