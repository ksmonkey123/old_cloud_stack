package ch.awae.cloud.ytdl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.awae.cloud.ytdl.model.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

	List<Job> findByUserOrderByCreatedAsc(long user);

}