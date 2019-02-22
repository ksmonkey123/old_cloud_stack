package ch.awae.cloud.ytdl.services;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ch.awae.cloud.ytdl.model.ExportFormat;
import ch.awae.cloud.ytdl.model.Job;
import ch.awae.cloud.ytdl.model.JobStatus;
import ch.awae.cloud.ytdl.model.OutputFile;
import ch.awae.cloud.ytdl.repository.JobRepository;
import ch.awae.utils.functional.T2;
import lombok.AllArgsConstructor;
import lombok.val;

@Service
@AllArgsConstructor
public class JobProcessingService {

	private DownloadService downloadService;
	private ConverterService converterService;
	private JobRepository repository;

	@Scheduled(fixedRateString = "${ytdl.processor.interval}")
	public void run() {
		cleanupBrokenJobs();
		performOneJob();
	}

	private void performOneJob() {
		val maybeJob = repository.findFirstByStatusOrderByCreatedAsc(JobStatus.PENDING);
		if (!maybeJob.isPresent())
			return;
		Job job = maybeJob.get();
		ExportFormat[] formats = job.getFormats().toArray(new ExportFormat[0]);
		String identifier = Long.toString(System.currentTimeMillis());
		job.setStatus(JobStatus.DOWNLOADING);
		repository.save(job);
		try {
			System.out.println("starting download");
			String tempFile = downloadService.downloadFile(job.getUrl(), identifier).get();
			job.setStatus(JobStatus.CONVERTING);
			job = repository.save(job);
			for (ExportFormat format : formats) {
				System.out.println("starting conversion to " + format.getName());
				T2<String, Long> fileInfo = converterService.convertFile(tempFile, identifier, format).get();
				OutputFile file = new OutputFile(fileInfo._1, fileInfo._2, job.getUrl(), format, UUID.randomUUID().toString(), identifier);
				job.getFiles().add(file);
				job = repository.save(job);
			}
			job.setStatus(JobStatus.DONE);
			job = repository.save(job);
			System.out.println("completed job");
		} catch (IOException | InterruptedException e) {
			System.out.println("failed job");
			System.err.println(e.toString());
			e.printStackTrace();
			job.setStatus(JobStatus.FAILED);
			job = repository.save(job);
		} finally {
			try {
				System.out.println("deleting temp folder");
				downloadService.deleteTempFolder(identifier);
			} catch (InterruptedException | IOException e1) {
				System.err.println(e1.toString());
				e1.printStackTrace();
			}
		}

	}

	private void cleanupBrokenJobs() {
		List<Job> brokenJobs = repository.findByStatusIn(Arrays.asList(JobStatus.CONVERTING, JobStatus.DOWNLOADING));
		for (Job job : brokenJobs)
			job.setStatus(JobStatus.FAILED);
		repository.saveAll(brokenJobs);
	}

}
