package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.entities.AgregadorData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAgregadorDataRepository extends JpaRepository<AgregadorData, Long> {

  default AgregadorData getOrCreate(){
    return findById(1L).orElse(new AgregadorData());
  }
}
