/**
 * 
 */
package org.webreformatter.rdf.xml;

import org.webreformatter.ns.NamespaceManager;
import org.webreformatter.xml.IXmlDocumentFactory;
import org.webreformatter.xml.IXmlDocumentParser;
import org.webreformatter.xml.basic.XmlDocumentFactory;
import org.webreformatter.xml.sax.SaxDocumentParser;

/**
 * @author kotelnikov
 */
public class NativeXmlTest extends AbstractXmlTest {

    public NativeXmlTest(String name) {
        super(name);
    }

    @Override
    protected IXmlDocumentFactory newXmlDocumentFactory(
        NamespaceManager namespaceManager) {
        return new XmlDocumentFactory(namespaceManager);
    }

    @Override
    protected IXmlDocumentParser newXmlParser() {
        return new SaxDocumentParser();
    }
}
