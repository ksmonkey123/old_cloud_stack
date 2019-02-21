package ch.awae.cloud.ytdl.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ch.awae.cloud.ytdl.model.FileJobTableEntryWrapper;
import ch.awae.cloud.ytdl.model.Job;
import ch.awae.cloud.ytdl.model.JobStatus;
import ch.awae.cloud.ytdl.repository.FileJobTableEntryWrapperRepository;
import ch.awae.cloud.ytdl.repository.FileRepository;
import ch.awae.cloud.ytdl.repository.JobRepository;
import lombok.AllArgsConstructor;
import lombok.val;

@Service
@AllArgsConstructor
public class JobProcessingService {

	private DownloadService downloadService;
	private ConverterService converterService;
	private FileJobTableEntryWrapperRepository linksRepo;
	private FileRepository fileRepo;
	private JobRepository repository;

	@Scheduled(fixedRateString = "${ytdl.processor.interval}")
	public void run() {
		cleanupBrokenJobs();
		cleanupOrphanedFiles();
		performOneJob();
	}

	private void performOneJob() {
		val maybeJob = repository.findOneByStatusOrderByCreatedAsc(JobStatus.PENDING);
		if (!maybeJob.isPresent())
			return;
		Job job = maybeJob.get();
		// ======== PROCESS ONE JOB ========
		// TODO: implement
	}

	private void cleanupBrokenJobs() {
		List<Job> brokenJobs = repository.findByStatusIn(Arrays.asList(JobStatus.CONVERTING, JobStatus.DOWNLOADING));
		for (Job job : brokenJobs)
			job.setStatus(JobStatus.FAILED);
		repository.saveAll(brokenJobs);
	}

	private void cleanupOrphanedFiles() {
		val links = linksRepo.findAll();
		List<Long> ids = new ArrayList<>();
		for (FileJobTableEntryWrapper link : links)
			ids.add(link.getEntry().getFileId());
		fileRepo.deleteAll(fileRepo.findByIdNotIn(ids));
	}

}
