package ch.awae.cloud.ytdl.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="ytdl_job_file_assoc")
public class FileJobTableEntryWrapper {
	
	@EmbeddedId
	private FileJobTableEntry entry;
	
}
