package ar.edu.utn.frba.dds.webclient.controller;

import ar.edu.utn.frba.dds.webclient.dto.EstadisticaDTO;
import ar.edu.utn.frba.dds.webclient.dto.FiltroDTO;
import ar.edu.utn.frba.dds.webclient.service.ISolicitudService;
import ar.edu.utn.frba.dds.webclient.service.imp.HechoService;
import ar.edu.utn.frba.dds.webclient.service.imp.EstadisticaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/estadisticas")
public class EstadisticaController {

    private final HechoService hechoService;
    private final ISolicitudService solicitudService;
    private final EstadisticaService estadisticaService;

    public EstadisticaController(HechoService hechoService,
                                 ISolicitudService solicitudService,
                                 EstadisticaService estadisticaService) {
        this.hechoService = hechoService;
        this.solicitudService = solicitudService;
        this.estadisticaService = estadisticaService;
    }

    @GetMapping()
    public String administrador(Model model) {
        model.addAttribute("pagina", hechoService.obtenerHechos(new FiltroDTO(),0,0));
        model.addAttribute("solicitudes", solicitudService.obtenerTodas(null).size());
        model.addAttribute("pageTitle", "Estadísticas - Metamapa");
        EstadisticaDTO ultima = estadisticaService.obtenerUltimaEstadistica();
        model.addAttribute("estadistica", ultima);
        model.addAttribute("pageTitle", "Detalle de Estadísticas - Metamapa");
        return "estadisticas/estadisticas";
    }
    @GetMapping("/generar")
    public String generarEstadistica(RedirectAttributes redirectAttributes){
        try{
            estadisticaService.generar();
            redirectAttributes.addFlashAttribute("successMessage", "Estadistica generada");

            return "redirect:/estadisticas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage","Error al generar la estadistica");
            return "redirect:/estadisticas";

        }
    }


}
