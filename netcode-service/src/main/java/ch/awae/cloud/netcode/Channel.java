package ch.awae.cloud.netcode;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.val;

public class Channel {

	private final ConcurrentHashMap<String, SseEmitter> members = new ConcurrentHashMap<>();
	private final AtomicBoolean open = new AtomicBoolean(true);

	private final ChannelService owner;
	private final String id;

	public Channel(ChannelService owner, String id) {
		this.owner = owner;
		this.id = id;
	}

	public SseEmitter join(String user) {
		Objects.requireNonNull(user);

		val emitter = new SseEmitter();
		val old = members.putIfAbsent(user, emitter);
		if (old != null) {
			emitter.complete();
			throw new IllegalArgumentException("user " + user + " is already in this channel");
		}
		emitter.onCompletion(() -> remove(user));
		return emitter;
	}

	private void remove(String user) {
		members.remove(user);
		if (members.isEmpty() && open.compareAndSet(true, false)) {
			owner.closeChannel(id);
		}
	}

	public void close() {
		open.set(false);
		members.forEachValue(10, e -> e.complete());
	}

	public void sendToAll(Message msg) {
		members.forEachValue(10, e -> send(e, msg));
	}

	public void sendTo(String user, Message msg) {
		val emitter = members.get(user);
		if (emitter == null)
			throw new IllegalArgumentException("user " + user + " is not in the channel");
		send(emitter, msg);
	}

	private void send(SseEmitter emitter, Message msg) {
		try {
			emitter.send(msg);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
