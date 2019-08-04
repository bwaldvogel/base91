package de.bwaldvogel.base91;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class Base91OutputStreamTest {

    @Test
    public void testWriteByteArrays() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Base91OutputStream base91OutputStream = new Base91OutputStream(byteArrayOutputStream);
        base91OutputStream.write("abcdefg".getBytes(StandardCharsets.UTF_8));
        base91OutputStream.write("higjklmn".getBytes(StandardCharsets.UTF_8));
        base91OutputStream.flush();

        byte[] encoded = byteArrayOutputStream.toByteArray();
        byte[] decoded = Base91.decode(encoded);
        assertThat(new String(decoded, StandardCharsets.UTF_8)).isEqualTo("abcdefghigjklmn");
    }

    @Test
    public void testWriteSingleBytes() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Base91OutputStream base91OutputStream = new Base91OutputStream(byteArrayOutputStream);
        base91OutputStream.write('a');
        base91OutputStream.write('b');
        base91OutputStream.write('c');
        base91OutputStream.write('d');
        base91OutputStream.flush();

        byte[] encoded = byteArrayOutputStream.toByteArray();
        byte[] decoded = Base91.decode(encoded);
        assertThat(new String(decoded, StandardCharsets.UTF_8)).isEqualTo("abcd");
    }

}
