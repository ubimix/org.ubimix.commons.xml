/**
 * 
 */
package org.webreformatter.rdf.xml;



import org.webreformatter.ns.NamespaceManager;
import org.webreformatter.xml.IXmlDocumentFactory;
import org.webreformatter.xml.IXmlDocumentParser;
import org.webreformatter.xml.delegate.DelegateDocumentFactory;
import org.webreformatter.xml.delegate.IDelegateAdapter;
import org.webreformatter.xml.delegate.dom.DomAdapter;
import org.webreformatter.xml.sax.SaxDocumentParser;

/**
 * @author kotelnikov
 */
public class XmlTest extends AbstractXmlTest {

    private IDelegateAdapter fDelegateAdapter = new DomAdapter();

    public XmlTest(String name) {
        super(name);
    }

    @Override
    protected IXmlDocumentFactory newXmlDocumentFactory(
        NamespaceManager namespaceManager) {
        return new DelegateDocumentFactory(namespaceManager, fDelegateAdapter);
    }

    @Override
    protected IXmlDocumentParser newXmlParser() {
        return new SaxDocumentParser();
    }
}
