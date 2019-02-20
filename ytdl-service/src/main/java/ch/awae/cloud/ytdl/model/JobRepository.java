package ch.awae.cloud.ytdl.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {

	List<Job> findByUserOrderByCreatedAsc(long user);

}