/**
 * 
 */
package org.webreformatter.xml.delegate;

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
public class DelegateDocument implements IXmlDocument {
    private static final long serialVersionUID = -3855623437375899163L;

    private IDelegateAdapter fDelegateAdapter;

    private Object fDocument;

    private NamespaceManager fNamespaceManager;

    private IXmlElement fRoot;

    /**
     * @param doc
     * @param defaultNamespace
     * @param xmlDocumentFactory
     */
    public DelegateDocument(
        IDelegateAdapter delegateAdapter,
        NamespaceManager namespaceManager,
        Object doc) {
        fDocument = doc;
        fNamespaceManager = namespaceManager;
        fDelegateAdapter = delegateAdapter;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DelegateDocument)) {
            return false;
        }
        DelegateDocument o = (DelegateDocument) obj;
        IXmlElement first = getRootElement();
        IXmlElement second = o.getRootElement();
        return first == null || second == null ? first == second : first
            .equals(second);
    }

    @SuppressWarnings("unchecked")
    public <N extends IXmlNode> N getClone(N node) {
        DelegateNode n = (DelegateNode) node;
        Object internalNode = n.fNode;
        IDelegateAdapter adapter = getDomAdapter();
        NamespaceManager sourceNsManager = n.getNamespaceManager();
        Object sourceDocument = ((DelegateDocument) n.getDocument())
            .getDocumentObject();
        Object newInternalNode = adapter.importNode(
            sourceNsManager,
            sourceDocument,
            fNamespaceManager,
            fDocument,
            internalNode);
        IXmlNode result = getXmlNode(newInternalNode);
        return (N) result;
    }

    public Object getDocumentObject() {
        return fDocument;
    }

    protected IDelegateAdapter getDomAdapter() {
        return fDelegateAdapter;
    }

    /**
     * Returns the underlying namespace manager
     * 
     * @return the underlying namespace manager
     */
    public NamespaceManager getNamespaceManager() {
        return fNamespaceManager;
    }

    /**
     * @param iId for this identifier the corresponding name is returned
     * @return the short form of the given id
     */
    protected String getNodeName(IName iId) {
        NamespaceManager manager = getNamespaceManager();
        String result = manager.getShortForm(iId);
        return result;
    }

    /**
     * @see org.webreformatter.xml.IXmlDocument#getRootElement()
     */
    public IXmlElement getRootElement() {
        if (fRoot == null) {
            IDelegateAdapter adapter = getDomAdapter();
            Object e = adapter.getDocumentElement(fDocument);
            if (e == null) {
                return null;
            }
            fRoot = new DelegateElement(this, e);
        }
        return fRoot;
    }

    protected IXmlNode getXmlNode(Object node) {
        IDelegateAdapter adapter = getDomAdapter();
        if (adapter.isCDATASection(node)) {
            return new DelegateCdata(this, node);
        } else if (adapter.isComment(node)) {
            return new DelegateComment(this, node);
        } else if (adapter.isText(node)) {
            return new DelegateText(this, node);
        } else if (adapter.isElement(node)) {
            return new DelegateElement(this, node);
        }
        return null;
    }

    @Override
    public int hashCode() {
        IXmlElement root = getRootElement();
        return root.hashCode();
    }

    /**
     * @see org.webreformatter.xml.IXmlDocument#newCdata(java.lang.String)
     */
    public IXmlCdata newCdata(String content) {
        IDelegateAdapter adapter = getDomAdapter();
        Object node = adapter.createCDATASection(fDocument, content);
        return (IXmlCdata) getXmlNode(node);
    }

    /**
     * @see org.webreformatter.xml.IXmlDocument#newComment(java.lang.String)
     */
    public IXmlComment newComment(String content) {
        IDelegateAdapter adapter = getDomAdapter();
        Object node = adapter.createComment(fDocument, content);
        return (IXmlComment) getXmlNode(node);
    }

    /**
     * @see org.webreformatter.xml.IXmlDocument#newElement(org.webreformatter.ns.IName)
     */
    public IXmlElement newElement(IName name) {
        NamespaceManager manager = getNamespaceManager();
        IDelegateAdapter adapter = getDomAdapter();
        Object node = adapter.createElement(manager, fDocument, name);
        return (IXmlElement) getXmlNode(node);
    }

    /**
     * @see org.webreformatter.xml.IXmlDocument#newText(java.lang.String)
     */
    public IXmlText newText(String content) {
        IDelegateAdapter adapter = getDomAdapter();
        Object node = adapter.createTextNode(fDocument, content);
        return (IXmlText) getXmlNode(node);
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
        DelegateElement e = (DelegateElement) element;
        Object newElement = e.getObject();
        IDelegateAdapter adapter = getDomAdapter();
        adapter.setDocumentElement(fDocument, newElement);
    }

    @Override
    public String toString() {
        IXmlElement root = getRootElement();
        return root != null ? root.toString() : "<null>";
    }

}
