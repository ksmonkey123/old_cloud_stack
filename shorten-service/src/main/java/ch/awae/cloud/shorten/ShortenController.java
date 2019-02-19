package ch.awae.cloud.shorten;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ch.awae.cloud.exception.ResourceNotFoundException;
import ch.awae.cloud.security.Security;
import lombok.Getter;

@RestController
public class ShortenController {

	private @Autowired EntryRepo repo;
	private @Autowired Generator generator;

	@GetMapping("/list")
	@Secured("ROLE_SHORTEN")
	public List<Entry> getUserLinkList() {
		return repo.findByUserOrderByCreatedDesc(Security.getUserId());
	}

	@PostMapping("/link")
	@Secured("ROLE_SHORTEN")
	public Entry createLink(@Valid @RequestBody CreateRequest request) {
		long userId = Security.getUserId();
		String id = generator.generateSequence(s -> !repo.findByIdentifier(s).isPresent());
		String target = request.getTarget();

		return repo.save(new Entry(userId, id, target));
	}

	@PatchMapping("/link/{identifier}")
	@Secured("ROLE_SHORTEN")
	public Entry patchLink(@PathVariable("identifier") String id, @Valid @RequestBody PatchRequest request) {
		Entry e = repo.findByIdentifier(id).orElseThrow(() -> new ResourceNotFoundException("link", "identifier", id));

		if (request.getActive() != null)
			e.setActive(request.getActive());

		return repo.save(e);
	}

	@GetMapping("/s/{identifier}")
	public void resolveLink(@PathVariable("identifier") String id, HttpServletResponse response) {
		Entry e = repo.findByIdentifier(id) //
				.filter(Entry::isActive) //
				.orElseThrow(() -> new ResourceNotFoundException("link", "identifier", id));

		response.setStatus(HttpStatus.PERMANENT_REDIRECT.value());
		response.setHeader("Location", e.getTarget());
	}

}

@Getter
class CreateRequest {
	private String target;
}

@Getter
class PatchRequest {
	private Boolean active;
}