package ar.edu.utn.frba.dds.dto.output;

public record GeorefUbicacion(
    double lat,
    double lon,
    boolean aplanar,
    String campos
){}
