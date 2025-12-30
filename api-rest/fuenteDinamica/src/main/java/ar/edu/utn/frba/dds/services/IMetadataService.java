package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.entities.Metadata;

public interface IMetadataService {
  void actualizarMetadata();
  Metadata obtenerMetadata();
}
