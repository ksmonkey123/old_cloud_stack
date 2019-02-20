package ch.awae.cloud.ytdl.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import ch.awae.cloud.ytdl.model.JobSummary;

public interface JobSummaryRepository extends Repository<JobSummary, Long> {

	List<JobSummary> findAll();
	
	List<JobSummary> findByUser(long userId);
	
}
