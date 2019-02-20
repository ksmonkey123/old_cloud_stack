package ch.awae.cloud.ytdl;

import java.util.List;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.awae.cloud.security.Security;
import ch.awae.cloud.ytdl.model.Job;
import ch.awae.cloud.ytdl.model.JobRepository;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class YTDLApiController {

	private JobRepository jobRepo;
	
	@Secured("ROLE_YTDL")
	@GetMapping("/list")
	public List<Job> getJobs() {
		long userId = Security.getUserId();
		return jobRepo.findByUserOrderByCreatedAsc(userId);
	}
	
}
