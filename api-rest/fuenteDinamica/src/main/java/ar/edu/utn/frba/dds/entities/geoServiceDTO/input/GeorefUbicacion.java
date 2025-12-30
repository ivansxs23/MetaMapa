package ar.edu.utn.frba.dds.entities.geoServiceDTO.input;

public record GeorefUbicacion(
    double lat,
    double lon,
    Long provincia_id,
    String provincia_nombre
) {}
