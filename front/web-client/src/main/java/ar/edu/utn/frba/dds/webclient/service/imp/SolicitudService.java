package ar.edu.utn.frba.dds.webclient.service.imp;

import ar.edu.utn.frba.dds.webclient.dto.FiltroSolicitudDTO;
import ar.edu.utn.frba.dds.webclient.dto.HechoDTO;
import ar.edu.utn.frba.dds.webclient.dto.SolicitudDTO;
import ar.edu.utn.frba.dds.webclient.dto.input.SolicitudInputDTO;
import ar.edu.utn.frba.dds.webclient.dto.input.TipoSolicitud;
import ar.edu.utn.frba.dds.webclient.service.ISolicitudService;
import ar.edu.utn.frba.dds.webclient.service.internal.WebApiCallerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.List;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class SolicitudService implements ISolicitudService {

    private final WebApiCallerService webApiCallerService;
    private final String agregadorUrl; // API de usuarios

    public SolicitudService(@Value("${api.backend.url}") String agregadorUrl,
                            WebApiCallerService webApiCallerService) {
        this.webApiCallerService = webApiCallerService;
        this.agregadorUrl = agregadorUrl;
    }

    @Override
    public List<SolicitudInputDTO> obtenerTodas(FiltroSolicitudDTO f) {
        if (f == null) {
            f = new FiltroSolicitudDTO();
        }
        UriComponentsBuilder uri = UriComponentsBuilder
            .fromHttpUrl(agregadorUrl + "/api/solicitudes");

        if (f.getTipo() != null) uri.queryParam("tipo", f.getTipo());
        if (f.getEstado() != null) uri.queryParam("estado", f.getEstado());
        if (f.getHechoId() != null) uri.queryParam("hechoId", f.getHechoId());
        if (f.getEsSpam() != null) uri.queryParam("esSpam", f.getEsSpam());
        if (f.getSolicitante() != null && !f.getSolicitante().isBlank())
            uri.queryParam("solicitante", f.getSolicitante().trim());
        if (f.getAdministrador() != null && !f.getAdministrador().isBlank())
            uri.queryParam("administrador", f.getAdministrador().trim());

        return webApiCallerService.getList(
            uri.toUriString(),
                SolicitudInputDTO.class
        );
    }


    @Override
    public void aprobarSolicitud(Long id) {
        try {
            webApiCallerService.get(
                agregadorUrl + "/api/solicitudes/" + id + "/aprobar",
                Void.class
            );
        }catch (Exception e) {
            throw new RuntimeException("No se pudo aprobar la solicitud", e);
        }
    }

    @Override
    public void denegarSolicitud(Long id) {
        try {
            webApiCallerService.get(
                agregadorUrl + "/api/solicitudes/" + id + "/denegar",
                Void.class
            );
        }catch (Exception e){
            throw new RuntimeException("No se pudo denegar la solicitud", e);
        }
    }

    public void crearSolicitud(SolicitudDTO solicitud) {
        System.out.println("Creando Solicitud de eliminacion");
        try {
            webApiCallerService.post(agregadorUrl + "/api/solicitudes", solicitud, String.class);

        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear la solicitud", e);
        }
    }
    public void crearSolicitudDeEliminacion(SolicitudDTO solicitud){
        solicitud.setTipo(TipoSolicitud.ELIMINACION);
        crearSolicitud(solicitud);
    }

    @Override
    public void crearSolicitudDeCreacion(HechoDTO hechoDTO) {
        SolicitudDTO solicitud = new SolicitudDTO();
        solicitud.setTipo(TipoSolicitud.CREACION);
        solicitud.setDatosNuevos(hechoDTO);
        crearSolicitud(solicitud);
    }

    @Override
    public void crearSolicitudDeEdicion(Long id, HechoDTO hechoDTO) {
        SolicitudDTO solicitudDTO = new SolicitudDTO();
        solicitudDTO.setTipo(TipoSolicitud.EDICION);
        solicitudDTO.setIdHecho(id);
        solicitudDTO.setDatosNuevos(hechoDTO);
        crearSolicitud(solicitudDTO);
    }
}
