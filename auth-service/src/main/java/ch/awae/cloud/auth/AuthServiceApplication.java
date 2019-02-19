package ch.awae.cloud.auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.awae.cloud.security.SecurityConfig;

@Import(SecurityConfig.class)
@SpringBootApplication
@EnableScheduling
public class AuthServiceApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CommandLineRunner runner(PasswordEncoder crypt, UserRepo repo) {
		return args -> {
			if (!repo.findByUsername("test").isPresent())
				repo.save(new User("test", crypt.encode("password"), null));
		};
	}

}
