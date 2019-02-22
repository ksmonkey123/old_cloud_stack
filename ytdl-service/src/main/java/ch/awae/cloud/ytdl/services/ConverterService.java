package ch.awae.cloud.ytdl.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ch.awae.cloud.ytdl.model.ExportFormat;
import ch.awae.utils.functional.T2;

@Service
public class ConverterService {

	private String outFile;
	private String binPath;
	private ExecService exec;

	public ConverterService(ExecService exec, @Value("${ytdl.filesystem.out}") String outFile,
			@Value("${ytdl.ffmpeg}") String binPath) {
		this.outFile = outFile;
		this.binPath = binPath;
		this.exec = exec;
	}

	public Optional<T2<String, Long>> convertFile(String file, String identifier, ExportFormat format)
			throws InterruptedException, IOException {
		new File(outFile + "/" + identifier + "/").mkdirs();

		if (format.isRaw())
			runCP(file, identifier);
		else
			runFFMPEG(file, identifier, format);

		try (Stream<Path> paths = Files.walk(Paths.get(outFile + "/" + identifier))) {
			Optional<Path> path = paths.filter(Files::isRegularFile)
					.filter(f -> f.getFileName().toString().endsWith(format.getSuffix())).findFirst();
			return path.map(p -> T2.of(p.getFileName().toString(), p.toFile().length()));
		}
	}

	private void runCP(String file, String identifier) throws InterruptedException, IOException {
		exec.exec("cp", file, outFile + "/" + identifier + "/");
	}

	private void runFFMPEG(String file, String identifier, ExportFormat format)
			throws InterruptedException, IOException {
		System.out.println("ffmpeg");
		String[] fileParts = file.split("/");
		String filename = fileParts[fileParts.length - 1];
		exec.exec(binPath, "-y", "-i", file, outFile + "/" + identifier + "/" + filename + format.getSuffix());
	}

}
