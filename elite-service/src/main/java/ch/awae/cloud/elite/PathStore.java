package ch.awae.cloud.elite;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.springframework.data.jpa.repository.JpaRepository;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "elite_path_store", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "origin", "target", "jumpRange" }) })
public class PathStore {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private @Id Long id;

	@Size(max = 100)
	private @Column(nullable = false) String origin;
	@Size(max = 100)
	private @Column(nullable = false) String target;
	private @Column(nullable = false) Double jumpRange;
	
	private Double pathLength;

	@Lob
	@Column
	private String pathString;

	private @Column(name = "cre_dat") Timestamp created;

	public PathStore(String origin, String target, double range, String string, double pathLength) {
		this.origin = origin;
		this.target = target;
		this.jumpRange = range;
		this.pathString = string;
		this.created = new Timestamp(System.currentTimeMillis());
		this.pathLength = pathLength;
	}

}

interface PathStoreRepo extends JpaRepository<PathStore, Long> {

	Optional<PathStore> findByOriginAndTargetAndJumpRange(String origin, String target, double range);

	List<PathStore> findByCreatedIsBefore(Timestamp time);
	
	List<PathStore> findByOriginAndJumpRange(String origin, double range);

}
