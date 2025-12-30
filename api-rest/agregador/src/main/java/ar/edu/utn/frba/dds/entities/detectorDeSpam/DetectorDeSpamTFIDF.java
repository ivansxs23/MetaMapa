package ar.edu.utn.frba.dds.entities.detectorDeSpam;

import ar.edu.utn.frba.dds.repositories.CorpusRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DetectorDeSpamTFIDF implements DetectorDeSpam {

  private final CorpusRepository corpusRepository;
  private List<List<String>> corpusPreprocesado; // ya no final ni cargado en constructor
  private final Set<String> stopWords;
  private final Set<String> palabrasProhibidas;
  private final double umbralDeSpam;

  public DetectorDeSpamTFIDF(
          @Value("${detector.umbral}") double umbral,
          @Value("${detector.palabrasProhibidasPath}") String prohibidasPath,
          CorpusRepository corpusRepository
  ) {
    this.umbralDeSpam = umbral;
    this.corpusRepository = corpusRepository;
    this.stopWords = new HashSet<>(Arrays.asList(
            "el", "la", "los", "las", "de", "que", "en", "y", "a", "un", "una"
    ));
    this.corpusPreprocesado = new ArrayList<>();

    try (InputStream is = getClass().getClassLoader().getResourceAsStream(prohibidasPath)) {
      if (is == null) {
        throw new RuntimeException("No se encontró el archivo en resources: " + prohibidasPath);
      }
      this.palabrasProhibidas = new BufferedReader(new InputStreamReader(is))
          .lines()
          .map(String::toLowerCase)
          .map(String::trim)
          .filter(p -> !p.isBlank())
          .collect(Collectors.toSet());
    } catch (IOException e) {
      throw new RuntimeException("Error al leer palabras prohibidas", e);
    }
  }

  @Override
  public boolean esSpam(String texto) {
    if (contieneSpamObvio(texto)) return true;

    Map<String, Double> tfidfs = calcularTFIDF(texto);
    double promedio = tfidfs.values().stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);

    return promedio > umbralDeSpam;
  }

  private boolean contieneSpamObvio(String texto) {
    List<String> palabras = limpiarTexto(texto);
    return palabras.stream().anyMatch(p -> palabrasProhibidas.contains(p) || p.matches("([a-zA-Z])\\1{2,}"));
  }

  private Map<String, Double> calcularTFIDF(String texto) {
    Map<String, Double> tf = calcularTF(texto);
    Map<String, Double> tfidf = new HashMap<>();
    for (String palabra : tf.keySet()) {
      double idf = calcularIDF(palabra); // ensureCorpusLoaded() se invoca dentro
      tfidf.put(palabra, tf.get(palabra) * idf);
    }
    return tfidf;
  }

  private Map<String, Double> calcularTF(String texto) {
    Map<String, Double> tf = new HashMap<>();
    List<String> palabras = limpiarTexto(texto);
    for (String palabra : palabras) {
      tf.put(palabra, tf.getOrDefault(palabra, 0.0) + 1);
    }
    int total = palabras.size();
    if (total > 0) {
      tf.replaceAll((p, v) -> v / total);
    }
    return tf;
  }

  // Recarga el corpus si aún no está cargado (lazy load).
  private void ensureCorpusLoaded() {
    // Si está vacío, recargo desde la DB (esto permite que los tests que llenan la DB en @BeforeEach funcionen).
    if (this.corpusPreprocesado == null || this.corpusPreprocesado.isEmpty()) {
      synchronized (this) {
        if (this.corpusPreprocesado == null || this.corpusPreprocesado.isEmpty()) {
          this.corpusPreprocesado = corpusRepository.findAll().stream()
                  .map(Documento::getTexto)
                  .map(this::limpiarTexto)
                  .collect(Collectors.toList());
        }
      }
    }
  }

  private double calcularIDF(String palabra) {
    ensureCorpusLoaded();
    long documentosConPalabra = corpusPreprocesado.stream()
            .filter(lista -> lista.contains(palabra))
            .count();
    return Math.log((1.0 + corpusPreprocesado.size()) / (1.0 + documentosConPalabra));
  }

  private List<String> limpiarTexto(String texto) {
    String sinAcentos = Normalizer.normalize(texto.toLowerCase(), Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    return Arrays.stream(sinAcentos.split("\\W+"))
            .filter(p -> !stopWords.contains(p) && p.length() > 2)
            .collect(Collectors.toList());
  }
}
