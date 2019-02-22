package ch.awae.cloud.ytdl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.awae.cloud.ytdl.model.ExportFormat;
import ch.awae.cloud.ytdl.model.OutputFile;

public interface FileRepository extends JpaRepository<OutputFile, Long> {

	Optional<OutputFile> findBySourceAndFormat(String source, ExportFormat format);

	List<OutputFile> findByIdNotIn(List<Long> ids);
	
	Optional<OutputFile> findByUuid(String uuid);
	
}
