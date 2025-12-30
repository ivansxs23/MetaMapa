package ar.edu.utn.frba.dds.webclient.service;

import org.springframework.web.multipart.MultipartFile;

public interface IMediaStorageService {
  String save(MultipartFile file);
}
