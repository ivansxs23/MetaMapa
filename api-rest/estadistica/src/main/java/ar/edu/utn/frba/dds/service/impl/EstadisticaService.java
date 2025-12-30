package ar.edu.utn.frba.dds.service.impl;

import ar.edu.utn.frba.dds.clienteUtils.ClienteEstadistica;
import ar.edu.utn.frba.dds.clienteUtils.EstadisticasMapper;
import ar.edu.utn.frba.dds.entities.Estadistica;
import ar.edu.utn.frba.dds.entities.dto.input.EstadisticasInputDTO;
import ar.edu.utn.frba.dds.repository.EstadisticasRepository;
import ar.edu.utn.frba.dds.service.IEstadisticaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EstadisticaService implements IEstadisticaService {

    private final ClienteEstadistica clienteEstadistica;
    private final EstadisticasRepository estadisticasRepository;
    private final EstadisticasMapper estadisticasMapper;

    public EstadisticaService(ClienteEstadistica clienteEstadistica, EstadisticasRepository estadisticasRepository, EstadisticasMapper estadisticasMapper) {
        this.clienteEstadistica = clienteEstadistica;
        this.estadisticasRepository = estadisticasRepository;
        this.estadisticasMapper = estadisticasMapper;
    }

    @Override
    public void generarEstadisticas() {
        List<EstadisticasInputDTO> estadisticasDto = clienteEstadistica.obtenerEstadisticas().block();
        List<Estadistica> estadisticas = new ArrayList<>();

        assert estadisticasDto != null;
        for (EstadisticasInputDTO dto : estadisticasDto) {
            Estadistica estadistica = estadisticasMapper.aDominio(dto);
            estadisticas.add(estadistica);
        }

        estadisticasRepository.saveAll(estadisticas);
    }
    @Override
    public List<Estadistica> obtenerEstadisticas() {
        return estadisticasRepository.findAll();
    }

}
