package ch.awae.cloud.ytdl.model;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "ytdl_job_files", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "file_id"))
	private List<OutputFile> files;

	@ElementCollection(targetClass = ExportFormat.class)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "ytdl_job_formats")
	@Column(name = "format")
	private List<ExportFormat> formats;

	public Job(long user, String url, ExportFormat... format) {
		this.user = user;
		this.status = JobStatus.PENDING;
		this.formats = Arrays.asList(format);
		this.created = new Timestamp(System.currentTimeMillis());
		this.url = url;
	}
}
