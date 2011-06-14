/**
 * 
 */
package org.webreformatter.xml.delegate;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.webreformatter.ns.IName;
import org.webreformatter.ns.NamespaceManager;
import org.webreformatter.xml.IXmlElement;
import org.webreformatter.xml.IXmlNode;

/**
 * @author kotelnikov
 */
public class DelegateElement extends DelegateNode implements IXmlElement {
    private static final long serialVersionUID = 5840389906865279552L;

    private IName fName;

    /**
     * Default constructor used for serialization/deserialization.
     */
    protected DelegateElement() {
    }

    /**
     * @param e
     * @param xmlDocument
     */
    public DelegateElement(DelegateDocument xmlDocument, Object e) {
        super(xmlDocument, e);
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#addChild(int,
     *      org.webreformatter.xml.IXmlNode)
     */
    public void addChild(int pos, IXmlNode node) {
        addChildren(pos, Arrays.asList(node));
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#addChild(org.webreformatter.xml.IXmlNode)
     */
    public void addChild(IXmlNode node) {
        int len = getChildCount();
        addChild(len, node);
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#addChildren(int,
     *      java.lang.Iterable)
     */
    public void addChildren(int pos, Iterable<IXmlNode> children) {
        IDelegateAdapter adapter = getDomAdapter();
        Object e = getObject();
        int len = adapter.getChildNumber(e);
        if (pos >= len) {
            for (IXmlNode node : children) {
                DelegateNode n = (DelegateNode) node;
                Object internalNode = n.getObject();
                adapter.appendChild(e, internalNode);
            }
        } else {
            Object nextNode = adapter.getChild(e, pos);
            for (IXmlNode node : children) {
                DelegateNode n = (DelegateNode) node;
                Object internalNode = n.getObject();
                adapter.insertBefore(e, internalNode, nextNode);
            }
        }
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#getAttribute(org.webreformatter.ns.IName)
     */
    public String getAttribute(IName attrName) {
        IDelegateAdapter adapter = getDomAdapter();
        Object element = getObject();
        NamespaceManager namespaceManager = getNamespaceManager();
        Object documentObject = getDocumentObject();
        String value = adapter.getAttribute(
            namespaceManager,
            documentObject,
            element,
            attrName);
        return value;
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#getAttributes()
     */
    public Map<IName, String> getAttributes() {
        IDelegateAdapter adapter = getDomAdapter();
        Object element = getObject();
        NamespaceManager namespaceManager = getNamespaceManager();
        Object documentObject = getDocumentObject();
        Map<IName, String> result = adapter.getAttributes(
            namespaceManager,
            documentObject,
            element);
        return result != null ? result : Collections.<IName, String> emptyMap();
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#getChild(int)
     */
    public IXmlNode getChild(int pos) {
        Object e = getObject();
        IDelegateAdapter adapter = getDomAdapter();
        int len = adapter.getChildNumber(e);
        if (pos < 0 || pos >= len) {
            return null;
        }
        Object node = adapter.getChild(e, pos);
        return fDocument.getXmlNode(node);
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#getChildCount()
     */
    public int getChildCount() {
        Object e = getObject();
        IDelegateAdapter adapter = getDomAdapter();
        return adapter.getChildNumber(e);
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#getName()
     */
    public IName getName() {
        if (fName == null) {
            NamespaceManager namespaceManager = getNamespaceManager();
            Object documentObject = getDocumentObject();
            IDelegateAdapter adapter = getDomAdapter();
            Object e = getObject();
            fName = adapter.getElementName(namespaceManager, documentObject, e);
        }
        return fName;
    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<IXmlNode> iterator() {
        final Object e = getObject();
        final IDelegateAdapter adapter = getDomAdapter();
        final int len = adapter.getChildNumber(e);
        return new Iterator<IXmlNode>() {
            int pos;

            public boolean hasNext() {
                return pos < len;
            }

            public IXmlNode next() {
                if (pos >= len) {
                    return null;
                }
                Object node = adapter.getChild(e, pos);
                pos++;
                return fDocument.getXmlNode(node);
            }

            public void remove() {
                throw new RuntimeException("Not implemented.");
            }
        };
    }

    public boolean removeAttribute(IName attrName) {
        Object element = getObject();
        Object document = getDocumentObject();
        IDelegateAdapter adapter = getDomAdapter();
        NamespaceManager namespaceManager = getNamespaceManager();
        return adapter.removeAttribute(
            namespaceManager,
            document,
            element,
            attrName);
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#removeChild(int)
     */
    public IXmlNode removeChild(int pos) {
        Object e = getObject();
        IDelegateAdapter adapter = getDomAdapter();
        int len = adapter.getChildNumber(e);
        if (pos < 0 || pos >= len) {
            return null;
        }
        Object node = adapter.getChild(e, pos);
        adapter.removeChild(e, pos);
        return fDocument.getXmlNode(node);
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#setAttribute(org.webreformatter.ns.IName,
     *      java.lang.String)
     */
    public void setAttribute(IName attrName, String value) {
        Object e = getObject();
        setAttribute(e, attrName, value);
    }

    private void setAttribute(Object e, IName name, String value) {
        IDelegateAdapter adapter = getDomAdapter();
        NamespaceManager namespaceManager = getNamespaceManager();
        Object documentObject = getDocumentObject();
        adapter.setAttribute(namespaceManager, documentObject, e, name, value);
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#setAttributes(java.util.Map)
     */
    public void setAttributes(Map<IName, String> attributes) {
        Object e = getObject();
        IDelegateAdapter adapter = getDomAdapter();
        NamespaceManager namespaceManager = getNamespaceManager();
        Object documentObject = getDocumentObject();
        adapter.setAttributes(namespaceManager, documentObject, e, attributes);
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#setChild(int,
     *      org.webreformatter.xml.IXmlNode)
     */
    public void setChild(int pos, IXmlNode node) {
        Object e = getObject();
        IDelegateAdapter adapter = getDomAdapter();
        int len = adapter.getChildNumber(e);
        if (pos < 0 || pos > len) {
            return;
        }
        DelegateNode n = (DelegateNode) node;
        Object newNode = n.getObject();
        if (pos == len) {
            adapter.appendChild(e, newNode);
        } else {
            Object child = adapter.getChild(e, pos);
            adapter.replaceChild(e, child, newNode);
        }
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#setChildren(java.lang.Iterable)
     */
    public void setChildren(Iterable<IXmlNode> children) {
        Object e = getObject();
        IDelegateAdapter adapter = getDomAdapter();
        {
            int len = adapter.getChildNumber(e);
            for (int i = len - 1; i >= 0; i--) {
                adapter.removeChild(e, i);
            }
        }
        if (children != null) {
            for (IXmlNode node : children) {
                DelegateNode n = (DelegateNode) node;
                Object internalNode = n.getObject();
                adapter.appendChild(e, internalNode);
            }
        }
    }

}
