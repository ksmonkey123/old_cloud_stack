package ch.awae.cloud.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Convert;
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

	@Convert(converter = StringListConverter.class)
	private List<String> roles;

	public User(String username, String password, boolean isAdmin) {
		this.username = username;
		this.password = password;
		roles = new ArrayList<>();
		roles.add("ROLE_USER");
		if (isAdmin)
			roles.add("ROLE_ADMIN");
	}

}

interface UserRepo extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

}
