package ar.edu.utn.frba.dds.schedulers;

import ar.edu.utn.frba.dds.services.IAlgoritmoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ConsensoScheduler {
  private final IAlgoritmoService algoritmoService;

  public ConsensoScheduler(IAlgoritmoService algoritmoService) {
    this.algoritmoService = algoritmoService;
  }


  @Scheduled(cron = "${cron.consenso}")
  public void ConsensuarColecciones(){
    System.out.println("Iniciando consenso de colecciones...");
    algoritmoService.consensuarHechos();


  }
}
