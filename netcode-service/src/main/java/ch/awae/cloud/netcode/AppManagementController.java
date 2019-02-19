package ch.awae.cloud.netcode;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ch.awae.cloud.exception.ResourceNotFoundException;
import ch.awae.cloud.security.Security;
import lombok.Getter;

@RestController
public class AppManagementController {

	private @Autowired AppRepo repo;
	private @Autowired Generator generator;

	@GetMapping("/list")
	@Secured("ROLE_NETCODE")
	public List<App> getUserAppList() {
		return repo.findByUserOrderByCreatedAsc(Security.getUserId());
	}

	@PostMapping("/app")
	@Secured("ROLE_NETCODE")
	public App createApp(@Valid @RequestBody CreateRequest request) {
		long userId = Security.getUserId();
		String id = generator.generateSequence(s -> !repo.findByIdentifier(s).isPresent());
		String name = request.getName();

		App app = new App(userId, id, name);

		return repo.save(app);
	}

	@DeleteMapping("/app/{identifier}")
	@Secured("ROLE_NETCODE")
	public void deleteApp(@PathVariable("identifier") String id) {
		App app = repo.findByIdentifierAndUser(id, Security.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("app", "identifier", id));

		repo.delete(app);
	}

}

@Getter
class CreateRequest {
	private String name;
}

@Getter
class DeleteRequest {
	private String identifier;
}