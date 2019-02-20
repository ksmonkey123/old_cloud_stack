package ch.awae.cloud.ytdl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ch.awae.cloud.security.Security;
import ch.awae.cloud.ytdl.model.ExportFormat;
import ch.awae.cloud.ytdl.model.FileCategory;
import ch.awae.cloud.ytdl.model.Job;
import ch.awae.cloud.ytdl.model.JobSummary;
import ch.awae.cloud.ytdl.repository.FormatRepository;
import ch.awae.cloud.ytdl.repository.JobRepository;
import ch.awae.cloud.ytdl.repository.JobSummaryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;

@RestController
@AllArgsConstructor
public class YTDLApiController {

	private JobRepository jobRepo;
	private JobSummaryRepository jobSummaryRepo;
	private FormatRepository formatRepo;

	@Secured("ROLE_YTDL")
	@GetMapping("/list")
	public List<JobSummary> getJobs() {
		return jobSummaryRepo.findByUser(Security.getUserId());
	}

	@Secured("ROLE_YTDL")
	@GetMapping("/formats")
	public List<CategorizedExportFormat> getFormats() {
		List<CategorizedExportFormat> list = new ArrayList<>();
		for (FileCategory fc : FileCategory.values()) {
			list.add(new CategorizedExportFormat(fc, formatRepo.findByCategory(fc)));
		}
		return list;
	}
	
	@Secured("ROLE_YTDL")
	@PostMapping("/job")
	public void postJob(@Valid @RequestBody PostJobRequest request) {
		val formats = formatRepo.findAllById(Arrays.asList(request.getFormats()));
		val formatArray = formats.toArray(new ExportFormat[0]);
		
		jobRepo.save(new Job(Security.getUserId(), request.getUrl(), formatArray));
	}
	
}

@Data
class CategorizedExportFormat {
	private final FileCategory category;
	private final List<ExportFormat> formats;
}

@Data
class PostJobRequest {
	private @NotBlank String url;
	private @NotEmpty Long[] formats;
}