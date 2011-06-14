/**
 * 
 */
package org.webreformatter.rdf.xml;

import java.util.Map;

import junit.framework.TestCase;

import org.webreformatter.ns.IId;
import org.webreformatter.ns.IName;
import org.webreformatter.ns.NamespaceManager;
import org.webreformatter.xml.IXmlDocument;
import org.webreformatter.xml.IXmlDocumentFactory;
import org.webreformatter.xml.IXmlDocumentParser;
import org.webreformatter.xml.IXmlElement;
import org.webreformatter.xml.IXmlNode;
import org.webreformatter.xml.IXmlText;
import org.webreformatter.xml.XmlSerializer;

/**
 * @author kotelnikov
 */
public/**
 * @author kotelnikov
 */
abstract class AbstractXmlTest extends TestCase {

    protected IXmlDocumentFactory fFactory;

    protected IXmlDocumentParser fParser;

    public AbstractXmlTest(String name) {
        super(name);
    }

    protected abstract IXmlDocumentFactory newXmlDocumentFactory(
        NamespaceManager namespaceManager);

    protected abstract IXmlDocumentParser newXmlParser();

    private IXmlDocument parse(String namespace, String xml) {
        IXmlDocument document = fFactory.newDocument();
        NamespaceManager namespaceManager = fFactory.getNamespaceManager();
        IId namespaceId = namespaceManager.getId(namespace);
        if (namespaceId != null) {
            namespaceManager.setNamespacePrefix(namespaceId, "");
        }
        fParser.parse(document, namespaceId, xml);
        return document;
    }

    @Override
    protected void setUp() throws Exception {
        NamespaceManager namespaceManager = new NamespaceManager();
        fFactory = newXmlDocumentFactory(namespaceManager);
        fParser = newXmlParser();
    }

    public void test() {
        testRootElement(
            "",
            "<n:xml xmlns:n='http://www.foo.bar/ns#'/>",
            "http://www.foo.bar/ns#",
            "xml");
    }

    public void testAttributesRead() throws Exception {
        NamespaceManager ns = fFactory.getNamespaceManager();
        ns.setNamespacePrefix("http://www.foo.bar#", "p");
        assertEquals(ns.getName("p:x"), ns.getName("http://www.foo.bar#", "x"));

        IXmlDocument document = parse(
            "http://www.foo.bar#",
            "<n:xml n:x='y' n:a='b' n:c='d' xmlns:n='http://www.foo.bar#' />");
        assertNotNull(document);
        IXmlElement root = document.getRootElement();
        Map<IName, String> attributes = root.getAttributes();
        assertNotNull(attributes);
        assertEquals(3, attributes.size());
        assertEquals(
            "y",
            attributes.get(ns.getName("http://www.foo.bar#", "x")));
        assertEquals(
            "b",
            attributes.get(ns.getName("http://www.foo.bar#", "a")));
        assertEquals(
            "d",
            attributes.get(ns.getName("http://www.foo.bar#", "c")));
    }

    public void testCreateDocument() {
        IXmlDocument doc = fFactory.newDocument();
        String ns = "http://www.foo.bar#";
        NamespaceManager manager = doc.getNamespaceManager();
        manager.setNamespacePrefix(ns, "");
        IName name = manager.getName(ns, "html");
        IXmlElement html = doc.newElement(name);
        doc.setRootElement(html);
        IXmlElement head = doc.newElement(manager.getName(ns, "head"));
        IXmlElement body = doc.newElement(manager.getName(ns, "body"));
        html.addChild(head);
        html.addChild(body);

        IXmlElement p = doc.newElement(manager.getName(ns, "p"));
        body.addChild(p);
        IXmlText text = doc.newText("Hello, there!");
        p.addChild(text);

        testSerialize(html, true, "<html xmlns='http://www.foo.bar#'>"
            + "<head></head>"
            + "<body><p>Hello, there!</p></body>"
            + "</html>");
        testSerialize(html, false, "<html xmlns='http://www.foo.bar#'>"
            + "<head />"
            + "<body><p>Hello, there!</p></body>"
            + "</html>");
        manager.setNamespacePrefix(ns, "x");
        testSerialize(html, true, "<x:html xmlns:x='http://www.foo.bar#'>"
            + "<x:head></x:head>"
            + "<x:body><x:p>Hello, there!</x:p></x:body>"
            + "</x:html>");
        String t = testSerialize(
            html,
            false,
            "<x:html xmlns:x='http://www.foo.bar#'>"
                + "<x:head />"
                + "<x:body><x:p>Hello, there!</x:p></x:body>"
                + "</x:html>");

        IXmlDocument doc1 = parse("http://www.foo.bar#", t);
        String a = html.toString();
        String b = doc1.getRootElement().toString();
        assertEquals(a, b);
    }

    private void testName(IName name, String namespace, String localName) {
        assertNotNull(name);
        assertEquals(localName, name.getLocalName());
        IId namespaceId = namespace != null ? fFactory
            .getNamespaceManager()
            .getId(namespace) : null;
        assertEquals(namespaceId, name.getNamespaceURL());
    }

    public void testNamespaces() throws Exception {
        {
            IXmlDocument document = parse("http://www.foo.bar#", ""
                + "<a xmlns:m='http://www.foo.bar#'>"
                + "<b xmlns:n='http://www.foo.bar#'>"
                + "<m:toto>TOTO</m:toto>"
                + "<n:toto>TOTO</n:toto></b>"
                + "</a>");
            assertNotNull(document);
            IXmlElement root = document.getRootElement();
            IXmlElement b = (IXmlElement) root.getChild(0);
            for (IXmlNode node : b) {
                assertTrue(node instanceof IXmlElement);
                IXmlElement e = (IXmlElement) node;
                testName(e.getName(), "http://www.foo.bar#", "toto");
            }
        }
        testNamespaces(
            "",
            ""
                + "<a xmlns='http://www.foo.bar#'>"
                + "<toto>TOTO</toto>"
                + "<n:toto xmlns:n='http://www.foo.bar#'>TOTO</n:toto>"
                + "</a>",
            "http://www.foo.bar#",
            "toto",
            "http://www.foo.bar#",
            "toto");

        // Empty prefix is mapped to the http://www.foo.bar# namespace.
        NamespaceManager ns = fFactory.getNamespaceManager();
        ns.setNamespacePrefix("http://www.foo.bar#", "");
        assertEquals(ns.getName("x"), ns.getName("http://www.foo.bar#", "x"));
        testNamespaces(
            "http://www.foo.bar#",
            ""
                + "<a>"
                + "<toto>TOTO</toto>"
                + "<n:toto xmlns:n='http://www.foo.bar#'>TOTO</n:toto>"
                + "</a>",
            "http://www.foo.bar#",
            "toto",
            "http://www.foo.bar#",
            "toto");
    }

    private void testNamespaces(
        String defaultNamespace,
        String xml,
        String... ns) {
        IXmlDocument document = parse(defaultNamespace, xml);
        assertNotNull(document);
        IXmlElement root = document.getRootElement();
        int counter = 0;
        for (IXmlNode node : root) {
            assertTrue(node instanceof IXmlElement);
            IXmlElement e = (IXmlElement) node;
            String namespaceUrl = ns[counter++];
            String name = ns[counter++];
            testName(e.getName(), namespaceUrl, name);
        }
        assertEquals(ns.length, counter);
    }

    public void testNodeChildrenRead() throws Exception {
        IXmlDocument document = parse("http://www.foo.bar#", ""
            + "<html>"
            + "<head><title>Hello, world</title></head>"
            + "<body><h1>I am here</h1></body>"
            + "</html>");
        assertNotNull(document);
        IXmlElement root = document.getRootElement();
        assertNotNull(root);
        testName(root.getName(), "http://www.foo.bar#", "html");

        int len = root.getChildCount();
        assertEquals(2, len);

        IXmlNode first = root.getChild(0);
        assertNotNull(first);
        assertTrue(first instanceof IXmlElement);
        testName(((IXmlElement) first).getName(), "http://www.foo.bar#", "head");

        IXmlNode second = root.getChild(1);
        assertNotNull(second);
        assertTrue(second instanceof IXmlElement);
        testName(
            ((IXmlElement) second).getName(),
            "http://www.foo.bar#",
            "body");
    }

    public void testRootElement() throws Exception {
        testRootElement("", "<xml/>", "", "xml");
        testRootElement(
            "",
            "<n:xml xmlns:n='http://www.foo.bar/ns#'/>",
            "http://www.foo.bar/ns#",
            "xml");

        NamespaceManager ns = fFactory.getNamespaceManager();
        assertEquals(
            "n",
            ns.getNamespacePrefix("http://www.foo.bar/ns#", false));
        ns.setNamespacePrefix("http://www.foo.bar#", "");
        testRootElement(
            "http://www.foo.bar/ns#",
            "<xml />",
            "http://www.foo.bar/ns#",
            "xml");
    }

    private void testRootElement(
        String defaultNamespace,
        String xml,
        String namespace,
        String localName) {
        IXmlDocument document = parse(defaultNamespace, xml);
        assertNotNull(document);
        IXmlElement root = document.getRootElement();
        assertNotNull(root);
        IName name = root.getName();
        testName(name, namespace, localName);

    }

    private String testSerialize(
        IXmlElement html,
        boolean canonicalForm,
        String control) {
        XmlSerializer serializer = new XmlSerializer(canonicalForm);
        serializer.serializeNode(html);
        String str = serializer.toString();
        assertEquals(control, str);
        return str;
    }

}
