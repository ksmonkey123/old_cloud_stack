package ch.awae.cloud.elite;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class DumpStorage {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void save(List<DumpedSystem> systems) {
		for (DumpedSystem system : systems)
			em.persist(new StarSystem(system.getId(), system.getId64(), system.getName(), system.getDate(),
					system.getCoords().getX(), system.getCoords().getY(), system.getCoords().getZ()));
		em.flush();
		em.clear();
	}

}
