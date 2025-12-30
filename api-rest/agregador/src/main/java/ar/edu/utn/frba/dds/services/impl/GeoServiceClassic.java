package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.services.IGeoService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class GeoServiceClassic implements IGeoService {

    private List<ProvinciaGeometria> provinciasCache = new ArrayList<>();

    // Clase interna para guardar la geometría
    private static class ProvinciaGeometria {
        String id;
        String nombre;
        List<List<double[]>> poligonos;

        public ProvinciaGeometria(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
            this.poligonos = new ArrayList<>();
        }
    }

    @PostConstruct
    public void iniciar() {
        try {
            System.out.println("--- INICIANDO CARGA DE PROVINCIAS ---");
            // CORRECCIÓN: Solo el nombre del archivo. Spring busca en src/main/resources automáticamente.
            cargarDatosDesdeJson("provincias.json");
            System.out.println("--- FIN CARGA: " + provinciasCache.size() + " provincias listas en memoria ---");
        } catch (IOException e) {
            System.err.println("ERROR CRÍTICO: No se pudo cargar el archivo de provincias.");
            e.printStackTrace();
        }
    }

    // 1. CARGAR DATOS
    public void cargarDatosDesdeJson(String nombreArchivo) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // CORRECCIÓN: Usar ClassPathResource para leer dentro del JAR/Classpath
        ClassPathResource resource = new ClassPathResource(nombreArchivo);

        // Usamos try-with-resources para asegurar que se cierre el stream
        try (InputStream inputStream = resource.getInputStream()) {
            JsonNode root = mapper.readTree(inputStream);
            JsonNode features = root.get("features");

            if (features.isArray()) {
                for (JsonNode feature : features) {
                    String nombre = feature.get("properties").get("nombre").asText();
                    String id = feature.get("properties").get("id").asText();
                    ProvinciaGeometria provincia = new ProvinciaGeometria(id, nombre);

                    JsonNode geometry = feature.get("geometry");
                    String type = geometry.get("type").asText();
                    JsonNode coordinates = geometry.get("coordinates");

                    if ("Polygon".equals(type)) {
                        agregarPoligono(provincia, coordinates.get(0));
                    } else if ("MultiPolygon".equals(type)) {
                        for (JsonNode poligonoNode : coordinates) {
                            agregarPoligono(provincia, poligonoNode.get(0));
                        }
                    }
                    provinciasCache.add(provincia);
                }
            }
        }
    }

    private void agregarPoligono(ProvinciaGeometria prov, JsonNode coordenadasNode) {
        List<double[]> puntos = new ArrayList<>();
        for (JsonNode coord : coordenadasNode) {
            // GeoJSON es [Longitud (X), Latitud (Y)]
            double lon = coord.get(0).asDouble();
            double lat = coord.get(1).asDouble();
            puntos.add(new double[]{lon, lat});
        }
        prov.poligonos.add(puntos);
    }

    // 2. BUSCAR PROVINCIA
    @Override
    public String obtenerProvincia(Double latitud, Double longitud) {
        if (latitud == null || longitud == null) return "Coordenadas Nulas";

        for (ProvinciaGeometria provincia : provinciasCache) {
            for (List<double[]> poligono : provincia.poligonos) {
                // NOTA: Pasamos (longitud, latitud) porque el algoritmo usa X, Y
                if (esPuntoEnPoligono(longitud, latitud, poligono)) {
                    return provincia.nombre;
                }
            }
        }
        return "Desconocida"; // Valor por defecto para que no rompa la BD
    }

    // 3. ALGORITMO RAY CASTING
    private boolean esPuntoEnPoligono(double x, double y, List<double[]> poligono) {
        boolean inside = false;
        int n = poligono.size();

        for (int i = 0, j = n - 1; i < n; j = i++) {
            double xi = poligono.get(i)[0], yi = poligono.get(i)[1];
            double xj = poligono.get(j)[0], yj = poligono.get(j)[1];

            boolean intersect = ((yi > y) != (yj > y)) &&
                    (x < (xj - xi) * (y - yi) / (yj - yi) + xi);

            if (intersect) {
                inside = !inside;
            }
        }
        return inside;
    }
}