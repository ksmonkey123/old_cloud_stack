package ch.awae.cloud.ytdl.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<OutputFile, Long> {

	Optional<OutputFile> findBySourceAndFormat(String source, ExportFormat format);
	
}
