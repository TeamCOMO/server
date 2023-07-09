package project.como;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ComoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComoApplication.class, args);
	}

}
