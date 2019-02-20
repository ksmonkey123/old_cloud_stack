package ch.awae.cloud.ytdl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.awae.cloud.ytdl.model.ExportFormat;
import ch.awae.cloud.ytdl.model.OutputFile;

public interface FileRepository extends JpaRepository<OutputFile, Long> {

	Optional<OutputFile> findBySourceAndFormat(String source, ExportFormat format);
	
}
