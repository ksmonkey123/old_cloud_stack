package ch.awae.cloud.auth;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

	@ManyToOne
	private User user;

	private Timestamp cre_dat;

	private Timestamp exp_dat;

	private String token;

	public Token(User user, Timestamp cre_dat, Timestamp exp_dat, String token) {
		this.user = user;
		this.cre_dat = cre_dat;
		this.exp_dat = exp_dat;
		this.token = token;
	}

}
