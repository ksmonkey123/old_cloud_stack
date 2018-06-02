package ch.awae.cloud.auth;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.awae.cloud.exception.BadRequestException;
import ch.awae.cloud.exception.ResourceNotFoundException;
import ch.awae.cloud.security.Security;
import lombok.Getter;

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

	@PostMapping("/logout")
	public void logout(@RequestHeader("Authorization") String refreshToken) {
		token.remove(refreshToken);
	}

	@PostMapping("/refresh")
	public TokenPair refresh(@RequestHeader("Authorization") String refreshToken) {
		return token.replace(refreshToken);
	}

	@Secured("ROLE_USER")
	@PostMapping("/password")
	public void changeMyPassword(@Valid @RequestBody PasswordChangeRequest request) {
		User user = repo.findById(Security.getUserId())
				.filter(usr -> crypto.matches(request.getPassword(), usr.getPassword()))
				.orElseThrow(() -> new BadRequestException("bad request"));
		user.setPassword(crypto.encode(request.getNewPassword()));
		repo.save(user);
	}

	@Secured("ROLE_USER")
	@GetMapping("/me")
	public User getMyInfo() {
		Long userId = Security.getUserId();
		return repo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/user/{userId}/password")
	public void changeOtherPasssword(@PathVariable("userId") Long userId,
			@Valid @RequestBody PasswordChangeRequest request) {
		repo.findById(Security.getUserId()) //
				.filter(usr -> crypto.matches(request.getPassword(), usr.getPassword()))
				.orElseThrow(() -> new BadRequestException("bad request"));
		User user = repo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));
		user.setPassword(crypto.encode(request.getNewPassword()));
		repo.save(user);
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/user/exists")
	public boolean existsUser(@RequestParam("username") String username) {
		return repo.findByUsername(username).isPresent();
	}

	@Transactional
	@Secured("ROLE_ADMIN")
	@PostMapping("/user")
	public void createUser(@Valid @RequestBody SignupRequest request) {
		if (existsUser(request.getUsername()))
			throw new BadRequestException("user '" + request.getUsername() + "' already exists");
		User user = new User(request.getUsername(), crypto.encode(request.getPassword()), request.isAdmin());
		repo.save(user);
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/users")
	public List<User> getUserList() {
		return repo.findAll();
	}

	@Secured("ROLE_ADMIN")
	@PatchMapping("/user/{userId}/role")
	public void patchUserRole(@PathVariable("userId") Long userId, @Valid @RequestBody RoleChangeRequest request) {
		long myId = Security.getUserId();
		if (myId == userId)
			throw new BadRequestException("cannot change your own roles");
		User user = repo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));
		user.setAdmin(request.isAdmin());
		repo.save(user);
	}

}

@Getter
class LoginRequest {
	private @NotBlank String username;
	private @NotBlank String password;
}

@Getter
class PasswordChangeRequest {
	private @NotBlank String password;
	private @NotBlank @Size(min = 6, max = 20) String newPassword;
}

@Getter
class SignupRequest {
	private @NotBlank @Size(min = 3, max = 15) String username;
	private @NotBlank @Size(min = 6, max = 20) String password;
	private boolean admin;
}

@Getter
class RoleChangeRequest {
	private boolean admin;
}
