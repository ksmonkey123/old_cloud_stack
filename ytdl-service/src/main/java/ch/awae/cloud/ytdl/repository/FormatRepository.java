package ch.awae.cloud.ytdl.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import ch.awae.cloud.ytdl.model.ExportFormat;

public interface FormatRepository extends Repository<ExportFormat, Long> {

	List<ExportFormat> findAll();

}
