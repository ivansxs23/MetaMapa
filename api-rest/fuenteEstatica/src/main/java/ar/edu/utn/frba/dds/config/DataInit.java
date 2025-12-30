package ar.edu.utn.frba.dds.config;

import ar.edu.utn.frba.dds.services.IHechoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInit implements CommandLineRunner {
  private final IHechoService hechoService;


  public DataInit(IHechoService hechoService) {
    this.hechoService = hechoService;
  }

  @Override
  public void run(String... args) {
    /*
    hechoService.importarHechos("cinco_hechos.csv");
    System.out.println("Importación de hechos completada.");
     */
  }
}