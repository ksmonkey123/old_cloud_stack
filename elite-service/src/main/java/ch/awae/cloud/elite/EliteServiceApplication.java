package ch.awae.cloud.elite;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.awae.cloud.security.SecurityConfig;

@Import(SecurityConfig.class)
@SpringBootApplication
public class EliteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EliteServiceApplication.class, args);
	}
}


@RestController
class CheckEndpointController {

	@GetMapping("/test")
	public String testEndpoint(@Value("${spring.application.name}") String name) {
		return name;
	}

}
