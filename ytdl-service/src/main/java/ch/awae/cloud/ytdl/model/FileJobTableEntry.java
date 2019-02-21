package ch.awae.cloud.ytdl.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;

@Getter
@Embeddable
public class FileJobTableEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "job_id")
	private Long jobId;
	@Column(name = "file_id")
	private Long fileId;

}
