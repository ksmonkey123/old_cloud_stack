package ch.awae.cloud.shorten;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import ch.awae.cloud.security.SecurityConfig;

@Import(SecurityConfig.class)
@SpringBootApplication
public class ShortenServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShortenServiceApplication.class, args);
	}
}
