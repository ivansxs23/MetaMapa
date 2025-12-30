package ar.edu.utn.frba.dds.webclient.controller;



import ar.edu.utn.frba.dds.webclient.dto.CategoriaDTO;
import ar.edu.utn.frba.dds.webclient.dto.FiltroDTO;
import ar.edu.utn.frba.dds.webclient.dto.input.RestPageResponse;
import ar.edu.utn.frba.dds.webclient.dto.input.coleccion.ColeccionDetalladoInputDTO;
import ar.edu.utn.frba.dds.webclient.dto.input.coleccion.ColeccionInputDTO;
import ar.edu.utn.frba.dds.webclient.dto.input.hecho.HechoInputDTO;
import ar.edu.utn.frba.dds.webclient.dto.output.ColeccionDTO;
import ar.edu.utn.frba.dds.webclient.service.ICategoriaService;
import ar.edu.utn.frba.dds.webclient.service.IColeccionService;

import ar.edu.utn.frba.dds.webclient.service.IHechoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/colecciones")
public class ColeccionController {

    private final IColeccionService coleccionService;
    private final ICategoriaService categoriaService;
    private final IHechoService hechoService;

    public ColeccionController(IColeccionService coleccionService, ICategoriaService categoriaService, IHechoService hechoService) {
        this.coleccionService = coleccionService;
      this.categoriaService = categoriaService;
        this.hechoService = hechoService;
    }

    @GetMapping()
    public String listarColecciones(Model model) {
        model.addAttribute("colecciones", coleccionService.obtenerTodasColecciones());
        model.addAttribute("pageTitle", "MetaMapa - Colecciones");
        return "colecciones/lista";
    }

    @GetMapping("/{id}")
    public String verDetalleColeccion(@PathVariable Long id,@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size, @ModelAttribute("filtros") FiltroDTO filtros, Model model) throws JsonProcessingException {
        ColeccionDetalladoInputDTO coleccion = coleccionService.obtenerColeccionPorId(id);
        RestPageResponse<HechoInputDTO> pagina = coleccionService.obtenerHechosDeColeccion(id,page,size, filtros);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String hechosJson = mapper.writeValueAsString(pagina.getContent());
        model.addAttribute("categorias", categoriaService.obtenerTodas());
        model.addAttribute("coleccion", coleccion);
        model.addAttribute("pagina", pagina);
        model.addAttribute("hechosJson", hechosJson);
        model.addAttribute("pageTitle", "MetaMapa - Detalle de Colección");
        model.addAttribute("queryString", filtros.toQueryString());
        return "colecciones/detalle";
    }
    @PostMapping("/{id}/filtrar")
    public String filtrar(@PathVariable Long id, @ModelAttribute FiltroDTO f, RedirectAttributes ra) {
        if (f.getIdCategoria()!=null)              ra.addAttribute("idCategoria", f.getIdCategoria());
        if (hasText(f.getProvincia()))             ra.addAttribute("provincia", f.getProvincia());
        if (hasText(f.getFuente()))             ra.addAttribute("fuente", f.getFuente());
        if (f.getFechaAcontecimientoDesde()!=null) ra.addAttribute("fechaAcontecimientoDesde", f.getFechaAcontecimientoDesde());
        if (f.getFechaAcontecimientoHasta()!=null) ra.addAttribute("fechaAcontecimientoHasta", f.getFechaAcontecimientoHasta());
        if (Boolean.TRUE.equals(f.getEsAnonimo())) ra.addAttribute("esAnonimo", true);
        if (Boolean.TRUE.equals(f.getCurado()))    ra.addAttribute("curado", true);
        return "redirect:/colecciones/{id}";
    }

    @GetMapping("/{idColeccion}/hechos/{idHecho}")
    public String verHechoDeColeccion(@PathVariable Long idColeccion,@PathVariable Long idHecho, Model model) {
        HechoInputDTO hecho = hechoService.obtenerHechoPorId(idHecho);
        model.addAttribute("hecho", hecho);
        model.addAttribute("coleccionId", idColeccion);
        return "hechos/ver-detalle";
    }

    @GetMapping("/crear")
    public String mostrarFormulario(Model model) {
        ColeccionInputDTO coleccion = new ColeccionInputDTO();
        coleccion.setCriterios(new ArrayList<>());
        List<CategoriaDTO> categorias = categoriaService.obtenerTodas();
        model.addAttribute("categorias", categorias);
        model.addAttribute("coleccion", coleccion);
        return "colecciones/crear";
    }

    @PostMapping("/crear")
    public String crear(@ModelAttribute("coleccion") ColeccionDTO dto,
                        RedirectAttributes redirectAttributes) {

        String mensaje = coleccionService.crearColeccion(dto);

        redirectAttributes.addFlashAttribute("successMessage", mensaje);

        return "redirect:/colecciones";
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(
            @PathVariable Long id,
            Model model
    ) {
        ColeccionDetalladoInputDTO coleccion = coleccionService.obtenerColeccionPorId(id);

        if (coleccion.getCriterios() == null) {
            coleccion.setCriterios(new ArrayList<>());
        }
        List<CategoriaDTO> categorias = categoriaService.obtenerTodas();

        model.addAttribute("categorias", categorias);
        model.addAttribute("coleccion", coleccion);
        return "colecciones/editar";
    }


    @PostMapping("/{id}/editar")
    public String editarColeccion(
            @PathVariable Long id,
            @ModelAttribute("coleccion") ColeccionDTO dto,
            RedirectAttributes redirectAttributes
    ) {
        coleccionService.editarColeccion(id, dto);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Colección editada correctamente"
        );

        return "redirect:/colecciones";
    }
    @GetMapping("/{id}/eliminar")
    public String eliminarColeccione(@PathVariable Long id, RedirectAttributes redirectAttributes){
        coleccionService.eliminarColeccion(id);
        redirectAttributes.addFlashAttribute("successMessage", "Coleccion eliminada exitosamente");
        return "redirect:/panel";
    }

    private boolean hasText(String s){ return s!=null && !s.trim().isEmpty(); }
}
