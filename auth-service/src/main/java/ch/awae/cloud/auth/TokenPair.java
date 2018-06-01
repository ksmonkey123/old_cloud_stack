package ch.awae.cloud.auth;

import lombok.Data;

@Data
public class TokenPair {
	private final String accessToken;
	private final String refreshToken;
}
