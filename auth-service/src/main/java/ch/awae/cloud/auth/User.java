package ch.awae.cloud.auth;

import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
@Entity
@Table(name = "auth_users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	private Long id;

	private String username;

	@JsonIgnore
	private String password;

	private boolean admin;

	public User(String username, String password, boolean isAdmin) {
		this.username = username;
		this.password = password;
		this.admin = isAdmin;
	}

}

interface UserRepo extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

}
