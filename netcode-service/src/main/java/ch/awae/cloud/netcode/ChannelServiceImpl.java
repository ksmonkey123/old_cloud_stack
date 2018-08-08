package ch.awae.cloud.netcode;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class ChannelServiceImpl implements ChannelService {

	private final ConcurrentHashMap<String, Channel> channels = new ConcurrentHashMap<>();

	@Override
	public Channel createChannel(String id) {
		Channel channel = new Channel(this, id);
		Channel old = channels.putIfAbsent(id, channel);
		if (old != null) {
			throw new IllegalArgumentException("channel " + id + " already exists");
		}
		return channel;
	}

	@Override
	public Optional<Channel> getChannel(String id) {
		return Optional.ofNullable(channels.get(id));
	}

	@Override
	public void closeChannel(String id) {
		channels.remove(id).close();
	}

}
