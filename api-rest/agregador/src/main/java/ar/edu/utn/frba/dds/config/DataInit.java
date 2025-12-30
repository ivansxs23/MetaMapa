package ar.edu.utn.frba.dds.config;
import ar.edu.utn.frba.dds.entities.Coleccion;
import ar.edu.utn.frba.dds.entities.TipoFuente;
import ar.edu.utn.frba.dds.entities.algoritmoDeConsenso.AlgoritmoMayoriaSimple;
import ar.edu.utn.frba.dds.entities.algoritmoDeConsenso.TipoAlgoritmo;
import ar.edu.utn.frba.dds.repositories.IColeccionRepository;
import ar.edu.utn.frba.dds.services.impl.AgregadorService;
import ar.edu.utn.frba.dds.services.impl.CategoriaService;
import ar.edu.utn.frba.dds.utils.AlgoritmoFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInit implements CommandLineRunner {
  private final IColeccionRepository coleccionRepository;
  private final CategoriaService categoriaService;
  private final AgregadorService agregadorService;
  private final AlgoritmoFactory algoritmoFactory;

  public DataInit(IColeccionRepository coleccionRepository, CategoriaService categoriaService, AgregadorService agregadorService, AlgoritmoFactory algoritmoFactory) {
    this.coleccionRepository = coleccionRepository;
    this.categoriaService = categoriaService;
    this.agregadorService = agregadorService;
    this.algoritmoFactory = algoritmoFactory;
  }

  @Override
  public void run(String... args) {
    if (categoriaService.obtenerTodas().isEmpty()) {
      categoriaService.buscarOCrearCategoria("Caída de aeronave");
      categoriaService.buscarOCrearCategoria("Medio Ambiente");
      System.out.println("Se han agregado 2 categorias de prueba a la base de datos.");
    }
    if (coleccionRepository.count() == 0) {
      Coleccion coleccion = new Coleccion();
      coleccion.setTitulo("Hechos subidos por usuarios");
      coleccion.setDescripcion("Coleccion que almacena hechos subidos por usuarios");
      coleccion.setAlgoritmo(new AlgoritmoMayoriaSimple());
      coleccion.agregarFuente(TipoFuente.CONTRIBUYENTE);
      coleccionRepository.save(coleccion);

      Coleccion coleccion2 = new Coleccion();
      coleccion2.setTitulo("Hechos de datasets");
      coleccion2.setDescripcion("Hechos obtenidos de archivo .cvs");
      coleccion2.setAlgoritmo(algoritmoFactory.aDominio(TipoAlgoritmo.SIMPLE));
      coleccion2.agregarFuente(TipoFuente.DATASET);
      coleccionRepository.save(coleccion2);

      Coleccion coleccion3 = new Coleccion();
      coleccion3.setTitulo("Hechos de api externa");
      coleccion3.setDescripcion("Esta coleccion solo tiene hechos subidos por otra organizacion");
      coleccion3.setAlgoritmo(algoritmoFactory.aDominio(TipoAlgoritmo.SIMPLE));
      coleccion3.agregarFuente(TipoFuente.PROXY);
      coleccionRepository.save(coleccion3);

      Coleccion coleccion4 = new Coleccion();
      coleccion4.setTitulo("Hechos de dataSets y usuarios");
      coleccion4.setDescripcion("Coleccion con hechos tanto de usuarios como de datasets");
      coleccion4.setAlgoritmo(algoritmoFactory.aDominio(TipoAlgoritmo.ABSOLUTA));
      coleccion4.agregarFuente(TipoFuente.CONTRIBUYENTE);
      coleccion4.agregarFuente(TipoFuente.DATASET);
      coleccionRepository.save(coleccion4);
      System.out.println("Se ha agregado 4 colecciones de prueba a la base de datos.");

      agregadorService.importarHechosYActualizarColecciones();
    }
  }
}
