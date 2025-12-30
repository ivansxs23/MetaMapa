package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.entities.HechoCsvData;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader; // Ojo: java.io.Reader, no tu interfaz
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections; // Para retornar lista vacía en vez de null
import java.util.List;

@Component("csv")
public class CSVReader implements IReader {
  private static final Logger logger = LoggerFactory.getLogger(CSVReader.class);


  @Override
  public List<HechoCsvData> leer(String rutaArchivo) {
    try (FileReader reader = new FileReader(rutaArchivo)) {
      return parsearDatos(reader);
    } catch (IOException e) {
      System.out.println("Error al leer archivo físico: " + e.getMessage());
      return Collections.emptyList();
    }
  }

  @Override
  public List<HechoCsvData> leer(InputStream inputStream) {
    try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
      return parsearDatos(reader);
    } catch (IOException e) {
      System.out.println("Error al leer InputStream: " + e.getMessage());
      return Collections.emptyList();
    }
  }
  private CsvToBean<HechoCsvData> crearCsvToBean(Reader reader) {
    return new CsvToBeanBuilder<HechoCsvData>(reader)
        .withType(HechoCsvData.class)
        .withSeparator(',')
        .withIgnoreQuotations(false)
        .withSkipLines(1)
        .build();
  }

  private List<HechoCsvData> parsearDatos(Reader reader) {
    CsvToBean<HechoCsvData> csvToBean = crearCsvToBean(reader);
    return csvToBean.parse();
  }

  public void leerEnLotes(
      InputStream inputStream,
      int batchSize,
      Consumer<List<HechoCsvData>> consumidor
  ) {

    logger.info("Inicio lectura CSV en lotes de {}", batchSize);

    int lineasLeidas = 0;
    int lineasInvalidas = 0;

    try (InputStreamReader reader =
             new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

      Iterator<HechoCsvData> iterator = crearCsvToBean(reader).iterator();
      List<HechoCsvData> buffer = new ArrayList<>(batchSize);

      while (iterator.hasNext()) {
        try {
          HechoCsvData data = iterator.next();
          buffer.add(data);
          lineasLeidas++;

          if (buffer.size() == batchSize) {
            logger.debug("Lote completo leído ({} registros)", buffer.size());
            consumidor.accept(buffer);
            buffer.clear();
          }

        } catch (Exception filaInvalida) {
          lineasInvalidas++;
          logger.warn("Línea inválida ignorada (total inválidas: {})", lineasInvalidas);
        }
      }

      if (!buffer.isEmpty()) {
        logger.debug("Último lote leído ({} registros)", buffer.size());
        consumidor.accept(buffer);
      }

      logger.info(
          "Fin lectura CSV. Líneas válidas: {}, inválidas: {}",
          lineasLeidas,
          lineasInvalidas
      );

    } catch (Exception e) {
      logger.error("Error crítico procesando CSV", e);
      throw new RuntimeException("Error procesando CSV por lotes", e);
    }
  }

}