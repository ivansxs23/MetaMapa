package ar.edu.utn.frba.dds.entities;

import ar.edu.utn.frba.dds.entities.detectorDeSpam.DetectorDeSpamTFIDF;
import ar.edu.utn.frba.dds.entities.detectorDeSpam.Documento;
import ar.edu.utn.frba.dds.repositories.CorpusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DetectorDeSpamTFIDFTest {

    @Autowired
    private DetectorDeSpamTFIDF detector;

    @Autowired
    private CorpusRepository corpusRepository;

    @BeforeEach
    void setup() {
        System.out.println("Cantidad en corpus: " + corpusRepository.count());
    }

    @BeforeEach
    void initCorpus() {
        if (corpusRepository.count() == 0) {
            List<Documento> docs = Arrays.asList(
                    new Documento("Este es un mensaje común de ejemplo sin spam"),
                    new Documento("Texto de prueba sin publicidad ni engaños"),
                    new Documento("Los usuarios suelen escribir mensajes normales"),
                    new Documento("Hoy hace un buen día para caminar tranquilo"),
                    new Documento("La tecnología avanza cada año más rápido"),
                    new Documento("Aprender programación requiere práctica constante"),
                    new Documento("El café de la mañana ayuda a concentrarse mejor"),
                    new Documento("Los deportes mejoran la salud física y mental"),
                    new Documento("El cine es una forma de arte muy popular"),
                    new Documento("Viajar permite conocer nuevas culturas y personas"),
                    new Documento("La música relaja y también motiva en distintos momentos"),
                    new Documento("El trabajo en equipo es clave para lograr objetivos"),
                    new Documento("Un libro interesante puede cambiar la manera de pensar"),
                    new Documento("El agua es fundamental para la vida en la Tierra"),
                    new Documento("Los amigos son un apoyo importante en la vida diaria")
            );
            corpusRepository.saveAll(docs);
        }
        System.out.println("Cantidad en corpus: " + corpusRepository.count());
    }

    @Test
    void testTextoNormal() {
        String texto = "Hola, esto es un mensaje normal sin spam";
        assertFalse(detector.esSpam(texto));
    }

    @Test
    void testSpamObvio() {
        String texto = "¡¡¡COMPRALOOO YA!!!increíbleee AHORAAAA mismooo";
        assertTrue(detector.esSpam(texto));
    }


    @Test
    void testTextoConPalabraProhibida() {
        String texto = "¡¡¡GRATIS!!! Gana dinero rápido ahora!!!";
        assertTrue(detector.esSpam(texto));
    }

    @Test
    void testTextoNormalLargo() {
        String texto = "Hoy comparto con ustedes un análisis extenso sobre cómo la colaboración en comunidades " +
                "puede mejorar el aprendizaje colectivo sin recurrir a trampas ni engaños.";
        assertFalse(detector.esSpam(texto));
    }

    @Test
    void testTextoConRepeticiones() {
        String texto = "Compraaaa ahoooora mismooo increiiiibleeee ofertaaaaa!!!";
        assertTrue(detector.esSpam(texto));
    }

    @Test
    void testTextoConMuchasStopWords() {
        String texto = "El la los las de que en y a un una el la que de en";
        assertFalse(detector.esSpam(texto));
    }

}

