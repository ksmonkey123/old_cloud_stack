package ch.awae.cloud.ytdl.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ytdl_files")
public class OutputFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	private Long id;

	@Column(name = "cre_dat", nullable = false, updatable = false)
	private Timestamp created;

	private String path;

	private long size;

	private String source;

	@ManyToOne
	private ExportFormat format;

	public OutputFile(String path, long size, String source, ExportFormat format) {
		this.path = path;
		this.size = size;
		this.source = source;
		this.format = format;
		this.created = new Timestamp(System.currentTimeMillis());
	}

}
