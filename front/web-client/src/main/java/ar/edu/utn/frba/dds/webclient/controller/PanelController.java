package ar.edu.utn.frba.dds.webclient.controller;

import ar.edu.utn.frba.dds.webclient.dto.FiltroSolicitudDTO;
import ar.edu.utn.frba.dds.webclient.dto.input.EstadoSolicitud;
import ar.edu.utn.frba.dds.webclient.dto.input.SolicitudInputDTO;
import ar.edu.utn.frba.dds.webclient.dto.input.TipoSolicitud;
import ar.edu.utn.frba.dds.webclient.service.IHechoService;
import ar.edu.utn.frba.dds.webclient.service.ISolicitudService;
import ar.edu.utn.frba.dds.webclient.service.imp.ColeccionService;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/panel")
public class PanelController {

    private final ColeccionService coleccionService;
    private final ISolicitudService solicitudService;
    private final IHechoService hechoService;

    public PanelController(ColeccionService coleccionService, ISolicitudService solicitudService, IHechoService hechoService) {
        this.coleccionService = coleccionService;
        this.solicitudService = solicitudService;
        this.hechoService = hechoService;
    }

    @GetMapping
    public String panel(Model model) {
        FiltroSolicitudDTO filtro = new FiltroSolicitudDTO();
        filtro.setEstado(EstadoSolicitud.PENDIENTE);

        return cargarPanel(model, filtro);
    }

    @PostMapping
    public String panelFiltrado(@ModelAttribute("filtro") FiltroSolicitudDTO filtro, Model model) {
        return cargarPanel(model, filtro);
    }

    private String cargarPanel(Model model, FiltroSolicitudDTO filtro) {

        List<SolicitudInputDTO> solicitudes = solicitudService.obtenerTodas(filtro);

        model.addAttribute("pageTitle", "Panel de Administrador");
        model.addAttribute("colecciones", coleccionService.obtenerTodasColecciones());

        model.addAttribute("solicitudes", solicitudes);

        // para mantener los valores en los inputs al renderizar
        model.addAttribute("filtro", filtro);

        return "panel/panel";
    }


    @PostMapping("/subir-csv")
    public String subirArchivo(@RequestParam("archivoCSV") MultipartFile archivo, Model model, RedirectAttributes redirectAttributes) {
        if (archivo.isEmpty()) {
            return "redirect:/panel?error=vacio";
        }
        try {
            hechoService.importarHechosCsv(archivo);
            redirectAttributes.addFlashAttribute("successMessage", "Archivo importado exitosamente.");
            return "redirect:/panel";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error al importar el archivo.");
            return "panel/panel";
        }
    }
    @GetMapping("/colecciones/{id}/eliminar")
    public String eliminar(@PathVariable Long id,RedirectAttributes redirectAttributes) {
        try {
            coleccionService.eliminarColeccion(id);
            redirectAttributes.addFlashAttribute("successMessage", "Coleccion eliminado exitosamente.");
            return "redirect:/panel";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la coleccion.");
            return "panel/panel";
        }
    }
}