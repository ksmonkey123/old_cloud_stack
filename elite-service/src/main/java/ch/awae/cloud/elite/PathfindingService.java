package ch.awae.cloud.elite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.awae.cloud.exception.ResourceNotFoundException;
import ch.awae.utils.pathfinding.AStarPathfinder;
import ch.awae.utils.pathfinding.GraphDataProvider;
import ch.awae.utils.pathfinding.Pathfinder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

@Service
public class PathfindingService {

	private @Autowired SystemsService systemService;
	private @Autowired PathStoreService storeService;

	public PathfindingResult findPath(String from, String to, double maxJump) {
		long start = System.currentTimeMillis();

		SystemEntry origin = systemService.getByName(from);
		SystemEntry target = systemService.getByName(to);

		try {
			return storeService.readFromStore(origin, target, maxJump)
					.orElseGet(() -> doPathfinding(maxJump, start, origin, target));
		} catch (NoPathException npe) {
			throw new ResourceNotFoundException("route", "path", origin.getName() + " \u2192 " + target.getName());
		}
	}

	private PathfindingResult doPathfinding(double maxJump, long start, SystemEntry origin, SystemEntry target) {
		Pathfinder<SystemEntry> pathfinder = new AStarPathfinder<>(new Skywalker(maxJump));
		pathfinder.setTimeout(300000);

		val result = pathfinder.findPath(origin, target);
		if (result == null) {
			try {
				storeService.storeNil(origin.getName(), target.getName(), maxJump);
				storeService.storeNil(target.getName(), origin.getName(), maxJump);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("no path found");
			throw new ResourceNotFoundException("route", "path", origin.getName() + " \u2192 " + target.getName());
		}
		System.out.println("found a route with " + result.size() + " jumps");

		val sorted = new ArrayList<SystemEntry>();
		sorted.add(origin);
		for (int i = result.size() - 1; i >= 0; i--)
			sorted.add(result.get(i));

		double travelDistance = 0;
		val steps = new ArrayList<TravelStep>();

		for (int i = 1; i < sorted.size(); i++) {
			val a = sorted.get(i - 1);
			val b = sorted.get(i);
			val dist = a.getCoords().distance(b.getCoords());
			travelDistance += dist;
			steps.add(new TravelStep(b.getName(), dist));
		}

		try {
			storeService.store(sorted, travelDistance, maxJump);
			val copy = new ArrayList<>(sorted);
			Collections.reverse(copy);
			storeService.store(copy, travelDistance, maxJump);
		} catch (Exception e) {
			e.printStackTrace();
		}

		long time = System.currentTimeMillis() - start;

		return new PathfindingResult(origin.getName(), target.getName(),
				target.getCoords().distance(origin.getCoords()), travelDistance, maxJump, result.size(),
				-Math.floorDiv(-time, 1000), steps, false);
	}

	@AllArgsConstructor
	private class Skywalker implements GraphDataProvider<SystemEntry> {
		private double maxJump;

		@Override
		public Iterable<SystemEntry> getNeighbours(SystemEntry vertex) {
			val list = systemService.getNeighbours(vertex, maxJump);
			// TODO: add known sub-paths from cache
			//storeService.getRoutes(vertex.getName(), maxJump);
			return list;
		}

		@Override
		public double getDistance(SystemEntry from, SystemEntry to) {
			return from.getCoords().distance(to.getCoords());
		}

	}

}

@Getter
@AllArgsConstructor
class PathfindingResult {
	String origin, target;
	double distance, routeLength, maxJumpRange;
	int jumps;
	long searchTime;
	List<TravelStep> steps;
	boolean cached;
}

@Getter
@AllArgsConstructor
class TravelStep {
	String name;
	double distance;
}