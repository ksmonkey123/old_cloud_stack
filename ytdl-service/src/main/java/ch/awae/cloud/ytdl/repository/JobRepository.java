package ch.awae.cloud.ytdl.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.awae.cloud.ytdl.model.Job;
import ch.awae.cloud.ytdl.model.JobStatus;

public interface JobRepository extends JpaRepository<Job, Long> {

	List<Job> findByUserOrderByCreatedAsc(long user);

	List<Job> findByStatusIn(List<JobStatus> states);

	Optional<Job> findFirstByStatusOrderByCreatedAsc(JobStatus status);
	
	Optional<Job> findByIdAndUser(long id, long user);
	
	Optional<Job> findByIdAndUserAndStatus(long id, long user, JobStatus status);
	
	List<Job> findByCreatedBefore(Timestamp date);

}