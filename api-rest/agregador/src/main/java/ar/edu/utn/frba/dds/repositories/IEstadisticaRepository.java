package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.dto.input.estadistica.CategoriaCantidadDTO;
import ar.edu.utn.frba.dds.entities.dto.input.estadistica.HoraCantidadCategoriaDTO;
import ar.edu.utn.frba.dds.entities.dto.input.estadistica.ProvinciaCantidadCategoriaDTO;
import ar.edu.utn.frba.dds.entities.dto.input.estadistica.ProvinciaCantidadDTO;
import ar.edu.utn.frba.dds.entities.solicitud.EstadoSolicitud;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IEstadisticaRepository extends JpaRepository<Hecho, Long> {
  // 1) Provincia con mayor cantidad de hechos en una colección
  @Query("SELECT new ar.edu.utn.frba.dds.entities.dto.input.estadistica.ProvinciaCantidadDTO(c.id,c.titulo,h.ubicacion.provincia, COUNT(h.id)) " +
      "FROM Coleccion c JOIN c.coleccionHechos ch JOIN ch.hecho h WHERE c.id = :idColeccion GROUP BY h.ubicacion.provincia ORDER BY COUNT(h.id) DESC")
  Page<ProvinciaCantidadDTO> contarHechosPorProvinciaDesc(@Param("idColeccion") Long idColeccion, Pageable pageable);


  // 2) Categoria con mayor cantidad de hechos
  @Query("SELECT new ar.edu.utn.frba.dds.entities.dto.input.estadistica.CategoriaCantidadDTO(h.categoria.nombre, COUNT(h)) " +
      "FROM Hecho h GROUP BY h.categoria.nombre ORDER BY COUNT(h) DESC")
  Page<CategoriaCantidadDTO> contarHechosPorCategoriaDesc(Pageable pageable);

  // 3) En qué provincia se presenta la mayor cantidad de hechos de una cierta categoría
  @Query("SELECT new ar.edu.utn.frba.dds.entities.dto.input.estadistica.ProvinciaCantidadCategoriaDTO(h.ubicacion.provincia, COUNT(h),h.categoria.nombre) " +
      "FROM Hecho h " +
      "WHERE h.categoria.id = :catId " +
      "GROUP BY h.ubicacion.provincia " +
      "ORDER BY COUNT(h) DESC")
  Page<ProvinciaCantidadCategoriaDTO> contarCategoriasPorProvinciaDesc(@Param("catId") Long categoriaId, Pageable pageable);


  // 4) A qué hora del día ocurren la mayor cantidad de hechos de una cierta categoría
  @Query(
      value = """
    select new ar.edu.utn.frba.dds.entities.dto.input.estadistica.HoraCantidadCategoriaDTO(
      hour(h.fechaAcontecimiento), count(h),h.categoria.nombre
    )
    from Hecho h
    where h.categoria.id = :catId
    group by hour(h.fechaAcontecimiento)
    order by count(h) desc
    """,
      countQuery = """
    select count(h)
    from Hecho h
    where h.categoria.id = :catId
    """
  )
  Page<HoraCantidadCategoriaDTO> contarHorasDeAcontecimientoPorCategoria(
      @Param("catId") Long categoriaId,
      Pageable pageable
  );

  // 5) Cuántas solicitudes de eliminación son spam
  @Query("SELECT COUNT(s) FROM Solicitud s WHERE s.esSpam = true")
  Long contarPorEstado(@Param("estado") EstadoSolicitud estadoSolicitud);

}
