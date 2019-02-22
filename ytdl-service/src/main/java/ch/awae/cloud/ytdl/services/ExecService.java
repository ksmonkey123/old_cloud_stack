package ch.awae.cloud.ytdl.services;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

import org.springframework.stereotype.Service;

@Service
public class ExecService {

	public int exec(String... cmd) throws InterruptedException, IOException {
		System.out.println("running command: " + String.join(" ", cmd));
		int exitCode = new ProcessBuilder().command(cmd).redirectError(Redirect.INHERIT).redirectInput(Redirect.INHERIT)
				.start().waitFor();
		System.out.println("process exited with exit code " + exitCode);
		return exitCode;
	}

}
