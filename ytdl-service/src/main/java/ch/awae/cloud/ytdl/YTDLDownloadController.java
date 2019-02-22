package ch.awae.cloud.ytdl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ch.awae.cloud.exception.ResourceNotFoundException;
import ch.awae.cloud.ytdl.services.FileStorageService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class YTDLDownloadController {

	private FileStorageService service;

	@GetMapping("/download/{uuid}")
	public ResponseEntity<Resource> downloadFile(@PathVariable("uuid") String uuid, HttpServletRequest request) {
		Resource file = service.getFileByUUID(uuid);
		if (file == null)
			throw new ResourceNotFoundException("file", "uuid", file);

		// Try to determine MIME-type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(file.getFile().getAbsolutePath());
		} catch (IOException e) {
		}
		if (contentType == null)
			contentType = "application/octet-stream";

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

}
