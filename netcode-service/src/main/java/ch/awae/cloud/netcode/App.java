package ch.awae.cloud.netcode;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "ntcd_apps")
public class App {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@Column(nullable = false, updatable = false)
	@JsonIgnore
	private long user;

	@NotBlank
	@Size(min = 32, max = 32)
	@Pattern(regexp = "[a-zA-Z0-9]{32}")
	@Column(unique = true, nullable = false, updatable = false)
	private String identifier;

	@NotBlank
	@Column(nullable = false, updatable = false)
	private String name;

	@Column(name = "cre_dat", nullable = false, updatable = false)
	private Timestamp created;

	public App(long user, String identifier, String name) {
		this.user = user;
		this.identifier = identifier;
		this.name = name;
		this.created = new Timestamp(System.currentTimeMillis());
	}

}

interface AppRepo extends JpaRepository<App, Long> {

	List<App> findByUser(Long user);

	Optional<App> findByIdentifier(String identifier);
	
	Optional<App> findByIdentifierAndUser(String identifier, Long user);
	
	List<App> findByUserOrderByCreatedAsc(Long user);

}
