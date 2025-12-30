package ar.edu.utn.frba.dds.repositories;


import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.TipoFuente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface IHechoRepository extends JpaRepository<Hecho, Long>, JpaSpecificationExecutor<Hecho> {
  Optional<Hecho> findByIdentificador(String identificador);
  Optional<Hecho> findByOrigen_TipoFuenteAndOrigen_IdEnFuente(TipoFuente tipoFuente, Long idFuente);
  Hecho findByIdAndActivoTrue(Long id);
}
