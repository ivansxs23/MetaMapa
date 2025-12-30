package ar.edu.utn.frba.dds.service;

import ar.edu.utn.frba.dds.entities.Estadistica;

import java.util.List;

public interface IEstadisticaService {
    List<Estadistica> obtenerEstadisticas();
    void generarEstadisticas();
}
