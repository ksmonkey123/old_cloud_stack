package ch.awae.cloud.auth;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import ch.awae.cloud.exception.BadRequestException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;

@Service
public class TokenService {

	@Value("${token.access.secret}")
	private String accSecret;

	@Value("${token.access.expiration}")
	private Long accExpiration;

	@Value("${token.refresh.secret}")
	private String refSecret;

	@Value("${token.refresh.expiration}")
	private Long refExpiration;

	@Autowired
	private TokenRepo repo;

	private static Logger logger = LoggerFactory.getLogger(TokenService.class);

	private RawToken generateToken(User user, String secret, Long expiration) {
		System.out.println(user);
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + expiration);
		val claims = new HashMap<String, Object>();

		claims.put("usr", user.getUsername());
		claims.put("aut", String.join(",", user.getRoles()));

		String token = Jwts.builder() //
				.setSubject(Long.toString(user.getId())) //
				.setIssuedAt(new Date()) //
				.setExpiration(expiryDate) //
				.addClaims(claims) //
				.signWith(SignatureAlgorithm.HS512, secret) //
				.compact();
		return new RawToken(token, new Timestamp(now.getTime()), new Timestamp(expiryDate.getTime()));
	}

	public TokenPair generateTokenPair(User user) {
		val accessToken = generateToken(user, accSecret, accExpiration);
		val refreshToken = generateToken(user, refSecret, refExpiration);

		repo.save(new Token(user, refreshToken.getFrom(), refreshToken.getTo(), refreshToken.getToken()));

		return new TokenPair(accessToken.getToken(), refreshToken.getToken());
	}

	public void remove(String refreshToken) {
		if (StringUtils.hasText(refreshToken) && refreshToken.startsWith("Bearer "))
			refreshToken = refreshToken.substring(7, refreshToken.length());
		else
			throw new BadRequestException("invalid token");

		val token = repo.findByToken(refreshToken).orElseThrow(() -> new BadRequestException("invalid token"));
		repo.delete(token);
	}

	public TokenPair replace(String refreshToken) {
		if (StringUtils.hasText(refreshToken) && refreshToken.startsWith("Bearer "))
			refreshToken = refreshToken.substring(7, refreshToken.length());
		else
			throw new BadRequestException("invalid token");

		val token = repo.findByToken(refreshToken).orElseThrow(() -> new BadRequestException("invalid token"));
		repo.delete(token);
		return generateTokenPair(token.getUser());
	}

	@Scheduled(fixedRateString = "${token.evictionRate}")
	public void cleanTable() {
		val now = new Timestamp(System.currentTimeMillis());
		val tokens = repo.findByExpiresLessThan(now);
		repo.deleteAll(tokens);
		logger.info("deleted " + tokens.size() + " tokens");
	}

}

@AllArgsConstructor
@Data
class RawToken {
	private final String token;
	private final Timestamp from, to;
}
