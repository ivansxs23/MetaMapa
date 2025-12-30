package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.entities.HechoCsvData;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

public interface IReader {
  List<HechoCsvData> leer(String rutaArchivo);
  List<HechoCsvData> leer(InputStream inputStream);
  void leerEnLotes(InputStream inputStream, int batchSize, Consumer<List<HechoCsvData>> consumidor);
}