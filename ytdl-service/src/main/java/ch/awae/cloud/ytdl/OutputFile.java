package ch.awae.cloud.ytdl;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	
	public OutputFile(String path, long size) {
		this.path = path;
		this.size = size;
		this.created = new Timestamp(System.currentTimeMillis());
	}

}

