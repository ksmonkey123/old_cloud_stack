package ch.awae.cloud.auth;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.repository.JpaRepository;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "auth_tokens")
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@Column(name = "cre_dat")
	private Timestamp creation;

	@Column(name = "exp_dat")
	private Timestamp expires;

	private String token;

	public Token(User user, Timestamp cre_dat, Timestamp exp_dat, String token) {
		this.user = user;
		this.creation = cre_dat;
		this.expires = exp_dat;
		this.token = token;
	}

}

interface TokenRepo extends JpaRepository<Token, Long> {

	List<Token> findByExpiresLessThan(Timestamp time);

	Optional<Token> findByToken(String token);

}