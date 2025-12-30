package ar.edu.utn.frba.dds.controllers;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Component;

@RestController
@RequestMapping("/api/test")
public class HealthTestController {

    // Inyectamos el indicador de salud personalizado
    private final ManualHealthIndicator healthIndicator;

    public HealthTestController(ManualHealthIndicator healthIndicator) {
        this.healthIndicator = healthIndicator;
    }

    @PostMapping("/romper")
    public String romper() {
        healthIndicator.setWorking(false);
        return "¡El sistema ha sido saboteado! Ahora health estará DOWN.";
    }

    @PostMapping("/arreglar")
    public String arreglar() {
        healthIndicator.setWorking(true);
        return "Sistema reparado.";
    }
}

// Este componente le dice a Actuator cómo se siente la app
@Component
class ManualHealthIndicator implements HealthIndicator {
    private boolean isWorking = true;

    public void setWorking(boolean working) {
        isWorking = working;
    }

    @Override
    public Health health() {
        if (isWorking) {
            return Health.up().build();
        } else {
            return Health.down().withDetail("Error", "Simulacion de fallo critico").build();
        }
    }
}