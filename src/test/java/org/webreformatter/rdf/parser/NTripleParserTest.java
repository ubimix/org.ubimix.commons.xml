/**
 * 
 */
package org.webreformatter.rdf.parser;

import junit.framework.TestCase;

import org.webreformatter.ns.IId;
import org.webreformatter.ns.NamespaceManager;

/**
 * @author kotelnikov
 */
public class NTripleParserTest extends TestCase {

    /**
     * @param name
     */
    public NTripleParserTest(String name) {
        super(name);
    }

    private String doParseSerialize(String str) {
        NTriplesSerializer serializer = new NTriplesSerializer();
        NTriplesParser parser = new NTriplesParser(serializer);
        parser.parse(str);
        String test = serializer.toString();
        return test;
    }

    private void test(String str, String control) {
        final StringBuffer buf = new StringBuffer();

        NamespaceManager namespaceManager = new NamespaceManager();
        namespaceManager.setNamespacePrefix("", "");
        namespaceManager.setNamespacePrefix("_", "_");
        NTriplesParser parser = new NTriplesParser(
            namespaceManager,
            new ITripleListener() {

                public void onComment(String comment) {
                }

                public void onDatatypeStatement(
                    IId subject,
                    IId predicate,
                    String object,
                    IId type) {
                    buf.append("[").append(subject).append("]");
                    buf.append(" [").append(predicate).append("]");
                    buf.append(" '").append(object).append("'");
                    if (type != null) {
                        buf.append("^^[").append(type).append("]");
                    }
                    buf.append(".\n");
                }

                public void onReferenceStatement(
                    IId subject,
                    IId predicate,
                    IId object) {
                    buf.append("[").append(subject).append("]");
                    buf.append(" [").append(predicate).append("]");
                    buf.append(" [").append(object).append("]");
                    buf.append(".\n");
                }

                public void onStringStatement(
                    IId subject,
                    IId predicate,
                    String object,
                    String lang) {
                    buf.append("[").append(subject).append("]");
                    buf.append(" [").append(predicate).append("]");
                    buf.append(" '").append(object).append("'");
                    if (lang != null) {
                        buf.append("@").append(lang);
                    }
                    buf.append(".\n");
                }

            });
        parser.parse(str);
        assertEquals(control, buf.toString());
    }

    private void testErrors(final String string) {
        try {
            NTriplesParser parser = new NTriplesParser(
                NamespaceManager.INSTANCE,
                new ITripleListener() {

                    public void onComment(String comment) {
                        fail();
                    }

                    public void onDatatypeStatement(
                        IId subject,
                        IId predicate,
                        String object,
                        IId type) {
                        fail();
                    }

                    public void onReferenceStatement(
                        IId subject,
                        IId predicate,
                        IId object) {
                        fail();
                    }

                    public void onStringStatement(
                        IId subject,
                        IId predicate,
                        String object,
                        String lang) {
                        fail();
                    }

                },
                new IErrorListener() {

                    private void fail(int row, int col) {
                        String str = "";
                        for (int i = 0; i < col - 1; i++) {
                            str += "-";
                        }
                        str += "^";
                        str = "============="
                            + "Error ["
                            + row
                            + ":"
                            + col
                            + "] ===========================\n"
                            + string
                            + "\n"
                            + str;
                        throw new RuntimeException(str);
                    }

                    public void onBadObjectLang(int row, int col) {
                        fail(row, col);
                    }

                    public void onBadObjectType(int row, int col) {
                        fail(row, col);
                    }

                    public void onBadPredicate(int row, int col) {
                        fail(row, col);
                    }

                    public void onBadSubject(int row, int col) {
                        fail(row, col);
                    }

                    public void onNoObject(int row, int col) {
                        fail(row, col);
                    }

                    public void onNoPredicate(int row, int col) {
                        fail(row, col);
                    }

                });
            parser.parse(string);
            fail();
        } catch (RuntimeException e) {
            // System.out.println(e.getMessage());
        }
    }

    public void testParser() throws Exception {
        test("<s1><p1>\"a\\nb\"\n", "[s1] [p1] 'a\nb'.\n");
        test("_:s _:p _:o", "[_:s] [_:p] [_:o].\n");
        test("    <a>        <b>           'c'^^<t>.", "[a] [b] 'c'^^[t].\n");
        test("<a><b>'c'^^<t>.", "[a] [b] 'c'^^[t].\n");
        test("<a><b>'c'@en.", "[a] [b] 'c'@en.\n");
        test("<a><b>'c'.", "[a] [b] 'c'.\n");
        test("", "");
        test(""
            + "<a> <b> <c>.\n"
            + "<x> <y> 'Z'.\n"
            + "<A><B>\"C\"@en.\n"
            + "<A><B>\"C\"^^<T>.\n"
            + "", ""
            + "[a] [b] [c].\n"
            + "[x] [y] 'Z'.\n"
            + "[A] [B] 'C'@en.\n"
            + "[A] [B] 'C'^^[T].\n"
            + "");
        test(
            "<http://example.org/resource1> <http://example.org/property> <http://example.org/resource2> .",
            "[http://example.org/resource1] [http://example.org/property] [http://example.org/resource2].\n");
        test(
            "_:anon <http://example.org/property> <http://example.org/resource2> .",
            "[_:anon] [http://example.org/property] [http://example.org/resource2].\n");

        // Euro symbol \u20ac - 3 UTF-8 bytes #xE2 #x82 #xAC
        test("<x> <p> \"\\u20ac\"", "[x] [p] '€'.\n");

        test("<r7> <p> \"simple literal\" .\n", "[r7] [p] 'simple literal'.\n");
        test("<r8> <p> \"backslash:\\\\\" .\n", "[r8] [p] 'backslash:\\'.\n");
        test("<r9> <p> \"dquote:\\\"\" .\n", "[r9] [p] 'dquote:\"'.\n");
        test("<r10> <p> \"newline:\\n\" .\n", "[r10] [p] 'newline:\n'.\n");
        test("<r11> <p> \"return\\r\" .\n", "[r11] [p] 'return\r'.\n");
        test("<r12> <p> \"return\\t\" .\n", "[r12] [p] 'return\t'.\n");

        testErrors("abc");
        testErrors("<abc>");
        testErrors("<abc><cde>");
        testErrors("<abc><cde><ef");
    }

    public void testParseSerialize() {
        testParseSerialize("<s1><p1>\"a\\nb\"\n", "<s1> <p1> \"a\\nb\" .\n");

        testParseSerialize("", "");
        testParseSerialize("<s><p><o>", "<s> <p> <o> .\n");
        testParseSerialize("<s><p>'abc'", "<s> <p> \"abc\" .\n");
        testParseSerialize("<s><p>''", "<s> <p> \"\" .\n");
        testParseSerialize("<s><p>''@en", "<s> <p> \"\"@en .\n");
        testParseSerialize("<s><p>''^^<t>", "<s> <p> \"\"^^<t> .\n");
        testParseSerialize("<s><p>\"abc\"", "<s> <p> \"abc\" .\n");
        testParseSerialize("<s><p>'abc'@fr", "<s> <p> \"abc\"@fr .\n");
        testParseSerialize(
            "      <s>    <p>    'abc'     @fr",
            "<s> <p> \"abc\"@fr .\n");
        testParseSerialize("<s><p>'abc'^^<type>", "<s> <p> \"abc\"^^<type> .\n");
        testParseSerialize(
            "      <s>    <p>    \"abc\"     ^^<type>",
            "<s> <p> \"abc\"^^<type> .\n");
        testParseSerialize("_:s _:p _:o", "_:s _:p _:o .\n");
        testParseSerialize(""
            + "_:s _:p _:o\n"
            + "<s><p><o>\n"
            + "<s1><p1>\"Literal \\nStatement\"\n"
            + "<s2><p2>\"Literal Statement\"@en\n"
            + "<s3><p3>\"123\"^^<xsd:int>", ""
            + "_:s _:p _:o .\n"
            + "<s> <p> <o> .\n"
            + "<s1> <p1> \"Literal \\nStatement\" .\n"
            + "<s2> <p2> \"Literal Statement\"@en .\n"
            + "<s3> <p3> \"123\"^^<xsd:int> .\n");
    }

    private void testParseSerialize(String str, String control) {
        String test = doParseSerialize(str);
        assertEquals(control, test);
        test = doParseSerialize(test);
        assertEquals(control, test);
    }

}
