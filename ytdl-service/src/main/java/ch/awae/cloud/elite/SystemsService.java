package ch.awae.cloud.elite;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ch.awae.cloud.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.val;

@Service
public class SystemsService {

	@Autowired
	private RestTemplate http;
	@Autowired
	private StarSystemRepo repo;

	public SystemEntry getByName(String name) {
		EDSMEntry result;
		System.out.println("Looking up system: " + name);
		try {
			result = http.getForObject("https://www.edsm.net/api-v1/system?systemName={name}&showId=1", EDSMEntry.class,
					name);
		} catch (RestClientException e) {
			throw new ResourceNotFoundException("sytem", "name", name);
		}
		System.out.println("received: " + result);
		return repo.findById(result.getId()).map(SystemEntry::new)
				.orElseThrow(() -> new ResourceNotFoundException("system", "id", result.getId()));
	}

	public List<SystemEntry> getNeighbours(SystemEntry entry, double distance) {
		double x = entry.getCoords().getX();
		double y = entry.getCoords().getY();
		double z = entry.getCoords().getZ();

		val result = repo.searchCube(x, y, z, distance).parallelStream().map(SystemEntry::new)
				.filter(sys -> !sys.equals(entry))
				.filter(sys -> sys.getCoords().distance(entry.getCoords()) <= distance).collect(Collectors.toList());
		return result;
	}

}

@ToString
@EqualsAndHashCode(of = "name")
@NoArgsConstructor
@Getter
class SystemEntry {
	private long id;
	private long id64;
	private String name;
	private SystemCoordinates coords;

	SystemEntry(StarSystem system) {
		id = system.getId();
		id64 = system.getId64();
		name = system.getName();
		coords = new SystemCoordinates(system.getX(), system.getY(), system.getZ());
	}

	public double distanceTo(SystemEntry other) {
		return this.coords.distance(other.coords);
	}
}

@Getter
@ToString
class EDSMEntry {
	private long id, id64;
	private String name;
}

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
class SystemCoordinates {
	private double x, y, z;

	public double distance(SystemCoordinates other) {
		double dx = this.x - other.x;
		double dy = this.y - other.y;
		double dz = this.z - other.z;
		return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
	}

}
