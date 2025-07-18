package sato.olx.olx_anuncios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OlxAnunciosApplication {

	public static void main(String[] args) {
		SpringApplication.run(OlxAnunciosApplication.class, args);
	}

}
