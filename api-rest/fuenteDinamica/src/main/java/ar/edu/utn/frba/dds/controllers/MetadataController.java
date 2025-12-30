package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.entities.Metadata;
import ar.edu.utn.frba.dds.services.IMetadataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/metadata")
public class MetadataController {
  private final IMetadataService metadataService;

  public MetadataController(IMetadataService metadataService) {
    this.metadataService = metadataService;
  }
@GetMapping
  public Metadata obtenerMetadata() {
    return metadataService.obtenerMetadata();
  }
}
