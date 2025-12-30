package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.entities.detectorDeSpam.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorpusRepository extends JpaRepository<Documento, Long> {
}
