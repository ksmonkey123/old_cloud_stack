package ch.awae.cloud.ytdl.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "ytdl_job_summary")
public class JobSummary {

	@Id
	private Long id;

	@JsonIgnore
	private long user;

	@Enumerated(EnumType.STRING)
	private JobStatus status;

	@Column(name = "cre_dat", nullable = false, updatable = false)
	private Timestamp created;

	private String url;

	private int formats;

	private int files;
	
}
