package ch.awae.cloud.ytdl.services;

import java.io.IOException;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import ch.awae.cloud.exception.BadRequestException;
import ch.awae.cloud.exception.ResourceNotFoundException;
import ch.awae.cloud.ytdl.model.Job;
import ch.awae.cloud.ytdl.model.JobStatus;
import ch.awae.cloud.ytdl.model.OutputFile;
import ch.awae.cloud.ytdl.repository.JobRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JobService {

	private JobRepository jobRepo;
	private FileStorageService storageService;

	public void deleteJob(long jobId, Predicate<Long> verifyUserId) {
		Job job;
		synchronized (jobRepo) {
			job = jobRepo.findById(jobId).filter(j -> verifyUserId.test(j.getUser()))
					.orElseThrow(() -> new ResourceNotFoundException("job", "id", jobId));
			if (job.getStatus() == JobStatus.DOWNLOADING || job.getStatus() == JobStatus.CONVERTING)
				throw new BadRequestException("cannot delete job that is currently being processed!");
			// DELETE FILES IF APPLICABLE
			for (OutputFile f : job.getFiles()) {
				try {
					storageService.deleteFilesByIdentifier(f.getIdentifier());
				} catch (InterruptedException | IOException e) {
					System.err.println(e.toString());
					e.printStackTrace();
				}
			}
			// DELETE JOB
			jobRepo.delete(job);
		}
	}

}
