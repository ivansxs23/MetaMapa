package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.entities.Hecho;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;

public class HechosUtils {
  public static String generarIdentificador(Hecho hecho) {

    String base = normalizar(hecho.getTitulo()) + "-" +
        hecho.getUbicacion().getLatitud() + "-" +
        hecho.getUbicacion().getLongitud() + "-" +
        hecho.getCategoria().getId() + "-" +
        hecho.getFechaAcontecimiento().toLocalDate();

    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = digest.digest(base.getBytes(StandardCharsets.UTF_8));
      return bytesToHex(hashBytes);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Algoritmo SHA-256 no disponible", e);
    }
  }

  // helper para convertir los bytes del hash a una cadena hexadecimal
  private  static String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));  // 2 dígitos hex por byte
    }
    return sb.toString();
  }

  public static String normalizar(String input) {
    if (input == null) return "";
    return Normalizer.normalize(input, Normalizer.Form.NFD)
        .replaceAll("\\p{M}", "") // quita acentos
        .toLowerCase()
        .trim();
  }

}
