package ch.awae.cloud.elite;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.awae.cloud.exception.ResourceNotFoundException;
import lombok.val;

@Service
public class PathStoreService {

	private final static double ROUNDING = 4.0;

	private @Autowired PathStoreRepo pathRepo;
	private @Autowired StarSystemRepo systemRepo;

	public Optional<PathfindingResult> readFromStore(SystemEntry origin, SystemEntry target, double range) {
		val start = System.currentTimeMillis();
		double maxJump = normalizeDistance(range, false);

		return pathRepo.findByOriginAndTargetAndJumpRange(origin.getName(), target.getName(), maxJump).map(store -> {
			if ("NOPATH".equals(store.getPathString()))
				throw new NoPathException();
			return decode(origin, maxJump, store.getPathString(), start);
		});
	}

	public void storeNil(String from, String to, double range) {
		val maxJmp = normalizeDistance(range, true);
		pathRepo.save(new PathStore(from, to, maxJmp, "NOPATH", 0));
	}

	public void store(List<SystemEntry> route, double length, double range) {
		val origin = route.get(0);
		val target = route.get(route.size() - 1);
		val maxJmp = normalizeDistance(range, true);
		val string = route.stream().skip(1).map(e -> Long.toString(e.getId())).reduce((a, b) -> a + "," + b).get();
		pathRepo.save(new PathStore(origin.getName(), target.getName(), maxJmp, string, length));
	}

	public List<SystemEntry> getRoutes(String origin, double range) {
		System.out.println("reading cached routes from " + origin);
		return pathRepo.findByOriginAndJumpRange(origin, normalizeDistance(range, false)).stream().filter(s -> "NOPATH".equals(s.getPathString()))
				.map(s -> {
					val a = s.getPathString().split(",");
					return Long.parseLong(a[a.length - 1]);
				})
				.map(id -> systemRepo.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("system", "id", id.toString()))) //
				.map(SystemEntry::new).collect(Collectors.toList());
	}

	private PathfindingResult decode(SystemEntry origin, double range, String string, long start) {
		System.out.println("decoding from cache");
		val steps = Stream.of(string.split(",")) //
				.map(Long::parseLong) //
				.map(id -> systemRepo.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("system", "id", id.toString()))) //
				.map(SystemEntry::new).collect(Collectors.toList());

		val target = steps.get(steps.size() - 1);

		int jumps = steps.size();
		double dist = origin.getCoords().distance(target.getCoords());
		double acc = 0;

		val travel = new ArrayList<TravelStep>();

		for (int i = 0; i < steps.size(); i++) {
			val a = i == 0 ? origin : steps.get(i - 1);
			val b = steps.get(i);
			val delta = a.getCoords().distance(b.getCoords());
			acc += delta;
			travel.add(new TravelStep(b.getName(), delta));
		}

		val time = System.currentTimeMillis() - start;

		return new PathfindingResult(origin.getName(), target.getName(), dist, acc, range, jumps,
				-Math.floorDiv(-time, 1000), travel, true);
	}

	private double normalizeDistance(double distance, boolean write) {
		return (write ? Math.ceil(distance * ROUNDING) : Math.floor(distance * ROUNDING)) / ROUNDING;
	}
}

class NoPathException extends RuntimeException {

	private static final long serialVersionUID = 1L;

}
