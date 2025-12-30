package ar.edu.utn.frba.dds.entities.dto.geoServiceDTO.output;

public record GeorefUbicacion(
    double lat,
    double lon,
    boolean aplanar,
    String campos
){}
