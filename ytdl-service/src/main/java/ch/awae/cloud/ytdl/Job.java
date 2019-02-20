package ch.awae.cloud.ytdl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.repository.JpaRepository;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.val;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ytdl_jobs")
public class Job {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	private Long id;

	@Column(nullable = false, updatable = false)
	private long user;

	@Enumerated(EnumType.STRING)
	private JobStatus status;

	@Column(name = "cre_dat", nullable = false, updatable = false)
	private Timestamp created;

	private String url;

	@ManyToOne
	private OutputFile file;

	@Enumerated(EnumType.STRING)
	private ExportFormat format;

	public Job(long user, String url, ExportFormat format) {
		this.user = user;
		this.status = JobStatus.PENDING;
		this.format = format;
		this.created = new Timestamp(System.currentTimeMillis());
		this.url = url;
	}
}

interface JobRepository extends JpaRepository<Job, Long> {

	List<Job> findByUserOrderByCreatedAsc(long user);

	List<Job> findByUrlAndStatus(String url, JobStatus status);

	default Optional<Job> findCompleted(String url) {
		val list = findByUrlAndStatus(url, JobStatus.DONE);
		if (list.isEmpty())
			return Optional.empty();
		return Optional.of(list.get(0));
	}

}
