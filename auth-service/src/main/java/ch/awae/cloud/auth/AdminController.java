package ch.awae.cloud.auth;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.awae.cloud.exception.BadRequestException;
import ch.awae.cloud.exception.ResourceNotFoundException;
import ch.awae.cloud.security.Security;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;

@AllArgsConstructor
@RestController
public class AdminController {

	private UserRepo repo;
	private RoleRepository roleRepo;
	private RoleService roleService;
	private PasswordEncoder crypto;

	@Secured("ROLE_ADMIN")
	@PostMapping("/user/{userId}/password")
	public void changeOtherPasssword(@PathVariable("userId") Long userId,
			@Valid @RequestBody PasswordChangeRequestByAdmin request) {
		repo.findById(Security.getUserId()) //
				.orElseThrow(() -> new BadRequestException("bad request"));
		User user = repo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));
		user.setPassword(crypto.encode(request.getPassword()));
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
		User user = new User(request.getUsername(), crypto.encode(request.getPassword()), null);
		repo.save(user);
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/users")
	public List<User> getUserList() {
		val list = repo.findAll();
		for (val user : list)
			user.patchRoles(roleService::getCompletedRoleList);
		return list;
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/roles")
	public List<Role> getRoles() {
		return roleRepo.findAllByOrderBySortAscNameAsc();
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/user/{userId}")
	public User getUser(@PathVariable("userId") Long userId) {
		return repo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));
	}

	@Secured("ROLE_ADMIN")
	@PatchMapping("/user/{userId}/role")
	public List<User> patchUserRole(@PathVariable("userId") Long userId,
			@Valid @RequestBody RoleChangeRequest request) {
		long myId = Security.getUserId();
		if (myId == userId)
			throw new BadRequestException("cannot change your own roles");
		User user = repo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));

		val roles = request.getRoles();
		if (roles.contains("ROLE_ADMIN"))
			user.setRoles(Arrays.asList("ROLE_ADMIN"));
		else
			user.setRoles(roles);

		repo.save(user);

		return getUserList();
	}
	
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/user/{userId}")
	public void deleteUser(@PathVariable("userId") Long userId) {
		long myId = Security.getUserId();
		if (myId == userId)
			throw new BadRequestException("cannot delete yourself!");
		User user = repo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));
		repo.delete(user);
	}

}

@Data
class SignupRequest {
	private @NotBlank @Size(min = 3, max = 15) String username;
	private @NotBlank @Size(min = 6, max = 20) String password;
}

@Data
class RoleChangeRequest {
	private List<String> roles;
}

@Data
class PasswordChangeRequestByAdmin {
	private @NotBlank @Size(min = 6, max = 20) String password;
}
