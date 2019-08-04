package de.bwaldvogel.base91;

import static org.assertj.core.api.Assertions.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Base91Test {

	private static final Logger log = LoggerFactory.getLogger(Base91Test.class);

	private static final Charset CHARSET = StandardCharsets.UTF_8;
	private static final double WORST_CASE_RATIO = 1.2308;
	private static final double BEST_CASE_RATIO = 1.1429;
	private static final long RANDOM_SEED = 4711;

	@Test
	public void testEncodeDecode() throws Exception {
		Map<String, String> encodeDecodes = new LinkedHashMap<>();
		encodeDecodes.put("test", "fPNKd");
		encodeDecodes.put("Never odd or even\n", "_O^gp@J`7RztjblLA#_1eHA");
		encodeDecodes.put("May a moody baby doom a yam?\n", "8D9Kc)=/2$WzeFui#G9Km+<{VT2u9MZil}[A");
		encodeDecodes.put("", "");
		encodeDecodes.put("a", "GB");

		for (Entry<String, String> entry : encodeDecodes.entrySet()) {
			String plainText = entry.getKey();
			String encodedText = entry.getValue();
			byte[] encode = Base91.encode(plainText.getBytes(CHARSET));
			byte[] decode = Base91.decode(encode);
			assertThat(new String(encode, CHARSET)).isEqualTo(encodedText);
			assertThat(new String(decode, CHARSET)).isEqualTo(plainText);
		}
	}

	@Test
	public void testRandomEncodeDecode() throws Exception {
		Random random = new Random(RANDOM_SEED);

		int encodedSize = 0;
		int plainSize = 0;

		double worstEncodingRatio = Double.MIN_VALUE;
		double bestEncodingRatio = Double.MAX_VALUE;

		for (int i = 0; i < 10000; i++) {
			byte[] bytes = new byte[random.nextInt(1000) + 100];
			random.nextBytes(bytes);
			byte[] encode = Base91.encode(bytes);
			byte[] decode = Base91.decode(encode);

			assertThat(decode).containsExactly(bytes);

			plainSize += bytes.length;
			encodedSize += encode.length;

			double encodingRatio = (double) encode.length / bytes.length;
			worstEncodingRatio = Math.max(worstEncodingRatio, encodingRatio);
			bestEncodingRatio = Math.min(bestEncodingRatio, encodingRatio);
		}

		double encodingRatio = (double) encodedSize / plainSize;
		assertThat(encodingRatio).isBetween(BEST_CASE_RATIO, WORST_CASE_RATIO);
		log.info("encoding ratio: {}", encodingRatio);
		log.info("worst encoding ratio: {}", worstEncodingRatio);
		log.info("best encoding ratio: {}", bestEncodingRatio);
	}
}
