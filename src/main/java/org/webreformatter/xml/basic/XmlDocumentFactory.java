/**
 * 
 */
package org.webreformatter.xml.basic;

import org.webreformatter.ns.NamespaceManager;
import org.webreformatter.xml.IXmlDocument;
import org.webreformatter.xml.IXmlDocumentFactory;

/**
 * @author kotelnikov
 */
public class XmlDocumentFactory implements IXmlDocumentFactory {
    private static final long serialVersionUID = 5421557962210471624L;

    private NamespaceManager fNamespaceManager;

    /**
     * Default constructor used for serialization/deserialization.
     */
    protected XmlDocumentFactory() {
    }

    /**
     * 
     */
    public XmlDocumentFactory(NamespaceManager namespaceManager) {
        fNamespaceManager = namespaceManager;
    }

    /**
     * @see org.webreformatter.xml.IXmlDocumentFactory#getNamespaceManager()
     */
    public NamespaceManager getNamespaceManager() {
        return fNamespaceManager;
    }

    /**
     * @see org.webreformatter.xml.IXmlDocumentFactory#newDocument()
     */
    public IXmlDocument newDocument() {
        return new XmlDocument(fNamespaceManager);
    }

}
