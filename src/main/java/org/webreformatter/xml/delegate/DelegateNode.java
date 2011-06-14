/**
 * 
 */
package org.webreformatter.xml.delegate;

import org.webreformatter.ns.IId;
import org.webreformatter.ns.NamespaceManager;
import org.webreformatter.xml.IXmlDocument;
import org.webreformatter.xml.IXmlNode;
import org.webreformatter.xml.XmlSerializer;

/**
 * @author kotelnikov
 */
public class DelegateNode implements IXmlNode {
    private static final long serialVersionUID = 7505215921254224045L;

    protected DelegateDocument fDocument;

    protected Object fNode;

    /**
     * Default constructor used for serialization/deserialization.
     */
    protected DelegateNode() {
    }

    /**
     * 
     */
    public DelegateNode(DelegateDocument doc, Object node) {
        fDocument = doc;
        fNode = node;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DelegateNode)) {
            return false;
        }
        DelegateNode n = (DelegateNode) obj;
        return fNode.equals(n.fNode);
    }

    /**
     * @see org.webreformatter.xml.IXmlNode#getDocument()
     */
    public IXmlDocument getDocument() {
        return fDocument;
    }

    protected Object getDocumentObject() {
        return fDocument.getDocumentObject();
    }

    protected IDelegateAdapter getDomAdapter() {
        return fDocument.getDomAdapter();
    }

    protected NamespaceManager getNamespaceManager() {
        NamespaceManager namespaceManager = fDocument.getNamespaceManager();
        return namespaceManager;
    }

    public Object getObject() {
        return fNode;
    }

    @Override
    public final int hashCode() {
        return fNode.hashCode();
    }

    protected String registerNamespacePrefix(IId namespaceUri, String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        NamespaceManager namespaceManager = getNamespaceManager();
        String oldPrefix = namespaceManager.getNamespacePrefix(
            namespaceUri,
            false);
        if (oldPrefix == null) {
            namespaceManager.setNamespacePrefix(namespaceUri, prefix);
        } else {
            prefix = oldPrefix;
        }
        return prefix;
    }

    @Override
    public String toString() {
        XmlSerializer serializer = new XmlSerializer(true);
        serializer.serializeNode(this);
        return serializer.toString();
    }

}
