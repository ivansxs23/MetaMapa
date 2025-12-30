package ar.edu.utn.frba.dds.webclient.service.imp;

import ar.edu.utn.frba.dds.webclient.dto.EstadisticaDTO;
import ar.edu.utn.frba.dds.webclient.service.IEstadisticaService;
import ar.edu.utn.frba.dds.webclient.service.internal.WebApiCallerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadisticaService implements IEstadisticaService {

    private final WebApiCallerService webApiCallerService;
    private final String baseUrl;

    public EstadisticaService(WebApiCallerService webApiCallerService,
                              @Value("${api.estadistica.url}") String url) {
        this.webApiCallerService = webApiCallerService;
        this.baseUrl = url;
    }

    @Override
    public List<EstadisticaDTO> obtenerEstadisticas() {
        return webApiCallerService.getList(this.baseUrl + "/estadisticas", EstadisticaDTO.class);
    }

    @Override
    public EstadisticaDTO obtenerUltimaEstadistica() {
        return webApiCallerService.get(this.baseUrl + "/estadisticas/ultima", EstadisticaDTO.class);
    }

    @Override
    public void generar() {
        webApiCallerService.get(this.baseUrl + "/estadisticas/generar", Void.class);
    }

}