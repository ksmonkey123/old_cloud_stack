package ch.awae.cloud.ytdl.services;

import java.io.IOException;
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
import ch.awae.utils.functional.T2;

@Service
public class FileStorageService {

	@Value("${ytdl.filesystem.out}")
	private String outFile;

	private @Autowired FileRepository repo;
	private @Autowired ExecService exec;

	public T2<Resource, Long> getFileByUUID(String uuid) {
		OutputFile file = repo.findByUuid(uuid).orElseThrow(() -> new ResourceNotFoundException("file", "uuid", uuid));
		try {
			Path filePath = Paths.get(outFile + "/" + file.getIdentifier() + "/" + file.getName());
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists())
				return T2.of(resource, file.getSize());
			else
				throw new ResourceNotFoundException("file", "uuid", uuid);
		} catch (Exception e) {
			throw new ResourceNotFoundException("file", "uuid", uuid);
		}
	}

	public void deleteFilesByIdentifier(String identifier) throws InterruptedException, IOException {
		exec.exec("rm", "-rv", outFile + "/" + identifier);
	}

}
