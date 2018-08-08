package ch.awae.cloud.netcode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ch.awae.cloud.exception.ResourceNotFoundException;
import lombok.Getter;

@RestController
public class NetcodeController {

	private @Autowired AppRepo repo;
	private @Autowired ChannelService service;

	@PostMapping("create")
	public SseEmitter createChannel(@Valid @RequestBody JoinRequest request) {
		String appId = request.getApp();
		repo.findByIdentifier(appId).orElseThrow(() -> new ResourceNotFoundException("app", "identifier", appId));
		Channel c = service.createChannel(request.getChannelId());
		return c.join(request.getUser());
	}

	@PostMapping("join")
	public SseEmitter joinChannel(@Valid @RequestBody JoinRequest request) {
		Channel c = service.getChannel(request.getChannelId())
				.orElseThrow(() -> new ResourceNotFoundException("channel", "id", request.getChannelId()));
		return c.join(request.getUser());
	}

	@PostMapping("send")
	public void sendToChannel(@Valid @RequestBody SendRequest request) {
		Channel c = service.getChannel(request.getChannelId())
				.orElseThrow(() -> new ResourceNotFoundException("channel", "id", request.getChannelId()));
		c.sendToAll(request.getMsg());
	}

	@PostMapping("private")
	public void sendPrivatly(@Valid @RequestBody PrivateSendRequest request) {
		Channel c = service.getChannel(request.getChannelId())
				.orElseThrow(() -> new ResourceNotFoundException("channel", "id", request.getChannelId()));
		c.sendTo(request.getRecipient(), request.getMsg());
	}

}

@Getter
class JoinRequest {
	private @NotBlank String app;
	private @NotBlank String id;
	private @NotBlank String user;

	String getChannelId() {
		return app + "/" + id;
	}
}

@Getter
class SendRequest {
	private @NotBlank String app;
	private @NotBlank String id;
	private @NotNull Message msg;

	String getChannelId() {
		return app + "/" + id;
	}
}

@Getter
class PrivateSendRequest {
	private @NotBlank String app;
	private @NotBlank String id;
	private @NotBlank String recipient;
	private @NotNull Message msg;

	String getChannelId() {
		return app + "/" + id;
	}
}