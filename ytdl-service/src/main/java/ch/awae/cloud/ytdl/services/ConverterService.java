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

@Service
public class ConverterService {

	private String outFile;

	public ConverterService(@Value("${ytdl.filesystem.out}") String outFile) {
		this.outFile = outFile;
	}

	public Optional<String> convertFile(String file, String identifier, ExportFormat format)
			throws InterruptedException, IOException {
		new File(outFile + "/" + identifier + "/").mkdirs();

		if (format.isRaw())
			runCP(file, identifier);
		else
			runFFMPEG(file, identifier, format);

		try (Stream<Path> paths = Files.walk(Paths.get(outFile + "/" + identifier))) {
			return paths.filter(Files::isRegularFile)
					.filter(f -> f.getFileName().toString().endsWith(format.getSuffix())).findFirst()
					.map(Object::toString);
		}
	}

	private void runCP(String file, String identifier) throws InterruptedException, IOException {
		String[] command = { "cp", file, outFile + "/" + identifier + "/" };
		Runtime.getRuntime().exec(command).waitFor();
	}

	private void runFFMPEG(String file, String identifier, ExportFormat format)
			throws InterruptedException, IOException {
		System.out.println("ffmpeg");
		String[] fileParts = file.split("/");
		String filename = fileParts[fileParts.length - 1];
		String[] command = { "ffmpeg", "-y", "-i", file, outFile + "/" + identifier + "/" + filename + format.getSuffix() };
		Runtime.getRuntime().exec(command).waitFor();
	}

}
