package ar.edu.utn.frba.dds.webclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ar.edu.utn.frba.dds.webclient")
public class WebClientApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebClientApplication.class, args);
	}
}

