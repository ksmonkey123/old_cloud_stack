package ch.awae.cloud.ytdl.services;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import ch.awae.cloud.exception.ResourceNotFoundException;
import ch.awae.cloud.ytdl.model.OutputFile;
import ch.awae.cloud.ytdl.repository.FileRepository;

@Service
public class FileStorageService {

	@Value("${ytdl.filesystem.out}")
	private String outFile;

	@Autowired
	private FileRepository repo;

	public Resource getFileByUUID(String uuid) {
		OutputFile file = repo.findByUuid(uuid).orElseThrow(() -> new ResourceNotFoundException("file", "uuid", uuid));
		try {
			Path filePath = Paths.get(outFile + "/" + file.getIdentifier() + "/" + file.getName());
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists())
				return resource;
			else
				throw new ResourceNotFoundException("file", "uuid", uuid);
		} catch (Exception e) {
			throw new ResourceNotFoundException("file", "uuid", uuid);
		}
	}

}
