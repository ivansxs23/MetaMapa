package ar.edu.utn.frba.dds.utils;


import ar.edu.utn.frba.dds.entities.Categoria;
import ar.edu.utn.frba.dds.entities.criterios.Criterio;
import ar.edu.utn.frba.dds.entities.criterios.CriterioPorCategoria;
import ar.edu.utn.frba.dds.entities.criterios.CriterioPorRangoFechaAcontecimiento;
import ar.edu.utn.frba.dds.entities.criterios.CriterioPorProvincia;
import ar.edu.utn.frba.dds.entities.dto.input.Criterio.CriterioDTO;
import ar.edu.utn.frba.dds.entities.dto.input.Criterio.CriterioPorCategoriaDTO;
import ar.edu.utn.frba.dds.entities.dto.input.Criterio.CriterioPorFechaDTO;
import ar.edu.utn.frba.dds.entities.dto.input.Criterio.CriterioPorProvinciaDTO;
import ar.edu.utn.frba.dds.services.impl.CategoriaService;
import org.springframework.stereotype.Component;

@Component
public class CriterioFactory {

    private final CategoriaService categoriaService;

    public CriterioFactory(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    public Criterio aDominio(CriterioDTO dto) {
        if (dto instanceof CriterioPorFechaDTO f) {
          return new CriterioPorRangoFechaAcontecimiento(f.getFechaDesde(), f.getFechaHasta());
        }
        if (dto instanceof CriterioPorProvinciaDTO u) {
          return new CriterioPorProvincia(u.getProvincia());
        }
        if (dto instanceof CriterioPorCategoriaDTO cat) {
            Categoria categoria = categoriaService.buscarPorId(cat.getIdCategoria());
            return new CriterioPorCategoria(categoria);
        }
        throw new IllegalArgumentException("Tipo de criterio no soportado: " + dto.getClass().getSimpleName());
    }
}