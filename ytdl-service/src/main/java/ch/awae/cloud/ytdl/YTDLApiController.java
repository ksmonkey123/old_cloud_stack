package ch.awae.cloud.ytdl;

import java.util.List;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.awae.cloud.security.Security;
import ch.awae.cloud.ytdl.model.ExportFormat;
import ch.awae.cloud.ytdl.model.Job;
import ch.awae.cloud.ytdl.model.JobSummary;
import ch.awae.cloud.ytdl.repository.FormatRepository;
import ch.awae.cloud.ytdl.repository.JobRepository;
import ch.awae.cloud.ytdl.repository.JobSummaryRepository;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class YTDLApiController {

	private JobSummaryRepository jobRepo;
	private FormatRepository formatRepo;

	@Secured("ROLE_YTDL")
	@GetMapping("/list")
	public List<JobSummary> getJobs() {
		return jobRepo.findByUser(Security.getUserId());
	}

	@Secured("ROLE_YTDL")
	@GetMapping("/formats")
	public List<ExportFormat> getFormats() {
		return formatRepo.findAll();
	}

}
