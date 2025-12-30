package ar.edu.utn.frba.dds.schedulers;

import ar.edu.utn.frba.dds.services.impl.AgregadorService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AgregadorScheduler {
  private final AgregadorService agregadorService;

  public AgregadorScheduler(AgregadorService agregadorService) {
    this.agregadorService = agregadorService;
  }
  @Scheduled(cron = "${cron.agregador}")
  public void ActualizarColeccionesCadaHora(){
    agregadorService.importarHechosYActualizarColecciones();
  }
}
