package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.entities.TipoFuente;
import ar.edu.utn.frba.dds.entities.dto.input.FiltroBusquedaHechosDTO;
import ar.edu.utn.frba.dds.entities.filtros.FiltroHecho;
import ar.edu.utn.frba.dds.entities.filtros.FiltroPorAnonimo;
import ar.edu.utn.frba.dds.entities.filtros.FiltroPorCategoria;
import ar.edu.utn.frba.dds.entities.filtros.FiltroPorFechaAcontecimiento;
import ar.edu.utn.frba.dds.entities.filtros.FiltroPorProvincia;
import ar.edu.utn.frba.dds.entities.filtros.FiltroPorTipoDeFuente;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FiltroFactory {
    public List<FiltroHecho> aDominio(FiltroBusquedaHechosDTO filtroBusquedaHechosDTO){
        List<FiltroHecho> filtroHechos = new ArrayList<>();
        if(filtroBusquedaHechosDTO.getIdCategoria() != null){
            filtroHechos.add(new FiltroPorCategoria(filtroBusquedaHechosDTO.getIdCategoria()));
        }
        if(filtroBusquedaHechosDTO.getProvincia() != null && !filtroBusquedaHechosDTO.getProvincia().isBlank()){
            filtroHechos.add(new FiltroPorProvincia(filtroBusquedaHechosDTO.getProvincia().toLowerCase()));
        }
        if(filtroBusquedaHechosDTO.getFuente() != null){
            switch (filtroBusquedaHechosDTO.getFuente()){
                case "DATASET":
                    filtroHechos.add(new FiltroPorTipoDeFuente(TipoFuente.DATASET));
                    break;
                case "PROXY":
                    filtroHechos.add(new FiltroPorTipoDeFuente(TipoFuente.PROXY));
                    break;
                case "CONTRIBUYENTE":
                    filtroHechos.add(new FiltroPorTipoDeFuente(TipoFuente.CONTRIBUYENTE));
                    break;
            }

        }
        if (filtroBusquedaHechosDTO.getFechaAcontecimientoDesde() != null && filtroBusquedaHechosDTO.getFechaAcontecimientoHasta() != null) {
            filtroHechos.add(new FiltroPorFechaAcontecimiento(filtroBusquedaHechosDTO.getFechaAcontecimientoDesde(), filtroBusquedaHechosDTO.getFechaAcontecimientoHasta()));
            }
        if((filtroBusquedaHechosDTO.getEsAnonimo() != null && filtroBusquedaHechosDTO.getEsAnonimo())){
            filtroHechos.add(new FiltroPorAnonimo(true));
        }
        return filtroHechos;
    }
}
