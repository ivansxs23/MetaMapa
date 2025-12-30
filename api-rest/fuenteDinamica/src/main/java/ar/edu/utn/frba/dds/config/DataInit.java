package ar.edu.utn.frba.dds.config;

import ar.edu.utn.frba.dds.entities.ArchivoDTO;
import ar.edu.utn.frba.dds.entities.InputHechoDto;
import ar.edu.utn.frba.dds.entities.TipoArchivo;
import ar.edu.utn.frba.dds.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.services.IHechoService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInit implements CommandLineRunner {
  private final IHechoService hechoService;
  private final IHechoRepository hechoRepository;

  public DataInit(IHechoService hechoService, IHechoRepository hechoRepository) {
    this.hechoService = hechoService;
    this.hechoRepository = hechoRepository;
  }

  @Override
  public void run(String... args) {
    if(hechoRepository.count() > 0){
      System.out.println("Ya hay hechos cargados, no se agrega ninguno nuevo");
      return;
    }
    InputHechoDto nuevoHecho = new InputHechoDto();
    nuevoHecho.setTitulo("Caída de aeronave impacta en Olavarría");
    nuevoHecho.setDescripcion("Grave caída de aeronave ocurrió en las inmediaciones de Olavarría, Buenos Aires. El incidente provocó pánico entre los residentes locales. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.");
    nuevoHecho.setFechaAcontecimiento(LocalDateTime.of(2001, 11, 29, 0, 0));
    nuevoHecho.setIdCategoria(1L);
    nuevoHecho.setLatitud(-36.868375);
    nuevoHecho.setLongitud(-60.343297);
    nuevoHecho.setEsAnonimo(false);
    nuevoHecho.setUsuario("user");
    ArchivoDTO archivo1 = new ArchivoDTO();
    archivo1.setTipo(TipoArchivo.IMAGEN);
    archivo1.setUrl("/uploads/1764170664123_images.png");
    nuevoHecho.setArchivos(List.of(archivo1));
    hechoService.agregarHecho(nuevoHecho);
    hechoService.aprobarHecho(1L);
    System.out.println("Se ha agregado un hecho de prueba a la base de datos.");

  }
}
