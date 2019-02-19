package ch.awae.cloud.elite;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import org.springframework.boot.CommandLineRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.ToString;

public class SystemDumpLoader implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		long start = System.currentTimeMillis();
		ObjectMapper mapper = new ObjectMapper();
		try (BufferedReader reader = new BufferedReader(new FileReader("dump.json"));
				BufferedWriter writer = new BufferedWriter(new FileWriter("load.sql"))) {
			int counter = 0;
			writer.write("use cloud;\n");
			while (reader.ready()) {
				String string = reader.readLine();
				if (string.equals("[") || string.equals("]"))
					continue;
				DumpedSystem system = mapper.readValue(string, DumpedSystem.class);
				writer.write("INSERT INTO `elite_systems` VALUES (" + system.getId() + ",'" + system.getDate() + "',"
						+ system.getId64() + ",'" + system.getName().replaceAll("'", "''") + "'," + system.getCoords().getX() + ","
						+ system.getCoords().getY() + "," + system.getCoords().getZ() + ");");
				counter++;
				if (counter % 100000 == 0) {
					long delta = (System.currentTimeMillis() - start) / 1000;
					System.out.println("### processed " + counter / 1000 + "k systems in " + delta + "s");
				}
			}
			writer.flush();
			System.out.println("DONE");
		}
	}

}

@ToString
@Getter
class DumpedSystem {
	private long id;
	private long id64;
	private String name, date;
	private Coordinates coords;
}

@ToString
@Getter
class Coordinates {
	private double x, y, z;
}
