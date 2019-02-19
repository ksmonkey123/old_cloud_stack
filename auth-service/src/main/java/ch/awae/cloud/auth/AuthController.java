package ch.awae.cloud.auth;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import ch.awae.cloud.exception.BadRequestException;
import ch.awae.cloud.exception.ResourceNotFoundException;
import ch.awae.cloud.security.Security;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@RestController
public class AuthController {

	private UserRepo repo;
	private RoleService roleService;
	private PasswordEncoder crypto;
	private TokenService token;

	@PostMapping("/login")
	public TokenPair login(@Valid @RequestBody LoginRequest request) {
		User user = repo.findByUsername(request.getUsername())
				.filter(u -> crypto.matches(request.getPassword(), u.getPassword()))
				.orElseThrow(() -> new BadRequestException("invalid credentials"));
		user.patchRoles(roleService::getCompletedRoleList);
		return token.generateTokenPair(user);
	}

	@PostMapping("/doLogout")
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

}

@Data
class LoginRequest {
	private @NotBlank String username;
	private @NotBlank String password;
}

@Data
class PasswordChangeRequest {
	private @NotBlank String password;
	private @NotBlank @Size(min = 6, max = 20) String newPassword;
}

