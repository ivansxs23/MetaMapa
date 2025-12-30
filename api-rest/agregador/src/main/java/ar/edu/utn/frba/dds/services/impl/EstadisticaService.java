package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.entities.Categoria;
import ar.edu.utn.frba.dds.entities.Coleccion;
import ar.edu.utn.frba.dds.entities.dto.input.estadistica.CategoriaCantidadDTO;
import ar.edu.utn.frba.dds.entities.dto.input.estadistica.HoraCantidadCategoriaDTO;
import ar.edu.utn.frba.dds.entities.dto.input.estadistica.ProvinciaCantidadCategoriaDTO;
import ar.edu.utn.frba.dds.entities.dto.input.estadistica.ProvinciaCantidadDTO;
import ar.edu.utn.frba.dds.entities.dto.output.EstadisticasOutputDTO;
import ar.edu.utn.frba.dds.entities.solicitud.EstadoSolicitud;
import ar.edu.utn.frba.dds.repositories.ICategoriaRepository;
import ar.edu.utn.frba.dds.repositories.IColeccionRepository;
import ar.edu.utn.frba.dds.repositories.IEstadisticaRepository;
import ar.edu.utn.frba.dds.services.IEstadisticaService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class EstadisticaService implements IEstadisticaService {
  private final IColeccionRepository coleccionRepository;
  private final IEstadisticaRepository estadisticaRepository;
  private final ICategoriaRepository categoriaRepository;



  public EstadisticaService(IColeccionRepository coleccionRepository, IEstadisticaRepository estadisticaRepository, ICategoriaRepository categoriaRepository) {
    this.coleccionRepository = coleccionRepository;
    this.estadisticaRepository = estadisticaRepository;
    this.categoriaRepository = categoriaRepository;
  }

  @Override
  public EstadisticasOutputDTO obtenerEstadisticas() {
    EstadisticasOutputDTO estadisticas = new EstadisticasOutputDTO();
    estadisticas.getTopProvinciaPorColecciones().addAll(obtenerTopProvinciasPorColecciones());
    estadisticas.setTopCategoria(obtenerTopCategoria());
    estadisticas.getTopProvinciaPorCategoria().addAll(obtenerTopProvinciaPorCategoria());
    estadisticas.setHoraPicoPorCategoria(obtenerHoraPicoPorCategoria());
    estadisticas.setCantidadSpam(obtenerCantidadSpam());
    return estadisticas;
  }
  public List<ProvinciaCantidadDTO> obtenerTopProvinciasPorColecciones() {
    List<Coleccion> colecciones = coleccionRepository.findAll();
    List<ProvinciaCantidadDTO> topProvincias = new ArrayList<>();
    for (Coleccion coleccion : colecciones) {

       estadisticaRepository.contarHechosPorProvinciaDesc(coleccion.getId(), PageRequest.of(0,1)).
           getContent().stream().findFirst().ifPresent(topProvincias::add);
    }
    return topProvincias;
  }

  public CategoriaCantidadDTO obtenerTopCategoria() {
    return estadisticaRepository.contarHechosPorCategoriaDesc(PageRequest.of(0,1)).getContent().stream().findFirst().orElse(null);
  }

  public List<ProvinciaCantidadCategoriaDTO> obtenerTopProvinciaPorCategoria() {
    List<Categoria> categorias = categoriaRepository.findAll();
    List <ProvinciaCantidadCategoriaDTO> topProvincias = new ArrayList<>();

    for (Categoria categoria : categorias) {
      estadisticaRepository.contarCategoriasPorProvinciaDesc(categoria.getId(), PageRequest.of(0, 1)).getContent().stream().findFirst().ifPresent(topProvincias::add);
    }

    return topProvincias;
  }

  public List<HoraCantidadCategoriaDTO> obtenerHoraPicoPorCategoria() {
    List<Categoria> categorias = categoriaRepository.findAll();
    List<HoraCantidadCategoriaDTO> horaPico = new ArrayList<>();

    for (Categoria categoria : categorias) {
      estadisticaRepository.contarHorasDeAcontecimientoPorCategoria(categoria.getId(), PageRequest.of(0,1)).getContent().stream().findFirst().ifPresent(horaPico::add);
    }
    return horaPico;
  }

  public Long obtenerCantidadSpam() {
    return estadisticaRepository.contarPorEstado(EstadoSolicitud.DENEGADA);
  }

}
