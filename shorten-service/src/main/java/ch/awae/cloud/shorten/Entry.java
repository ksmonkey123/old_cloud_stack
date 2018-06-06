package ch.awae.cloud.shorten;

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
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "shrt_entries")
public class Entry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@Column(nullable = false, updatable = false)
	@JsonIgnore
	private long user;

	@NotBlank
	@Size(min = 6, max = 6)
	@Pattern(regexp = "[a-zA-Z0-9]{6}")
	@Column(unique = true, nullable = false, updatable = false)
	private String identifier;

	@NotBlank
	@Column(nullable = false, updatable = false)
	private String target;

	@Setter
	@Column(nullable = false)
	private boolean active;

	@Column(name = "cre_dat", nullable = false, updatable = false)
	private Timestamp created;

	public Entry(long user, String identifier, String target) {
		this.user = user;
		this.identifier = identifier;
		this.target = target;
		this.active = true;
		this.created = new Timestamp(System.currentTimeMillis());
	}

}

interface EntryRepo extends JpaRepository<Entry, Long> {

	List<Entry> findByUser(Long user);

	Optional<Entry> findByIdentifier(String identifier);
	
	List<Entry> findByUserOrderByCreatedDesc(Long user);

}
