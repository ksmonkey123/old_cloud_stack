package ch.awae.cloud.auth;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

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

	private RawToken generateToken(User user, String secret, Long expiration) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + expiration);
		val claims = new HashMap<String, Object>();

		claims.put("usr", user.getUsername());
		claims.put("aut", user.isAdmin() ? "ROLE_USER,ROLE_ADMIN" : "ROLE_USER");

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

}

@AllArgsConstructor
@Data
class RawToken {
	private final String token;
	private final Timestamp from, to;
}

interface TokenRepo extends JpaRepository<Token, Long> {

}