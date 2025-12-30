package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.entities.Metadata;
import ar.edu.utn.frba.dds.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.repositories.IMetadataRepository;
import ar.edu.utn.frba.dds.services.IMetadataService;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class MetadataService implements IMetadataService {
  private final IMetadataRepository metadataRepository;
  private final IHechoRepository hechoRepository;

  public MetadataService(IMetadataRepository metadataRepository, IHechoRepository hechoRepository) {
    this.metadataRepository = metadataRepository;
    this.hechoRepository = hechoRepository;
  }
  @Override
  public void actualizarMetadata() {
    Metadata metadata = new Metadata("dinamica",
        hechoRepository.count(), LocalDateTime.now()
    );
    metadataRepository.save(metadata);
  }

  @Override
  public Metadata obtenerMetadata() {
    return metadataRepository.findById("dinamica")
        .orElseThrow(() -> new RuntimeException("Metadata no encontrada"));
  }
}
