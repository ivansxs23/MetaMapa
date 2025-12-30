package ar.edu.utn.frba.dds.webclient.controller;

import ar.edu.utn.frba.dds.webclient.dto.LoginRequestDTO;
import ar.edu.utn.frba.dds.webclient.dto.UserDTO;
import ar.edu.utn.frba.dds.webclient.exceptions.DuplicateEntityException;
import ar.edu.utn.frba.dds.webclient.service.IAuthService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {

  private final IAuthService authService;

  public AuthController(IAuthService authService) {
    this.authService = authService;
  }

  // Login
  @GetMapping("/login")
  public String showLogin(Model model) {
    model.addAttribute("credenciales", new LoginRequestDTO());
    return "auth/login";
  }
  // Registro
  @GetMapping("/signup")
  public String showSignup(Model model) {
    model.addAttribute("user", new UserDTO());
    return "auth/sign-up";
  }

  @PostMapping("/signup")
  public String signup(@Valid @ModelAttribute("user") UserDTO userDTO,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      return "auth/sign-up";
    }try{
      authService.signUp(userDTO);
      redirectAttributes.addFlashAttribute("successMessage", "Usuario creado exitosamente");
    }catch (DuplicateEntityException e) {
      bindingResult.rejectValue(e.getFieldName(), "error" + e.getFieldName(), e.getMessage());
      return "auth/sign-up";
    }catch (Exception e){
      model.addAttribute("errorMessage", "Error al crear el usuario: ");
      return "auth/sign-up";
    }
    return "redirect:/auth/login";
  }
}
