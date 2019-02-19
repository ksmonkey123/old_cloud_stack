package ch.awae.cloud.elite;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.repository.JpaRepository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "elite_systems")
public class StarSystem {

	@Setter(value = AccessLevel.NONE)
	private @Id Long id;
	private Long id64;
	private String name;
	private String date;
	private Double x, y, z;

}

interface StarSystemRepo extends JpaRepository<StarSystem, Long> {

	List<StarSystem> findByXGreaterThanEqualAndXLessThanEqualAndYGreaterThanEqualAndYLessThanEqualAndZGreaterThanEqualAndZLessThanEqual(
			double minX, double maxX, double minY, double maxY, double minZ, double maxZ);

	default List<StarSystem> searchCube(double x, double y, double z, double delta) {
		return findByXGreaterThanEqualAndXLessThanEqualAndYGreaterThanEqualAndYLessThanEqualAndZGreaterThanEqualAndZLessThanEqual(
				x - delta, x + delta, y - delta, y + delta, z - delta, z + delta);
	}

}