package ch.awae.cloud.auth;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ch.awae.cloud.exception.BadRequestException;

@RestController
public class AuthController {

	@Autowired
	UserRepo repo;

	@Autowired
	PasswordEncoder crypto;

	@Autowired
	TokenService token;

	@PostMapping("/login")
	public TokenPair login(@Valid @RequestBody LoginRequest request) {
		User user = repo.findByUsername(request.getUsername())
				.filter(u -> crypto.matches(request.getPassword(), u.getPassword()))
				.orElseThrow(() -> new BadRequestException("invalid credentials"));
		return token.generateTokenPair(user);
	}

}

interface UserRepo extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

}
