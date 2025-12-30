package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.entities.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMetadataRepository extends JpaRepository<Metadata, String> {
}
