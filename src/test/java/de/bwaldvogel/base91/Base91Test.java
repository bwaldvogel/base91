package de.bwaldvogel.base91;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.junit.Test;

public class Base91Test {

    private static Charset CHARSET = Charset.forName("ISO-8859-1");
    private static double worstCaseRatio = 1.2308;
    private static double bestCaseRatio = 1.1429;

    @Test
    public void testEncodeDecode() throws Exception {

	Map<String, String> encodeDecodes = new HashMap<String, String>();
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
	    assertEquals(encodedText, new String(encode, CHARSET));
	    assertEquals(plainText, new String(decode, CHARSET));
	}
    }

    @Test
    public void testRandomEncodeDecode() throws Exception {

	Random random = new Random(System.currentTimeMillis());

	int encodedSize = 0;
	int plainSize = 0;

	double worstEncodingRatio = Double.MIN_VALUE;
	double bestEncodingRatio = Double.MAX_VALUE;

	for (int i = 0; i < 10000; i++) {
	    byte[] bytes = new byte[random.nextInt(1000) + 100];
	    random.nextBytes(bytes);
	    byte[] encode = Base91.encode(bytes);
	    byte[] decode = Base91.decode(encode);

	    assertArrayEquals(decode, bytes);

	    plainSize += bytes.length;
	    encodedSize += encode.length;

	    double encodingRatio = (double) encode.length / bytes.length;
	    worstEncodingRatio = Math.max(worstEncodingRatio, encodingRatio);
	    bestEncodingRatio = Math.min(bestEncodingRatio, encodingRatio);
	}

	double encodingRatio = (double) encodedSize / plainSize;
	assertTrue(encodingRatio <= worstCaseRatio);
	assertTrue(encodingRatio >= bestCaseRatio);
	System.out.println("encoding ratio: " + encodingRatio);
	System.out.println("worst encoding ratio: " + worstEncodingRatio);
	System.out.println("best encoding ratio: " + bestEncodingRatio);
    }
}
