package ar.edu.utn.frba.dds.webclient.service.imp;

import ar.edu.utn.frba.dds.webclient.service.IMediaStorageService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalMediaStorageService implements IMediaStorageService {
  private final Path uploadDir = Paths.get("uploads");

  public LocalMediaStorageService() throws IOException {
    if (!Files.exists(uploadDir)) {
      Files.createDirectories(uploadDir);
    }
  }

  @Override
  public String save(MultipartFile file) {
    try {
      String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
      Path filePath = uploadDir.resolve(fileName);

      Files.copy(file.getInputStream(), filePath);

      return "/uploads/" + fileName;

    } catch (IOException e) {
      throw new RuntimeException("Error al guardar archivo", e);
    }
  }
}
