package ar.edu.utn.frba.dds.webclient.controller;

import ar.edu.utn.frba.dds.webclient.dto.FiltroSolicitudDTO;
import ar.edu.utn.frba.dds.webclient.dto.SolicitudDTO;
import ar.edu.utn.frba.dds.webclient.service.ISolicitudService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/solicitudes")
public class SolicitudController {

    private final ISolicitudService solicitudService;

    public SolicitudController(ISolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @GetMapping
    public String listarSolicitudes(Model model, @ModelAttribute FiltroSolicitudDTO filtro) {
        model.addAttribute("solicitudes", solicitudService.obtenerTodas(filtro));
        model.addAttribute("pageTitle", "Solicitudes de Eliminación");
        return "panel/solicitudes";
    }

    @GetMapping("/crear/{hechoId}")
    public String mostrarFormularioCreacion(@PathVariable Long hechoId, Model model) {
        SolicitudDTO solicitudDto = new SolicitudDTO();
        solicitudDto.setIdHecho(hechoId);
        model.addAttribute("solicitudDto", solicitudDto);
        model.addAttribute("pageTitle", "Reportar Hecho");
        return "solicitudes/crear"; // HTML en carpeta solicitudes
    }

    @PostMapping
    public String crearSolicitud(@Valid @ModelAttribute("solicitudDto") SolicitudDTO solicitudDTO,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Solicitud creada exitosamente");
            return "solicitudes/crear";
        }

        try {
            solicitudService.crearSolicitudDeEliminacion(solicitudDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Solicitud creada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Solicitud creada exitosamente");
            return "solicitudes/crear";
        }
        return "redirect:/";
    }



    @GetMapping("/{id}/aprobar")
    public String aprobarSolicitud(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            solicitudService.aprobarSolicitud(id);
            redirectAttributes.addFlashAttribute("successMessage", "Solicitud aprobada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/panel";
    }

    @GetMapping("/{id}/denegar")
    public String denegarSolicitud(@PathVariable Long id) {
        solicitudService.denegarSolicitud(id);
        return "redirect:/panel";
    }
}
