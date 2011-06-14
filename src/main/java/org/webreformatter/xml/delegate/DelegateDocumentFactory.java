/**
 * 
 */
package org.webreformatter.xml.delegate;

import org.webreformatter.ns.NamespaceManager;
import org.webreformatter.xml.IXmlDocument;
import org.webreformatter.xml.IXmlDocumentFactory;

/**
 * @author kotelnikov
 */
public class DelegateDocumentFactory implements IXmlDocumentFactory {
    private static final long serialVersionUID = 592916484933508173L;

    private IDelegateAdapter fDelegateAdapter;

    private NamespaceManager fNamespaceManager;

    /**
     * @param namespaceManager
     */
    public DelegateDocumentFactory(
        NamespaceManager namespaceManager,
        IDelegateAdapter adapter) {
        fNamespaceManager = namespaceManager;
        fDelegateAdapter = adapter;
    }

    public IDelegateAdapter getDomAdapter() {
        return fDelegateAdapter;
    }

    /**
     * @see org.webreformatter.xml.IXmlDocumentFactory#getNamespaceManager()
     */
    public NamespaceManager getNamespaceManager() {
        return fNamespaceManager;
    }

    /**
     * @param t
     * @return an error wrapping the given exception
     * @throws Error
     */
    private Error handleError(Throwable t) throws Error {
        if (t instanceof Error) {
            return (Error) t;
        }
        return new Error(t);
    }

    /**
     * @see org.webreformatter.xml.IXmlDocumentFactory#newDocument()
     */
    public IXmlDocument newDocument() {
        try {
            Object doc = fDelegateAdapter.newDocument(fNamespaceManager);
            return newDocument(doc);
        } catch (Throwable e) {
            throw handleError(e);
        }
    }

    /**
     * Creates and returns a newly created document (a {@link IXmlDocument}
     * instance).
     * 
     * @param doc the DOM object
     * @param defaultNamespace the default namespace which is associated with
     *        nodes without explicit namespaces
     * @return a newly created document ({@link IXmlDocument} instance).
     */
    public DelegateDocument newDocument(Object doc) {
        return new DelegateDocument(fDelegateAdapter, fNamespaceManager, doc);
    }

    protected void setDomAdapter(IDelegateAdapter delegateAdapter) {
        fDelegateAdapter = delegateAdapter;
    }

}
