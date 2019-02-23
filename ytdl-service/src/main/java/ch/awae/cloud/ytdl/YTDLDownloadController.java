package ch.awae.cloud.ytdl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ch.awae.cloud.exception.ResourceNotFoundException;
import ch.awae.cloud.ytdl.services.FileStorageService;
import ch.awae.utils.functional.T2;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class YTDLDownloadController {

	private FileStorageService service;

	@GetMapping("/download/{uuid}")
	public ResponseEntity<Resource> downloadFile(@PathVariable("uuid") String uuid, HttpServletRequest request)
			throws UnsupportedEncodingException {
		T2<Resource, Long> file = service.getFileByUUID(uuid);
		if (file == null)
			throw new ResourceNotFoundException("file", "uuid", uuid);

		// Try to determine MIME-type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(file._1.getFile().getAbsolutePath());
		} catch (IOException e) {
		}
		if (contentType == null)
			contentType = "application/octet-stream";

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + URLDecoder.decode(file._1.getFilename(), "UTF-8") + "\"")
				.header(HttpHeaders.CONTENT_LENGTH, file._2.toString())
				.body(file._1);
	}

}
