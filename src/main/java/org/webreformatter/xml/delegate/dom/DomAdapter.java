/**
 * 
 */
package org.webreformatter.xml.delegate.dom;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.webreformatter.ns.IId;
import org.webreformatter.ns.IName;
import org.webreformatter.ns.NamespaceManager;
import org.webreformatter.xml.delegate.IDelegateAdapter;

public class DomAdapter implements IDelegateAdapter {

    private DocumentBuilder fBuilderFactory;

    public void appendChild(Object parent, Object child) {
        Node p = (Node) parent;
        Node c = (Node) child;
        p.appendChild(c);
    }

    /**
     * Compares the given nodes and returns <code>true</code> if they are equal
     * 
     * @param first the first XML node to compare
     * @param second the second XML node to compare
     * @return <code>true</code> if the given XML nodes are equal
     */
    protected boolean compareNodes(Node first, Node second) {
        if (!equals(first.getNamespaceURI(), second.getNamespaceURI())
            || !equals(first.getLocalName(), second.getLocalName())) {
            return false;
        }
        // Attributes
        Map<String, String> firstMap = getAttributes(first);
        Map<String, String> secondMap = getAttributes(second);
        if (!equals(firstMap, secondMap)) {
            return false;
        }

        // Children
        NodeList firstChildren = first.getChildNodes();
        NodeList secondChildren = second.getChildNodes();
        if (firstChildren.getLength() != secondChildren.getLength()) {
            return false;
        }
        int len = firstChildren.getLength();
        for (int i = 0; i < len; i++) {
            Node firstChild = firstChildren.item(i);
            Node secondChild = secondChildren.item(i);
            if (!compareNodes(firstChild, secondChild)) {
                return false;
            }
        }
        return true;
    }

    public Object createCDATASection(Object document, String content) {
        Document doc = (Document) document;
        CDATASection result = doc.createCDATASection(content);
        return result;
    }

    public Object createComment(Object document, String content) {
        Document doc = (Document) document;
        Comment result = doc.createComment(content);
        return result;
    }

    public Object createElement(
        NamespaceManager manager,
        Object document,
        IName name) {
        Document doc = (Document) document;
        IId namespaceURI = name.getNamespaceURL();
        String prefix = manager.getPrefix(name);
        String qualifiedName = name.getLocalName();
        if (!isEmpty(prefix)) {
            qualifiedName = prefix + ":" + qualifiedName;
        }
        Element result = !isEmpty(namespaceURI) ? doc.createElementNS(
            namespaceURI.toString(),
            qualifiedName) : doc.createElement(qualifiedName);
        getQualifiedName(result, manager, name, true);
        return result;
    }

    public Object createTextNode(Object document, String content) {
        Document doc = (Document) document;
        return doc.createTextNode(content);
    }

    /**
     * Compares the given object and returns <code>true</code> if they are
     * equal.
     * 
     * @param first the first object to compare
     * @param second the second object to compare
     * @return <code>true</code> if the given objects are equal
     */
    private boolean equals(Object first, Object second) {
        if (first == null || second == null) {
            return first == second;
        }
        return first.equals(second);
    }

    public String getAttribute(
        NamespaceManager manager,
        Object document,
        Object element,
        IName attrName) {
        if (!(element instanceof Element)) {
            return null;
        }
        Element e = (Element) element;
        String qualifiedName = getQualifiedName(e, manager, attrName, false);
        IId namespaceURI = attrName.getNamespaceURL();
        String result = null;
        if (!isEmpty(namespaceURI)) {
            result = e.getAttributeNS(namespaceURI.toString(), qualifiedName);
        } else {
            result = e.getAttribute(qualifiedName);
        }
        return result;
    }

    public Map<IName, String> getAttributes(
        NamespaceManager manager,
        Object document,
        Object element) {
        if (!(element instanceof Element)) {
            return Collections.emptyMap();
        }
        Element e = (Element) element;
        Map<IName, String> result = new HashMap<IName, String>();
        NamedNodeMap map = e.getAttributes();
        for (int i = map.getLength() - 1; i >= 0; i--) {
            Attr attr = (Attr) map.item(i);
            if (isSystemAttr(attr)) {
                continue;
            }
            IName name = getName(manager, attr);
            String value = attr.getValue();
            result.put(name, value);
        }
        return result;
    }

    private Map<String, String> getAttributes(Node node) {
        if (!(node instanceof Element)) {
            return Collections.emptyMap();
        }
        Map<String, String> result = null;
        NamedNodeMap attributes = node.getAttributes();
        int len = attributes != null ? attributes.getLength() : 0;
        for (int i = 0; i < len; i++) {
            Attr attr = (Attr) attributes.item(i);
            if (isSystemAttr(attr)) {
                continue;
            }
            String localName = attr.getLocalName();
            String namespace = attr.getNamespaceURI();
            String value = attr.getValue();
            if (result == null) {
                result = new HashMap<String, String>();
            }
            result.put(namespace + localName, value);
        }
        return result;
    }

    /**
     * Returns a document builder factory used to create new documents and/or
     * parse existing documents.
     * 
     * @return a document builder factory
     * @throws ParserConfigurationException
     */
    private DocumentBuilder getBuilderFactory()
        throws ParserConfigurationException {
        if (fBuilderFactory == null) {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                .newInstance();
            factory.setNamespaceAware(true); // never forget this!
            try {
                factory.setFeature(
                    "http://xml.org/sax/features/namespaces",
                    true);
            } catch (Throwable t) {
                // Just skip it...
            }
            fBuilderFactory = factory.newDocumentBuilder();
        }
        return fBuilderFactory;
    }

    public Object getChild(Object node, int pos) {
        Node n = (Node) node;
        NodeList children = n.getChildNodes();
        return children.item(pos);
    }

    public int getChildNumber(Object node) {
        Node n = (Node) node;
        NodeList nodes = n.getChildNodes();
        return nodes.getLength();
    }

    public Object getDocumentElement(Object document) {
        if (!(document instanceof Document)) {
            return null;
        }
        Document doc = (Document) document;
        return doc.getDocumentElement();
    }

    public IName getElementName(
        NamespaceManager manager,
        Object document,
        Object element) {
        if (!(element instanceof Element)) {
            return null;
        }
        Element n = (Element) element;
        IName name = getName(manager, n);
        return name;
    }

    private String getLocalName(Node n) {
        String name = n.getNodeName();
        if (name == null) {
            name = n.getLocalName();
        } else {
            int idx = name.indexOf(":");
            if (idx > 0) {
                name = name.substring(idx + 1);
            }
        }
        return name;
    }

    private IName getName(NamespaceManager manager, Node node) {
        IId namespace = getNamespace(manager, node);
        String localName = getLocalName(node);
        IName name = manager.getName(namespace, localName);
        return name;
    }

    private IId getNamespace(NamespaceManager manager, Node n) {
        String namespace = n.getNamespaceURI();
        if (namespace == null) {
            String qualifiedName = n.getNodeName();
            int idx = qualifiedName.indexOf(":");
            String prefix = "";
            if (idx > 0) {
                prefix = qualifiedName.substring(0, idx);
            }
            namespace = n.lookupNamespaceURI(prefix);
            if (namespace == null) {
                if (idx > 0) {
                    namespace = qualifiedName.substring(0, idx);
                }
            }
        }
        if (namespace == null) {
            namespace = "";
        }
        IId id = manager.getId(namespace);
        return id;
    }

    public Object getParentNode(Object node) {
        Node n = (Node) node;
        Node result = n.getParentNode();
        return result;
    }

    public String getPrefix(Object node) {
        Node n = (Node) node;
        return n.getPrefix();
    }

    public Object getPreviousSibling(Object node) {
        Node n = (Node) node;
        Node result = n.getPreviousSibling();
        return result;
    }

    private String getQualifiedName(
        Element element,
        NamespaceManager manager,
        IName name,
        boolean createPrefix) {
        String prefix = manager.getPrefix(name);
        if (!isEmpty(prefix)) {
            IId namespaceURI = name.getNamespaceURL();
            String oldPrefix = !isEmpty(namespaceURI) ? element
                .lookupPrefix(namespaceURI.toString()) : null;
            if (oldPrefix == null || !oldPrefix.equals(prefix) && createPrefix) {
                if ("".equals(prefix)) {
                    element.setAttribute("xmlns", namespaceURI.toString());
                } else {
                    element.setAttributeNS(
                        "http://www.w3.org/2000/xmlns/",
                        "xmlns:" + prefix,
                        namespaceURI.toString());
                }
            }
        }
        String qualifiedName = name.getLocalName();
        if (!isEmpty(prefix)) {
            qualifiedName = prefix + ":" + qualifiedName;
        }
        return qualifiedName;
    }

    public String getTextContent(Object node) {
        Node n = (Node) node;
        return n.getTextContent();
    }

    public Object importNode(
        NamespaceManager oldNsManager,
        Object sourceDocument,
        NamespaceManager targetNsManager,
        Object targetDocument,
        Object nodeToCopy) {
        if (!(targetDocument instanceof Document)) {
            return false;
        }
        Document doc = (Document) targetDocument;
        Node n = (Node) nodeToCopy;
        Node result = doc.importNode(n, true);
        return result;
    }

    public void insertBefore(Object e, Object newChild, Object refChild) {
        Node n = (Node) e;
        n.insertBefore((Node) newChild, (Node) refChild);
    }

    public boolean isCDATASection(Object node) {
        return node instanceof CDATASection;
    }

    public boolean isComment(Object node) {
        return node instanceof Comment;
    }

    public boolean isElement(Object node) {
        return node instanceof Element;
    }

    private boolean isEmpty(Object o) {
        return o == null || "".equals(o.toString());
    }

    private boolean isSystemAttr(Attr attr) {
        String namespaceURI = attr.getNamespaceURI();
        if ("http://www.w3.org/2000/xmlns/".equals(namespaceURI)) {
            String name = attr.getName();
            if (name.startsWith("xmlns")) {
                return true;
            }
        }
        return false;
    }

    public boolean isText(Object node) {
        return node instanceof Text;
    }

    public Object newDocument(NamespaceManager manager) throws Exception {
        DocumentBuilder builder = getBuilderFactory();
        Document doc = builder.newDocument();
        return doc;
    }

    public boolean removeAttribute(
        NamespaceManager manager,
        Object document,
        Object element,
        IName attrName) {
        Element e = (Element) element;
        String qualifiedName = getQualifiedName(e, manager, attrName, false);
        IId namespaceURI = attrName.getNamespaceURL();
        NamedNodeMap map = e.getAttributes();
        Node item;
        if (!isEmpty(namespaceURI)) {
            item = map
                .removeNamedItemNS(namespaceURI.toString(), qualifiedName);
        } else {
            item = map.removeNamedItem(qualifiedName);
        }
        return item != null;
    }

    public void removeAttribute(Object element, Object attr) {
        Element e = (Element) element;
        Attr a = (Attr) attr;
        e.removeAttributeNode(a);
    }

    public void removeChild(Object node, int pos) {
        if (!(node instanceof Node)) {
            return;
        }
        Node n = (Node) node;
        NodeList children = n.getChildNodes();
        Node nodeToRemove = children.item(pos);
        n.removeChild(nodeToRemove);
    }

    public void replaceChild(Object node, Object oldChild, Object newNode) {
        Node n = (Node) node;
        Node oldN = (Node) oldChild;
        Node newN = (Node) newNode;
        n.replaceChild(oldN, newN);
    }

    public void setAttribute(
        NamespaceManager manager,
        Object document,
        Object node,
        IName name,
        String value) {
        if (!(node instanceof Element)) {
            return;
        }
        Element e = (Element) node;
        String qualifiedName = getQualifiedName(e, manager, name, true);
        IId namespaceURI = name.getNamespaceURL();
        if (!isEmpty(namespaceURI)) {
            e.setAttributeNS(namespaceURI.toString(), qualifiedName, value);
        } else {
            e.setAttribute(qualifiedName, value);
        }
    }

    public void setAttributes(
        NamespaceManager namespaceManager,
        Object document,
        Object element,
        Map<IName, String> attributes) {
        if (!(element instanceof Element)) {
            return;
        }
        Element e = (Element) element;
        NamedNodeMap map = e.getAttributes();
        for (int i = map.getLength() - 1; i >= 0; i--) {
            Attr attr = (Attr) map.item(i);
            String namespaceURI = attr.getNamespaceURI();
            String name = attr.getName();
            if (!isEmpty(namespaceURI)) {
                map.removeNamedItemNS(namespaceURI, name);
            } else {
                map.removeNamedItem(name);
            }
        }
        for (Map.Entry<IName, String> entry : attributes.entrySet()) {
            IName name = entry.getKey();
            String value = entry.getValue();
            String qualifiedName = getQualifiedName(
                e,
                namespaceManager,
                name,
                true);
            IId namespaceURI = name.getNamespaceURL();
            if (!isEmpty(namespaceURI)) {
                e.setAttributeNS(namespaceURI.toString(), qualifiedName, value);
            } else {
                e.setAttribute(qualifiedName, value);
            }
        }
    }

    public void setDocumentElement(Object document, Object newElement) {
        Document doc = (Document) document;
        Element e = (Element) newElement;
        Element oldElement = doc.getDocumentElement();
        if (oldElement != null) {
            doc.replaceChild(e, oldElement);
        } else {
            doc.appendChild(e);
        }

    }

    public void setElementNamespace(Object node, String prefix, String namespace) {
        if (!(node instanceof Element)) {
            return;
        }
        Element e = (Element) node;
        e.setAttributeNS("http://www.w3.org/2000/xmlns/", prefix, namespace);

    }

    public void setTextContent(Object node, String content) {
        Node n = (Node) node;
        n.setTextContent(content);
    }
}