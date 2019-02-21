package ch.awae.cloud.ytdl.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import ch.awae.cloud.ytdl.model.FileJobTableEntry;
import ch.awae.cloud.ytdl.model.FileJobTableEntryWrapper;

public interface FileJobTableEntryWrapperRepository extends Repository<FileJobTableEntryWrapper, FileJobTableEntry> {

	List<FileJobTableEntryWrapper> findAll();

}
