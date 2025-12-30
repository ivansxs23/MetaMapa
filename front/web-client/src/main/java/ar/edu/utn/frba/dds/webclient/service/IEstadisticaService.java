package ar.edu.utn.frba.dds.webclient.service;

import ar.edu.utn.frba.dds.webclient.dto.EstadisticaDTO;

import java.util.List;

public interface IEstadisticaService {
     List<EstadisticaDTO> obtenerEstadisticas();
     EstadisticaDTO obtenerUltimaEstadistica();
     void generar();
}
