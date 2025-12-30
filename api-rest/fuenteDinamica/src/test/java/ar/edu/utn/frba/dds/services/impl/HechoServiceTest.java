package ar.edu.utn.frba.dds.services.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import ar.edu.utn.frba.dds.Exceptions.NotEditableException;
import ar.edu.utn.frba.dds.entities.Hecho;
import ar.edu.utn.frba.dds.entities.InputHechoDto;
import ar.edu.utn.frba.dds.repositories.IHechoRepository;
import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;

public class HechoServiceTest {

  @Mock
  private IHechoRepository hechoRepository;

  @InjectMocks
  private HechoService hechoService;

  private Hecho hechoExistente;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    hechoExistente = new Hecho();
    hechoExistente.setId(1L);
    hechoExistente.setVigente(true);
    hechoExistente.setTitulo("Incendio en Córdoba");
    hechoExistente.setDescripcion("Foco activo en zona rural");
    hechoExistente.setIdCategoria(1L);
    hechoExistente.setFechaAcontecimiento(LocalDateTime.of(2024, 6, 15, 14, 0));
    hechoExistente.setUsername("usuarioTest");
    hechoExistente.setEsAnonimo(false);
    hechoExistente.setEsEditable(true);
    hechoExistente.setFechaCarga(LocalDateTime.now());
    hechoExistente.setUltimaEdicion(LocalDateTime.now());
  }

  // GET HECHOS
  @Test
  void testGetHechos() {
    when(hechoRepository.findByVigenteTrue()).thenReturn(List.of(hechoExistente));

    Page<Hecho> result = hechoService.buscar(null, null);
    assertEquals(1, result.getTotalElements());
  }

  // AGREGAR HECHO
  @Test
  @DisplayName("Agregar hecho con usuario autenticado")
  void testAgregarHecho() {
    InputHechoDto dto = new InputHechoDto();
    dto.setTitulo("Nuevo incendio");
    dto.setDescripcion("Zona urbana afectada");
    dto.setIdCategoria(2L);
    dto.setLatitud(-34.6037);
    dto.setLongitud(-58.3816);
    dto.setFechaAcontecimiento(LocalDateTime.of(2024, 7, 10, 18, 30));
    dto.setEsAnonimo(false);

    Hecho hechoGuardado = new Hecho();
    hechoGuardado.setId(2L);
    hechoGuardado.setVigente(true);
    hechoGuardado.setTitulo(dto.getTitulo());
    hechoGuardado.setDescripcion(dto.getDescripcion());
    hechoGuardado.setIdCategoria(dto.getIdCategoria());
    hechoGuardado.setFechaAcontecimiento(dto.getFechaAcontecimiento());
    hechoGuardado.setUsername("usuarioTest");
    hechoGuardado.setEsAnonimo(dto.getEsAnonimo());
    hechoGuardado.setEsEditable(true);
    hechoGuardado.setUsername("usuarioTest");
    hechoGuardado.setFechaCarga(LocalDateTime.now());
    hechoGuardado.setUltimaEdicion(LocalDateTime.now());

    when(hechoRepository.save(any(Hecho.class))).thenReturn(hechoGuardado);

    Hecho result = hechoService.agregarHecho(dto);

    assertNotNull(result);
    assertEquals("Nuevo incendio", result.getTitulo());
    assertEquals("Zona urbana afectada", result.getDescripcion());
    assertFalse(result.getEsAnonimo());
    assertEquals("usuarioTest", result.getUsername());
    assertNotNull(result.getFechaCarga());
    assertNotNull(result.getUltimaEdicion());
    verify(hechoRepository, times(1)).save(any(Hecho.class));
  }

  @Test
  @DisplayName("Agregar hecho sin usuario autenticado (anónimo)")
  void testAgregarHechoSinUsuario() {
    InputHechoDto dto = new InputHechoDto();
    dto.setTitulo("Hecho anónimo");
    dto.setDescripcion("Descripción anónima");
    dto.setIdCategoria(3L);
    dto.setLatitud(-31.0);
    dto.setLongitud(-64.0);
    dto.setFechaAcontecimiento(LocalDateTime.now());
    dto.setEsAnonimo(true);

    Hecho hechoGuardado = new Hecho();
    hechoGuardado.setId(3L);
    hechoGuardado.setTitulo(dto.getTitulo());
    hechoGuardado.setDescripcion(dto.getDescripcion());
    hechoGuardado.setUsername("anonymousUser");
    hechoGuardado.setEsAnonimo(true);
    hechoGuardado.setEsEditable(false);
    hechoGuardado.setVigente(true);
    hechoGuardado.setFechaCarga(LocalDateTime.now());
    hechoGuardado.setUltimaEdicion(LocalDateTime.now());

    when(hechoRepository.save(any(Hecho.class))).thenReturn(hechoGuardado);

    Hecho result = hechoService.agregarHecho(dto);

    assertNotNull(result);
    assertEquals("anonymousUser", result.getUsername());
    assertTrue(result.getEsAnonimo());
    assertFalse(result.getEsEditable());
    verify(hechoRepository, times(1)).save(any(Hecho.class));
  }


  // EDITAR HECHO
  @Test
  @DisplayName("Editar hecho existente por su propietario")
  void testEditarHecho() {
    InputHechoDto dtoEditado = new InputHechoDto();
    dtoEditado.setTitulo("Incendio controlado");
    dtoEditado.setDescripcion("Situación bajo control");
    dtoEditado.setIdCategoria(1L);
    dtoEditado.setUsuario("usuarioTest");

    when(hechoRepository.findById(1L)).thenReturn(Optional.of(hechoExistente));
    when(hechoRepository.save(any(Hecho.class))).thenAnswer(inv -> inv.getArgument(0));

    Hecho result = hechoService.modificarHecho(1L, dtoEditado);

    assertEquals("Incendio controlado", result.getTitulo());
    assertEquals("Situación bajo control", result.getDescripcion());
    assertNotNull(result.getUltimaEdicion());
    verify(hechoRepository, times(1)).findById(1L);
    verify(hechoRepository, times(1)).save(any(Hecho.class));
  }

  @Test
  @DisplayName("Intento de editar hecho de otro usuario debe fallar")
  void testModificarHechoDeOtroUsuarioDebeFallar() {
    InputHechoDto dtoEditado = new InputHechoDto();
    dtoEditado.setTitulo("Intento no autorizado");
    dtoEditado.setDescripcion("Otro usuario intenta editar");
    dtoEditado.setUsuario("otroUsuario");

    when(hechoRepository.findById(1L)).thenReturn(Optional.of(hechoExistente));

    Exception exception = assertThrows(AccessDeniedException.class, () -> hechoService.modificarHecho(1L, dtoEditado));

    assertEquals(AccessDeniedException.class, exception.getClass());
    verify(hechoRepository, times(1)).findById(1L);
    verify(hechoRepository, never()).save(any(Hecho.class));
  }
  @Test
  @DisplayName("Intento de editar hecho no editable debe fallar")
  void testModificarHechoNoEditableDebeFallar() {
    hechoExistente.setEsEditable(false); // ya no editable
    when(hechoRepository.findById(1L)).thenReturn(Optional.of(hechoExistente));

    InputHechoDto dtoEditado = new InputHechoDto();
    dtoEditado.setTitulo("Intento de edición");
    dtoEditado.setDescripcion("Este hecho no es editable");
    dtoEditado.setUsuario("usuarioTest");

    Exception exception = assertThrows(NotEditableException.class, () -> hechoService.modificarHecho(1L, dtoEditado));

    assertEquals(NotEditableException.class, exception.getClass());
    verify(hechoRepository, times(1)).findById(1L);
    verify(hechoRepository, never()).save(any(Hecho.class));
  }

  @Test
  @DisplayName("Intento de editar hecho inexistente debe fallar")
  void testModificarHechoInexistenteDebeFallar() {
    when(hechoRepository.findById(999L)).thenReturn(Optional.empty());

    InputHechoDto dtoEditado = new InputHechoDto();
    dtoEditado.setTitulo("Hecho inexistente");
    dtoEditado.setUsuario("usuarioTest");

    Exception exception = assertThrows(RuntimeException.class, () -> hechoService.modificarHecho(999L, dtoEditado));

    assertEquals("Hecho no encontrado", exception.getMessage());
    verify(hechoRepository, times(1)).findById(999L);
    verify(hechoRepository, never()).save(any(Hecho.class));
  }


}
