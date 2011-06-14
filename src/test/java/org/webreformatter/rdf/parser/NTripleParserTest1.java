/**
 * 
 */
package org.webreformatter.rdf.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import junit.framework.TestCase;

/**
 * @author kotelnikov
 */
public class NTripleParserTest1 extends TestCase {

    /**
     * @param name
     */
    public NTripleParserTest1(String name) {
        super(name);
    }

    private String doParseSerialize(String str) {
        NTriplesSerializer serializer = new NTriplesSerializer();
        NTriplesParser parser = new NTriplesParser(serializer);
        parser.parse(str);
        String test = serializer.toString();
        return test;
    }

    private String normalize(String str) {
        str = str.trim();
        str = str.replaceAll("[\\r\\n]+", "\n");
        return str;
    }

    private String read(String string) throws IOException {
        Class<? extends NTripleParserTest1> cls = getClass();
        String packageName = cls.getPackage().getName();
        String path = "/" + packageName.replace('.', '/') + "/" + string;
        InputStream input = cls.getResourceAsStream(path);
        try {
            byte[] buf = new byte[1024 * 10];
            OutputStream out = new ByteArrayOutputStream();
            int len;
            while ((len = input.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            return new String(buf, "UTF-8");
        } finally {
            input.close();
        }
    }

    public void testParseSerialize() throws IOException {
        String control = read("one-result.nt");
        control = normalize(control);

        String str = read("one-test.nt");
        String result;

        result = doParseSerialize(str);
        result = normalize(result);
        assertEquals(control, result);

        result = doParseSerialize(control);
        result = normalize(result);
        assertEquals(control, result);

    }
}
