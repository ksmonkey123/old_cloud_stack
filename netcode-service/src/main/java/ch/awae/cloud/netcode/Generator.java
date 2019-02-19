package ch.awae.cloud.netcode;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

@Service
public class Generator {

	private SecureRandom random = new SecureRandom();

	public String generateSequence() {
		while (true) {
			// 24 bytes yield 32 chars
			byte bytes[] = new byte[24];
			random.nextBytes(bytes);
			Encoder encoder = Base64.getUrlEncoder().withoutPadding();
			String candidate = encoder.encodeToString(bytes);
			if (candidate.contains("_") || candidate.contains("-"))
				continue;
			return candidate.substring(0, 32);
		}
	}

	public String generateSequence(Predicate<String> test) {
		String sequence;
		do
			sequence = generateSequence();
		while (!test.test(sequence));
		return sequence;
	}

}
