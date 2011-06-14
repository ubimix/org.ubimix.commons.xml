/**
 * 
 */
package org.webreformatter.path.xml;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.webreformatter.ns.IId;
import org.webreformatter.ns.NamespaceManager;
import org.webreformatter.path.IPathNodeCollector;
import org.webreformatter.path.PathProcessor;
import org.webreformatter.xml.IXmlDocument;
import org.webreformatter.xml.IXmlDocumentFactory;
import org.webreformatter.xml.IXmlNode;
import org.webreformatter.xml.basic.XmlDocumentFactory;
import org.webreformatter.xml.sax.SaxDocumentParser;

/**
 * @author kotelnikov
 */
public class NodeSelectorTest extends TestCase {

    protected XmlSelectorBuilder fBuilder;

    private IId fDefaultNamespace;

    private IXmlDocumentFactory fDocumentFactory;

    /**
     * @param name
     */
    public NodeSelectorTest(String name) {
        super(name);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        NamespaceManager namespaceManager = new NamespaceManager();
        fDefaultNamespace = namespaceManager.getId("uri:ns#");
        namespaceManager.setNamespacePrefix(fDefaultNamespace, "");
        fDocumentFactory = new XmlDocumentFactory(namespaceManager);
        fBuilder = new XmlSelectorBuilder(namespaceManager);
    }

    private void test(String xml, PathProcessor selector, String... controlXml) {
        SaxDocumentParser parser = new SaxDocumentParser();
        IXmlDocument doc = fDocumentFactory.newDocument();
        parser.parse(doc, fDefaultNamespace, xml);

        final List<IXmlNode> result = new ArrayList<IXmlNode>();
        IPathNodeCollector collector = new IPathNodeCollector() {
            public boolean setResult(Object node) {
                result.add((IXmlNode) node);
                return true;
            }
        };
        selector.select(doc.getRootElement(), collector);

        int i = 0;
        for (String control : controlXml) {
            IXmlNode testElement = result.get(i++);
            String test = testElement.toString();
            assertEquals(control, test);
        }
        assertEquals(controlXml.length, result.size());
    }

    public void testComplexTests() throws Exception {

        fBuilder.clear();
        PathProcessor selector = fBuilder
            .element("item", true)
            .element("title")
            .buildPath();
        test(
            ""
                + " <feed>"
                + "     <title>Title of the feed</title>"
                + "     <item>"
                + "         <title>Title One</title>"
                + "         <content>This is the content 1</content>"
                + "     </item>"
                + "     <item>"
                + "         <title>Title Two</title>"
                + "         <content>This is the content 2</content>"
                + "         <comment>"
                + "             <title>Title of the comment</title>"
                + "             <content>Comment content</content>"
                + "         </comment>"
                + "     </item>"
                + "     <comment>"
                + "         <title>Title of the feed comment</title>"
                + "         <content>Content of the feed comment</content>"
                + "     </comment>"
                + "</feed>",
            selector,
            "<title xmlns='uri:ns#'>Title One</title>",
            "<title xmlns='uri:ns#'>Title Two</title>");

        // All titles
        fBuilder.clear();
        selector = fBuilder.element("title", true).buildPath();
        test(
            ""
                + " <feed>"
                + "     <title>Title of the feed</title>"
                + "     <item>"
                + "         <title>Title One</title>"
                + "         <content>This is the content 1</content>"
                + "     </item>"
                + "     <item>"
                + "         <title>Title Two</title>"
                + "         <content>This is the content 2</content>"
                + "         <comment>"
                + "             <title>Title of the item comment</title>"
                + "             <content>Comment content</content>"
                + "         </comment>"
                + "     </item>"
                + "     <comment>"
                + "         <title>Title of the feed comment</title>"
                + "         <content>Content of the feed comment</content>"
                + "     </comment>"
                + "</feed>",
            selector,
            "<title xmlns='uri:ns#'>Title of the feed</title>",
            "<title xmlns='uri:ns#'>Title One</title>",
            "<title xmlns='uri:ns#'>Title Two</title>",
            "<title xmlns='uri:ns#'>Title of the item comment</title>",
            "<title xmlns='uri:ns#'>Title of the feed comment</title>");

        fBuilder.clear();
        selector = fBuilder.element("a", true).attrs("m").buildPath();
        test(
            ""
                + " <html>"
                + "     <a/>"
                + "     <b>TEXT</b>"
                + "     <a m='n'>titi</a>"
                + "     <x>"
                + "         <y>"
                + "             <a m=''>TEXT</a>"
                + "         </y>"
                + "     </x>"
                + "</html>",
            selector,
            "<a m='n' xmlns='uri:ns#'>titi</a>",
            "<a m='' xmlns='uri:ns#'>TEXT</a>");

    }

    public void testElementAttributes() throws Exception {
        PathProcessor selector = fBuilder.element("a", true).attrs(
            "n",
            null,
            "m",
            null).buildPath();
        test(
            "<X><a>text</a><a n='123' m='345'>text</a><a>text</a></X>",
            selector,
            "<a n='123' m='345' xmlns='uri:ns#'>text</a>");
        test(
            "<X><a n='123'>text</a><a n='123' m='345'>text</a><a m='345'>text</a></X>",
            selector,
            "<a n='123' m='345' xmlns='uri:ns#'>text</a>");

        fBuilder.clear();
        selector = fBuilder.element("a", true).attrs("n", "123").buildPath();
        test(
            "<X><a>text</a><a n='123' m='345'>text</a><a>text</a></X>",
            selector,
            "<a n='123' m='345' xmlns='uri:ns#'>text</a>");
        test(
            "<X><a n='123'>text</a><a n='123' m='345'>text</a><a m='345'>text</a></X>",
            selector,
            "<a n='123' xmlns='uri:ns#'>text</a>",
            "<a n='123' m='345' xmlns='uri:ns#'>text</a>");

        // Find all elements with the specified attribute
        fBuilder.clear();
        selector = fBuilder.element(null, true).attrs("n", "123").buildPath();
        test(
            "<X><a>text</a><a n='123' m='345'>text</a><a>text</a></X>",
            selector,
            "<a n='123' m='345' xmlns='uri:ns#'>text</a>");
        test(
            "<X><a n='123'>text</a><a n='123' m='345'>text</a><a m='345'>text</a></X>",
            selector,
            "<a n='123' xmlns='uri:ns#'>text</a>",
            "<a n='123' m='345' xmlns='uri:ns#'>text</a>");
        test(
            "<X><a n='123' m='345'>A text</a><b n='123'>B text</b><K><L><M><N><c n='123'>C text</c></N></M></L></K></X>",
            selector,
            "<a n='123' m='345' xmlns='uri:ns#'>A text</a>",
            "<b n='123' xmlns='uri:ns#'>B text</b>",
            "<c n='123' xmlns='uri:ns#'>C text</c>");
    }

    public void testElementNames() throws Exception {
        PathProcessor selector = fBuilder.element("a", true).buildPath();
        test("<a/>", selector, "<a xmlns='uri:ns#'></a>");
        test("<X><a/></X>", selector, "<a xmlns='uri:ns#'></a>");
        test("<X><a>text</a></X>", selector, "<a xmlns='uri:ns#'>text</a>");
        test(
            "<X><a>A</a><a>B</a><a>C</a></X>",
            selector,
            "<a xmlns='uri:ns#'>A</a>",
            "<a xmlns='uri:ns#'>B</a>",
            "<a xmlns='uri:ns#'>C</a>");
        test(
            "<X><a>A</a><x1><x2><x3><a>B</a></x3></x2></x1><MM><a>C</a></MM></X>",
            selector,
            "<a xmlns='uri:ns#'>A</a>",
            "<a xmlns='uri:ns#'>B</a>",
            "<a xmlns='uri:ns#'>C</a>");

        // Negative tests
        test("<m/>", selector);
        test("<A/>", selector);
        test("<N><m></m></N>", selector);
    }

    public void testElementText() throws Exception {
        PathProcessor selector = fBuilder
            .element("a", true)
            .text("toto")
            .buildPath();
        test("<X><a>toto</a></X>", selector, "toto");
        test(
            "<X><a>toto</a><b>toto</b><a>toto</a></X>",
            selector,
            "toto",
            "toto");
        test(
            "<X><a>toto 1</a><b>toto 2</b><a>toto 3</a></X>",
            selector,
            "toto 1",
            "toto 3");

        test(
            "<X><a>A toto 1</a><b>B toto 2</b><a>C toto 3</a></X>",
            selector,
            "A toto 1",
            "C toto 3");
        // Negative tests
        test("<a/>", selector);
        test("<X><a>titi</a></X>", selector);
    }

}
