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

	private String name;

	private long size;

	private String source;
	
	private String identifier;
	
	private String uuid;

	@ManyToOne
	private ExportFormat format;

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
