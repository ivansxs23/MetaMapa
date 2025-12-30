package ar.edu.utn.frba.dds.webclient;

import ar.edu.utn.frba.dds.webclient.controller.HomeController;
import ar.edu.utn.frba.dds.webclient.dto.input.coleccion.ColeccionInputDTO;
import ar.edu.utn.frba.dds.webclient.service.imp.ColeccionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
@Import(HomeControllerTest.TestConfig.class) // importamos el mock manual
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ColeccionService coleccionService;

    @Test
    void testListarColecciones() throws Exception {
        // Creamos colecciones de prueba
        ColeccionInputDTO c1 = new ColeccionInputDTO();
        c1.setId(1L);
        c1.setTitulo("Incidentes urbanos");
        c1.setDescripcion("Reportes verificados de cortes, obras y accidentes.");

        ColeccionInputDTO c2 = new ColeccionInputDTO();
        c2.setId(2L);
        c2.setTitulo("Avistajes");
        c2.setDescripcion("Hechos subidos por la comunidad.");

        when(coleccionService.obtenerTodasColecciones())
                .thenReturn(Arrays.asList(c1, c2));

        mockMvc.perform(get("/lista"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("colecciones"))
                .andExpect(model().attributeExists("titulo"))
                .andExpect(view().name("colecciones/lista"));
    }

    // Configuración para inyectar el mock en el controller
    static class TestConfig {
        @Mock
        private ColeccionService coleccionService;

        public TestConfig() {}
    }
}
