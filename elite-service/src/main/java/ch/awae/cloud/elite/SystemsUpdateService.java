package ch.awae.cloud.elite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.val;

@Service
public class SystemsUpdateService {

	private @Autowired RestTemplate http;
	private @Autowired StarSystemRepo repo;

	private static Logger logger = LoggerFactory.getLogger(SystemsUpdateService.class);
	
	{
		logger.info("intialized systems updater");
	}
	
	@Scheduled(cron = "${systems.updateSchedule}")
	public void run() {
		logger.info("starting systems update");
		val sys = http.getForObject("https://www.edsm.net/dump/systemsWithCoordinates7days.json", DumpedSystem[].class);
		logger.info("need to update " + sys.length + " systems");
		int counter = 0;
		for (val system : sys) {
			val updated = repo.findById(system.getId()).map(s -> patch(s, system)).orElseGet(() -> create(system));
			repo.save(updated);
			counter++;
			if (counter % 1000 == 0) {
				logger.info("updated " + counter + " systems");
			}
		}
		logger.info("systems update complete");
	}

	private StarSystem create(DumpedSystem system) {
		val c = system.getCoords();
		return new StarSystem(system.getId(), system.getId64(), system.getName(), system.getDate(), c.getX(), c.getY(),
				c.getZ());
	}

	private StarSystem patch(StarSystem s, DumpedSystem system) {
		val c = system.getCoords();
		s.setName(system.getName());
		s.setDate(system.getDate());
		s.setX(c.getX());
		s.setY(c.getY());
		s.setZ(c.getZ());
		return s;
	}

}
