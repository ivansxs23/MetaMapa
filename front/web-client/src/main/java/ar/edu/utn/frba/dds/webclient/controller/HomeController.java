package ar.edu.utn.frba.dds.webclient.controller;
import ar.edu.utn.frba.dds.webclient.dto.input.coleccion.ColeccionInputDTO;
import ar.edu.utn.frba.dds.webclient.service.IColeccionService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

 private final IColeccionService coleccionService;

    public HomeController(IColeccionService coleccionService) {
        this.coleccionService = coleccionService;
    }


    @GetMapping("/")
  public String home(Model model) {
      List<ColeccionInputDTO> primerasTres = coleccionService.obtenerTodasColecciones()
          .stream()
          .limit(3)
          .toList();
    model.addAttribute("pageTitle", "MetaMapa - Inicio");
    model.addAttribute("colecciones",primerasTres);
    return "home/landing";
  }



  @GetMapping("/error")
  public String notFound(Model model) {
    model.addAttribute("titulo", "El recurso buscado no existe, lo sentimos");
    model.addAttribute("mensaje", "mejor suerte la proxima");
    return "error/404";
  }
}
