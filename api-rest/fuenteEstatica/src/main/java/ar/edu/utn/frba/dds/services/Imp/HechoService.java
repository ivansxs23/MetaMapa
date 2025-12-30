package ar.edu.utn.frba.dds.services.Imp;

import ar.edu.utn.frba.dds.entities.Categoria;
import ar.edu.utn.frba.dds.entities.HechoCsvData;
import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.filtro.FiltroDTO;
import ar.edu.utn.frba.dds.entities.filtro.HechoSpecification;
import ar.edu.utn.frba.dds.repositories.ICategoriaRepository;
import ar.edu.utn.frba.dds.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.services.IHechoService;
import ar.edu.utn.frba.dds.services.IMetadataService;
import ar.edu.utn.frba.dds.utils.HechoMapper;
import ar.edu.utn.frba.dds.utils.IReader;

import ar.edu.utn.frba.dds.utils.IWebClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@Service @EnableAsync
public class HechoService implements IHechoService {

  private final IReader csvIReader;
  private final ICategoriaRepository categoriaRepository;
  private final IHechoRepository hechoRepository;
  private final IHechoRepository hechoNewRepository;
  private final IMetadataService metadataService;
  private final IWebClient webClient;
  private final HechoMapper hechoMapper;
  private static final Logger logger = LoggerFactory.getLogger(HechoService.class);
  @Value("${csv.batch-size}")
  private int batchSize;


  public HechoService(IReader csvIReader, ICategoriaRepository categoriaRepository, IHechoRepository hechoRepository, IHechoRepository hechoNewRepository, IMetadataService metadataService, IWebClient webClient, HechoMapper hechoMapper) {
    this.csvIReader = csvIReader;
    this.categoriaRepository = categoriaRepository;
    this.hechoRepository = hechoRepository;
    this.hechoNewRepository = hechoNewRepository;
    this.metadataService = metadataService;
    this.webClient = webClient;
    this.hechoMapper = hechoMapper;
  }

  @Override
  public Page<Hecho> obtenerHechos(FiltroDTO filtro, Pageable pageable) {
    return hechoRepository.findAll(
        HechoSpecification.conFiltros(filtro),
        pageable
    );
  }

  @Async
  public void importarHechosPorLote(MultipartFile archivo) {
    String nombreArchivo = archivo.getOriginalFilename();
    logger.info("Inicio importación CSV: {}", nombreArchivo);

    try {


      validarExtensionCsv(nombreArchivo);

      csvIReader.leerEnLotes(
          archivo.getInputStream(),
          batchSize,
          lote -> guardarLote(lote, nombreArchivo)
      );

      metadataService.actualizarMetadata();
      logger.info("Metadata actualizada correctamente");

    } catch (Exception e) {
      logger.error("Error durante la importación del archivo {}", nombreArchivo, e);
        throw new RuntimeException("Error al leer el archivo", e);
    }
  }

  private void guardarLote(List<HechoCsvData> lote, String nombreArchivo) {
    logger.debug("Procesando lote de {} registros (archivo: {})", lote.size(), nombreArchivo);

    Map<String, Categoria> categorias = obtenerCategoriasDelLote(lote);
    Map<String, Hecho> existentesPorTitulo = obtenerHechosExistentesPorTitulo(lote);
    Map<String, String> provinciaPorCoord = webClient.obtenerProvinciaPorCoordenada(lote);
    List<Hecho> hechos = mapearHechosDelLote(lote, nombreArchivo, categorias, existentesPorTitulo, provinciaPorCoord);

    logger.debug("Lote persistido correctamente ({} registros)", hechos.size());
    hechoNewRepository.saveAll(hechos);
  }

  private List<Hecho> mapearHechosDelLote(
      List<HechoCsvData> lote,
      String nombreArchivo,
      Map<String, Categoria> categorias,
      Map<String, Hecho> existentesPorTitulo,
      Map<String, String> provinciaPorCoord
  ) {

    Map<String, HechoCsvData> ultimoPorTitulo = new LinkedHashMap<>();
    for (HechoCsvData data : lote) {
      ultimoPorTitulo.put(data.getTitulo(), data);
    }

    List<Hecho> hechos = new ArrayList<>(ultimoPorTitulo.size());

    for (HechoCsvData data : ultimoPorTitulo.values()) {

      Hecho hecho = existentesPorTitulo.getOrDefault(
          data.getTitulo(),
          new Hecho()
      );

      Categoria categoria = categorias.get(
          data.getCategoriaString()
      );

      String provincia = provinciaPorCoord.get(
          keyCoord(data.getLatitud(), data.getLongitud())
      );

      hechoMapper.mapearDesdeCsv(
          hecho,
          data,
          categoria,
          nombreArchivo,
          provincia
      );
      hechos.add(hecho);
    }

    return hechos;
  }


  private Map<String, Categoria> obtenerCategoriasDelLote(List<HechoCsvData> lote) {
    Map<String, Categoria> map = new HashMap<>();

    for (HechoCsvData data : lote) {
      String nombre = data.getCategoriaString();

      map.computeIfAbsent(nombre, n ->
          categoriaRepository.getCategoriaByNombre(n)
              .orElseGet(() -> categoriaRepository.save(new Categoria(n)))
      );
    }
    return map;
  }

  private Map<String, Hecho> obtenerHechosExistentesPorTitulo(List<HechoCsvData> lote) {

    List<String> titulos = lote.stream()
        .map(HechoCsvData::getTitulo)
        .distinct()
        .toList();

    return hechoNewRepository.findAllByTituloIn(titulos).stream()
        .collect(Collectors.toMap(
            Hecho::getTitulo,
            h -> h,
            (h1, h2) -> h1.getId() > h2.getId() ? h1 : h2
        ));
  }




  private void validarExtensionCsv(String nombreArchivo) {
    if (nombreArchivo == null || nombreArchivo.isBlank()) {
      throw new IllegalArgumentException("Nombre de archivo inválido");
    }
    String extension = obtenerExtension(nombreArchivo).toLowerCase();
    if (!"csv".equals(extension)) {
      throw new IllegalArgumentException("Extensión no válida: " + extension);
    }
  }
  private String obtenerExtension(String nombre) {
    int idx = nombre.lastIndexOf('.');
    if (idx == -1 || idx == nombre.length() - 1) {
      throw new IllegalArgumentException("El archivo debe tener una extensión válida");
    }
    return nombre.substring(idx + 1);
  }
  private String keyCoord(double lat, double lon) {
    return lat + "|" + lon;
  }
}
