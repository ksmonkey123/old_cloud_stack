package ch.awae.cloud.ytdl.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import ch.awae.cloud.ytdl.model.ExportFormat;
import ch.awae.cloud.ytdl.model.FileCategory;

public interface FormatRepository extends Repository<ExportFormat, Long> {

	List<ExportFormat> findAll();
	
	List<ExportFormat> findByCategory(FileCategory category);
	
	List<ExportFormat> findAllById(Iterable<Long> ids);
	
}
