package ar.edu.utn.frba.dds.webclient.controller;

import ar.edu.utn.frba.dds.webclient.dto.ArchivoOutputDTO;
import ar.edu.utn.frba.dds.webclient.dto.FiltroDTO;
import ar.edu.utn.frba.dds.webclient.dto.HechoDTO;
import ar.edu.utn.frba.dds.webclient.dto.TipoFuente;
import ar.edu.utn.frba.dds.webclient.dto.TipoMedia;
import ar.edu.utn.frba.dds.webclient.dto.input.RestPageResponse;
import ar.edu.utn.frba.dds.webclient.dto.input.hecho.HechoInputDTO;
import ar.edu.utn.frba.dds.webclient.exceptions.DuplicateEntityException;
import ar.edu.utn.frba.dds.webclient.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.webclient.service.ICategoriaService;
import ar.edu.utn.frba.dds.webclient.service.IHechoService;
import ar.edu.utn.frba.dds.webclient.service.ISolicitudService;
import ar.edu.utn.frba.dds.webclient.service.imp.ColeccionService;
import ar.edu.utn.frba.dds.webclient.service.imp.HechoService;
import ar.edu.utn.frba.dds.webclient.service.imp.LocalMediaStorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ar.edu.utn.frba.dds.webclient.utils.HechoMapper;

@Controller
@RequestMapping("/hechos")
public class HechoController {

    private final IHechoService hechoService;
    private final ColeccionService coleccionService;
    private final ICategoriaService categoriaService;
    private final HechoMapper hechoMapper;
    private final ISolicitudService solicitudService;
    private final LocalMediaStorageService localMediaStorageService;

    public HechoController(HechoService hechoService, ColeccionService coleccionService, ICategoriaService categoriaService, HechoMapper hechoMapper, ISolicitudService solicitudService, LocalMediaStorageService localMediaStorageService) {
        this.hechoService = hechoService;
        this.coleccionService = coleccionService;
        this.categoriaService = categoriaService;
        this.hechoMapper = hechoMapper;
      this.solicitudService = solicitudService;
      this.localMediaStorageService = localMediaStorageService;
    }

    @GetMapping
    public String listarHechos(@ModelAttribute("filtros") FiltroDTO filtros,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size, Model model) throws JsonProcessingException {
        RestPageResponse<HechoInputDTO> pagina = hechoService.obtenerHechos(filtros, page, size);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String hechosJson = mapper.writeValueAsString(pagina.getContent());
        model.addAttribute("categorias", categoriaService.obtenerTodas());
        model.addAttribute("pagina", pagina);
        model.addAttribute("hechosJson", hechosJson);
        model.addAttribute("pageTitle", "MetaMapa - Ver hechos");
        model.addAttribute("queryString", filtros.toQueryString());
        return "hechos/ver-todo";
    }

    @PostMapping("/filtrar")
    public String filtrarHechos(@ModelAttribute FiltroDTO f, RedirectAttributes ra) {
        if (f.getIdColeccion() != null) ra.addAttribute("idColeccion", f.getIdColeccion());
        if (f.getIdCategoria() != null) ra.addAttribute("idCategoria", f.getIdCategoria());
        if (hasText(f.getProvincia())) ra.addAttribute("provincia", f.getProvincia());
        if (hasText(f.getFuente())) ra.addAttribute("fuente", f.getFuente());
        if (f.getFechaAcontecimientoDesde() != null && !f.getFechaAcontecimientoDesde().isEmpty())
            ra.addAttribute("fechaAcontecimientoDesde", f.getFechaAcontecimientoDesde());
        if (f.getFechaAcontecimientoHasta() != null)
            ra.addAttribute("fechaAcontecimientoHasta", f.getFechaAcontecimientoHasta());
        if (Boolean.TRUE.equals(f.getEsAnonimo())) ra.addAttribute("esAnonimo", false);
        if (Boolean.TRUE.equals(f.getCurado())) ra.addAttribute("curado", true);

        return "redirect:/hechos";
    }

    @GetMapping("/crear")
    public String formularioCrearHecho(Model model) {
        model.addAttribute("hecho", new HechoDTO());
        model.addAttribute("categorias", categoriaService.obtenerTodas());
        return "hechos/crear";
    }

    @PostMapping(value = "/crear", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String crearHecho(
            @Valid @ModelAttribute("hecho") HechoDTO hechoDTO,
            BindingResult bindingResult,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            Model model,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categorias", categoriaService.obtenerTodas());
            return "hechos/crear";
        }

        // === LÓGICA DE ANONIMATO ===
        boolean usuarioAutenticado = authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName());

        if (!usuarioAutenticado) {
            hechoDTO.setEsAnonimo(true);
        }


        // === PROCESAR ARCHIVOS MULTIMEDIA ===
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {  // Evita archivos vacíos
                    try {
                        String url = localMediaStorageService.save(file);
                        ArchivoOutputDTO archivo = new ArchivoOutputDTO();
                        archivo.setTipo(TipoMedia.IMAGEN);  // Ajustar si detectás video u otro tipo
                        archivo.setUrl(url);
                        hechoDTO.getArchivos().add(archivo);
                    } catch (Exception ex) {
                        // Si falla guardar un archivo, lo ignoramos o agregamos error
                        model.addAttribute("errorMessage", "Error al guardar uno de los archivos.");
                        model.addAttribute("categorias", categoriaService.obtenerTodas());
                        return "hechos/crear";
                    }
                }
            }
        }

        // === CREAR LA SOLICITUD ===
        try {
            solicitudService.crearSolicitudDeCreacion(hechoDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Solicitud de creación enviada correctamente.");
            return "redirect:/hechos";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue(e.getFieldName(), "duplicate." + e.getFieldName(), e.getMessage());
            model.addAttribute("categorias", categoriaService.obtenerTodas());
            return "hechos/crear";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ocurrió un error inesperado al enviar la solicitud. Por favor, inténtalo más tarde.");
            model.addAttribute("categorias", categoriaService.obtenerTodas());
            return "hechos/crear";  // Volver al formulario con mensaje de error
        }
    }

    @GetMapping({"/{id}"})
    public String DetalleHecho(@PathVariable Long id, Model model) {
        HechoInputDTO hecho = hechoService.obtenerHechoPorId(id);
        System.out.println(hecho.getEsEditable());

        model.addAttribute("hecho", hecho);
        model.addAttribute("pageTitle", "MetaMapa - Ver hecho");
        return "hechos/ver-detalle";
    }

    @GetMapping("/{id}/editar")
    public String formularioEditarHecho(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        System.out.println("ID recibido: " + id);
        try {
            HechoInputDTO hechoInputDTO = hechoService.obtenerHechoPorId(id);
            HechoDTO hecho = hechoMapper.aHechoDTO(hechoInputDTO);
            model.addAttribute("hecho", hecho);
            model.addAttribute("categorias", categoriaService.obtenerTodas());
            model.addAttribute("id", id);
            return "hechos/editar";
        } catch (NotFoundException ex) {
            redirectAttributes.addFlashAttribute("mensaje", ex.getMessage());
            return "redirect:/404";
        }
    }

    @PostMapping("/{id}/editar")
    public String editarHecho(@PathVariable Long id, @Valid @ModelAttribute("hecho") HechoDTO hechoDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        System.out.println("ID recibido para edición: " + id);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("categorias", categoriaService.obtenerTodas());
            return "hechos/editar";
        }
        try{
            solicitudService.crearSolicitudDeEdicion(id, hechoDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Hecho editado correctamente.");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/hechos/{id}/editar";
        }
        return"redirect:/";

    }
    private boolean hasText(String s){ return s!=null && !s.trim().isEmpty(); }
}

