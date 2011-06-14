/**
 * 
 */
package org.webreformatter.xml.basic;

import org.webreformatter.xml.IXmlDocument;
import org.webreformatter.xml.IXmlElement;
import org.webreformatter.xml.IXmlNode;
import org.webreformatter.xml.XmlSerializer;

/**
 * @author kotelnikov
 */
public class XmlNode implements IXmlNode {
    private static final long serialVersionUID = -5580714813808700535L;

    protected XmlDocument fDocument;

    private IXmlElement fParent;

    private int fPos;

    /**
     * The default constructor used to serialize/deserialize instances.
     */
    protected XmlNode() {
    }

    /**
     * 
     */
    public XmlNode(XmlDocument document) {
        setDocument(document);
    }

    @Override
    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * @see org.webreformatter.xml.IXmlNode#getDocument()
     */
    public IXmlDocument getDocument() {
        return fDocument;
    }

    /**
     * @see org.webreformatter.xml.IXmlNode#getNextSibling()
     */
    public IXmlNode getNextSibling() {
        int parentSize = fParent != null ? fParent.getChildCount() : 0;
        return fPos < parentSize - 1 ? fParent.getChild(fPos + 1) : null;
    }

    int getPos() {
        return fPos;
    }

    /**
     * @see org.webreformatter.xml.IXmlNode#getPreviousSibling()
     */
    public IXmlNode getPreviousSibling() {
        int parentSize = fParent != null ? fParent.getChildCount() : 0;
        return fPos > 0 && fPos < parentSize
            ? fParent.getChild(fPos - 1)
            : null;
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    void removeFromParent() {
        int parentSize = fParent != null ? fParent.getChildCount() : 0;
        if (fPos >= 0 && fPos < parentSize) {
            fParent.removeChild(fPos);
        }
        fParent = null;
    }

    protected void setDocument(XmlDocument document) {
        fDocument = document;
    }

    void setParent(IXmlElement parent) {
        fParent = parent;
    }

    void setPos(int pos) {
        fPos = pos;
    }

    @Override
    public String toString() {
        XmlSerializer serializer = new XmlSerializer(true);
        serializer.serializeNode(this);
        return serializer.toString();
    }

}
