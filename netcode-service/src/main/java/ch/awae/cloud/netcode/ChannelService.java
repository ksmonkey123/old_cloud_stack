package ch.awae.cloud.netcode;

import java.util.Optional;

public interface ChannelService {

	Channel createChannel(String id);

	Optional<Channel> getChannel(String id);

	void closeChannel(String id);

}
