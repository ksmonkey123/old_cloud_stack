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

	private String tempFile;

	public DownloadService(@Value("${ytdl.filesystem.temp}") String tempFile) {
		this.tempFile = tempFile;
	}

	public Optional<String> downloadFile(String url, String identifier) throws IOException, InterruptedException {
		String[] command = { "youtube-dl", "-o", tempFile + "/" + identifier + "/%(title)s.%(ext)s", url };
		Runtime.getRuntime().exec(command).waitFor();
		try (Stream<Path> paths = Files.walk(Paths.get(tempFile + "/" + identifier))) {
			return paths.filter(Files::isRegularFile).findFirst().map(Object::toString);
		}
	}

	public void deleteTempFile(String identifier) throws InterruptedException, IOException {
		String[] command = { "rm", "-r", tempFile + "/" + identifier };
		Runtime.getRuntime().exec(command).waitFor();
	}

}
