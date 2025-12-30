package ar.edu.utn.frba.dds.async;

import ar.edu.utn.frba.dds.services.impl.ColeccionService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ColeccionActualizadaListener {
  private final ColeccionService coleccionService;

  public ColeccionActualizadaListener(ColeccionService coleccionService) {
    this.coleccionService = coleccionService;
  }

  @Async("coleccionExecutor")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void on(ColeccionActualizadaEvent event) {
    coleccionService.actualizarColeccion(event.coleccionId());
  }
}
