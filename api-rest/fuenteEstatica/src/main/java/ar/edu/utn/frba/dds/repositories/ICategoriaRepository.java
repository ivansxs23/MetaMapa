package ar.edu.utn.frba.dds.repositories;
import ar.edu.utn.frba.dds.entities.Categoria;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> getCategoriaByNombre(String nombre);
}
