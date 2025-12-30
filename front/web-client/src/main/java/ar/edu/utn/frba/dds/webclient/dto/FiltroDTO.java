package ar.edu.utn.frba.dds.webclient.dto;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.web.util.UriUtils;

@Data
public class FiltroDTO {
    private Long idColeccion;
    private Long idCategoria;
    private String provincia;
    private String fuente;
    private String fechaAcontecimientoDesde;
    private String fechaAcontecimientoHasta;
    private Boolean esAnonimo;
    private Boolean curado;

    public Map<String, Object> toQueryParams() {
        Map<String, Object> params = new LinkedHashMap<>();

        if (idColeccion != null) {
            params.put("idColeccion", idColeccion);
        }

        if (idCategoria != null) {
            params.put("idCategoria", idCategoria);
        }

        if (provincia != null && !provincia.isBlank()) {
            params.put("provincia", provincia);
        }

        if (fuente != null && !fuente.isBlank()) {
            params.put("fuente", fuente);
        }

        if (fechaAcontecimientoDesde != null && !fechaAcontecimientoDesde.isBlank()) {
            params.put("fechaAcontecimientoDesde", fechaAcontecimientoDesde);
        }

        if (fechaAcontecimientoHasta != null && !fechaAcontecimientoHasta.isBlank()) {
            params.put("fechaAcontecimientoHasta", fechaAcontecimientoHasta);
        }

        if (esAnonimo != null) {
            params.put("esAnonimo", esAnonimo);
        }

        if (curado != null) {
            params.put("curado", curado);
        }

        return params;
    }
    public String toQueryString() {
        Map<String, Object> params = toQueryParams();

        if (params.isEmpty()) {
            return "";
        }

        return params.entrySet().stream()
            .map(e -> e.getKey() + "=" + UriUtils.encode(
                String.valueOf(e.getValue()),
                StandardCharsets.UTF_8
            ))
            .collect(Collectors.joining("&", "&", ""));
    }

}
