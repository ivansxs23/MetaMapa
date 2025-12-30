package ar.edu.utn.frba.dds.dto.input;

public record GeorefUbicacion(
    double lat,
    double lon,
    Long provincia_id,
    String provincia_nombre
) {}
