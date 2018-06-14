package ch.awae.cloud.elite;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.awae.cloud.exception.BadRequestException;
import ch.awae.utils.pathfinding.AStarPathfinder;
import ch.awae.utils.pathfinding.GraphDataProvider;
import ch.awae.utils.pathfinding.Pathfinder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

@Service
public class PathfindingService {

	@Autowired
	private SystemsService service;

	public PathfindingResult findPath(String from, String to, double maxJump) {
		long start = System.currentTimeMillis();
		Pathfinder<SystemEntry> pathfinder = new AStarPathfinder<>(new Skywalker(maxJump));

		SystemEntry origin = service.getByName(from);
		SystemEntry target = service.getByName(to);

		val result = pathfinder.findPath(origin, target);
		if (result == null)
			throw new BadRequestException("could not find route from " + origin.getName() + " to " + target.getName());

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

		long time = System.currentTimeMillis() - start;

		return new PathfindingResult(origin.getName(), target.getName(),
				target.getCoords().distance(origin.getCoords()), travelDistance, maxJump, result.size(),
				-Math.floorDiv(-time, 1000), steps);
	}

	@AllArgsConstructor
	private class Skywalker implements GraphDataProvider<SystemEntry> {
		private double maxJump;

		@Override
		public Iterable<SystemEntry> getNeighbours(SystemEntry vertex) {
			return service.getNeighbours(vertex, maxJump);
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
}

@Getter
@AllArgsConstructor
class TravelStep {
	String name;
	double distance;
}