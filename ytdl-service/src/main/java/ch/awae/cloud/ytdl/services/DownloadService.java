package ch.awae.cloud.ytdl.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DownloadService {

	private String tempFile, binPath;
	private ExecService exec;

	public DownloadService(ExecService exec, @Value("${ytdl.filesystem.temp}") String tempFile,
			@Value("${ytdl.youtube-dl}") String binPath) {
		this.tempFile = tempFile;
		this.binPath = binPath;
		this.exec = exec;
	}

	public Optional<String> downloadFile(String url, String identifier) throws IOException, InterruptedException {
		exec.exec(binPath, "-o", tempFile + "/" + identifier + "/%(title)s.%(ext)s", url);
		try (Stream<Path> paths = Files.walk(Paths.get(tempFile + "/" + identifier))) {
			return paths.filter(Files::isRegularFile).findFirst().map(Object::toString);
		}
	}

	public void deleteTempFolder(String identifier) throws InterruptedException, IOException {
		exec.exec("rm", "-r", tempFile + "/" + identifier);
	}

}
