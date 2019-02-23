package ch.awae.cloud.ytdl.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "ytdl_files")
public class OutputFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	@JsonIgnore
	private Long id;

	@Column(name = "cre_dat", nullable = false, updatable = false)
	private Timestamp created;

	private String name;

	private long size;

	private String source;
	
	@JsonIgnore
	private String identifier;
	
	private String uuid;

	@ManyToOne
	private ExportFormat format;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "job_id")
	@Setter(AccessLevel.PACKAGE)
	@JsonIgnore
	private Job job;
	
	public OutputFile(String name, long size, String source, ExportFormat format, String uuid, String identifier) {
		this.name = name;
		this.size = size;
		this.source = source;
		this.format = format;
		this.identifier = identifier;
		this.uuid = uuid;
		this.created = new Timestamp(System.currentTimeMillis());
	}

}
