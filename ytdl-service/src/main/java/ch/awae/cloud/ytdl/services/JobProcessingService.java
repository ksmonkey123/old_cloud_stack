package ch.awae.cloud.ytdl.services;

import java.io.IOException;
import java.sql.Timestamp;
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
	private JobService jobService;

	@Scheduled(fixedRateString = "${ytdl.processor.interval}")
	public void run() {
		cleanupBrokenJobs();
		deleteOldJobs();
		performOneJob();
	}

	private void deleteOldJobs() {
		List<Job> oldJobs = repository
				.findByCreatedBefore(new Timestamp(System.currentTimeMillis() - (24 * 60 * 60 * 1000)));
		for (Job job : oldJobs)
			jobService.deleteJob(job.getId(), user -> true);
	}

	private void performOneJob() {
		Job job;
		String identifier;
		ExportFormat[] formats;
		synchronized (repository) {
			val maybeJob = repository.findFirstByStatusOrderByCreatedAsc(JobStatus.PENDING);
			if (!maybeJob.isPresent())
				return;
			job = maybeJob.get();
			formats = job.getFormats().toArray(new ExportFormat[0]);
			identifier = Long.toString(System.currentTimeMillis());
			job.setStatus(JobStatus.DOWNLOADING);
			job = repository.saveAndFlush(job);
		}
		try {
			System.out.println("starting download");
			String tempFile = downloadService.downloadFile(job.getUrl(), identifier).get();
			job.setStatus(JobStatus.CONVERTING);
			job.setName(tempFile);
			job = repository.save(job);
			for (ExportFormat format : formats) {
				System.out.println("starting conversion to " + format.getName());
				T2<String, Long> fileInfo = converterService.convertFile(tempFile, identifier, format).get();
				OutputFile file = new OutputFile(fileInfo._1, fileInfo._2, job.getUrl(), format,
						UUID.randomUUID().toString(), identifier);
				job.addFile(file);
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
