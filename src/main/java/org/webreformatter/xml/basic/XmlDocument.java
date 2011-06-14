/**
 * 
 */
package org.webreformatter.xml.basic;

import java.util.Map;

import org.webreformatter.ns.IName;
import org.webreformatter.ns.NamespaceManager;
import org.webreformatter.xml.IXmlCdata;
import org.webreformatter.xml.IXmlComment;
import org.webreformatter.xml.IXmlDocument;
import org.webreformatter.xml.IXmlElement;
import org.webreformatter.xml.IXmlNode;
import org.webreformatter.xml.IXmlText;

/**
 * @author kotelnikov
 */
public class XmlDocument implements IXmlDocument {

    private static final long serialVersionUID = 560741245137134752L;

    private NamespaceManager fNamespaceManager;

    private IXmlElement fRootElement;

    /**
     * Default constructor used for serialization/deserialization.
     */
    protected XmlDocument() {
        this(new NamespaceManager());
    }

    /**
     * 
     */
    public XmlDocument(NamespaceManager namespaceManager) {
        fNamespaceManager = namespaceManager;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof XmlDocument))
            return false;
        XmlDocument o = (XmlDocument) obj;
        return equals(fRootElement, o.fRootElement);
    }

    private boolean equals(Object first, Object second) {
        if (first == null || second == null)
            return first == second;
        return first.equals(second);
    }

    /**
     * @see org.webreformatter.xml.IXmlDocument#getClone(org.webreformatter.xml.IXmlNode)
     */
    @SuppressWarnings("unchecked")
    public <N extends IXmlNode> N getClone(N node) {
        IXmlNode result = null;
        if (node instanceof IXmlCdata) {
            result = newCdata(((IXmlCdata) node).getContent());
        } else if (node instanceof XmlText) {
            result = newText(((IXmlText) node).getContent());
        } else if (node instanceof IXmlComment) {
            result = newComment(((IXmlComment) node).getComment());
        } else if (node instanceof IXmlElement) {
            IXmlElement e = (IXmlElement) node;
            IName name = e.getName();
            IXmlElement resultElement = newElement(name);
            Map<IName, String> attributes = e.getAttributes();
            resultElement.setAttributes(attributes);
            for (IXmlNode child : e) {
                IXmlNode childClone = getClone(child);
                resultElement.addChild(childClone);
            }
            result = resultElement;
        }
        return (N) result;
    }

    /**
     * @see org.webreformatter.xml.IXmlDocument#getNamespaceManager()
     */
    public NamespaceManager getNamespaceManager() {
        return fNamespaceManager;
    }

    /**
     * @see org.webreformatter.xml.IXmlDocument#getRootElement()
     */
    public IXmlElement getRootElement() {
        return fRootElement;
    }

    @Override
    public int hashCode() {
        int first = fNamespaceManager.hashCode();
        int second = fRootElement != null ? fRootElement.hashCode() : 0;
        return first ^ second;
    }

    /**
     * @see org.webreformatter.xml.IXmlDocument#newCdata(java.lang.String)
     */
    public IXmlCdata newCdata(String content) {
        return new XmlCdata(this, content);
    }

    /**
     * @see org.webreformatter.xml.IXmlDocument#newComment(java.lang.String)
     */
    public IXmlComment newComment(String content) {
        return new XmlComment(this, content);
    }

    /**
     * @see org.webreformatter.xml.IXmlDocument#newElement(org.webreformatter.ns.IName)
     */
    public IXmlElement newElement(IName name) {
        return new XmlElement(this, name);
    }

    /**
     * @see org.webreformatter.xml.IXmlDocument#newText(java.lang.String)
     */
    public IXmlText newText(String content) {
        return new XmlText(this, content);
    }

    /**
     * @see org.webreformatter.xml.IXmlDocument#setNamespaceManager(org.webreformatter.ns.NamespaceManager)
     */
    public void setNamespaceManager(NamespaceManager namespaceManager) {
        fNamespaceManager = namespaceManager;
    }

    /**
     * @see org.webreformatter.xml.IXmlDocument#setRootElement(org.webreformatter.xml.IXmlElement)
     */
    public void setRootElement(IXmlElement element) {
        fRootElement = element;
    }

    @Override
    public String toString() {
        return fRootElement != null ? fRootElement.toString() : null;
    }

}
