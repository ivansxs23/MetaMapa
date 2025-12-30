package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.entities.Hecho;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IHechoRepository extends JpaRepository<Hecho, Long>, JpaSpecificationExecutor<Hecho> {
    Hecho findByTitulo(String titulo);
    List<Hecho> findAllByTituloIn(List<String> titulos);
}
