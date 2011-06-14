/**
 * 
 */
package org.webreformatter.xml.delegate;

import java.util.Map;

import org.webreformatter.ns.IName;
import org.webreformatter.ns.NamespaceManager;

/**
 * @author kotelnikov
 */
public interface IDelegateAdapter {

    void appendChild(Object parent, Object child);

    Object createCDATASection(Object document, String content);

    Object createComment(Object document, String content);

    Object createElement(NamespaceManager manager, Object document, IName name);

    Object createTextNode(Object document, String content);

    String getAttribute(
        NamespaceManager manager,
        Object document,
        Object element,
        IName attrName);

    Map<IName, String> getAttributes(
        NamespaceManager manager,
        Object document,
        Object element);

    Object getChild(Object node, int pos);

    int getChildNumber(Object node);

    Object getDocumentElement(Object document);

    IName getElementName(
        NamespaceManager manager,
        Object document,
        Object element);

    String getTextContent(Object node);

    Object importNode(
        NamespaceManager oldNsManager,
        Object sourceDocument,
        NamespaceManager targetNsManager,
        Object targetDocument,
        Object nodeToCopy);

    void insertBefore(Object parent, Object newChild, Object refChild);

    boolean isCDATASection(Object node);

    boolean isComment(Object node);

    boolean isElement(Object node);

    boolean isText(Object node);

    Object newDocument(NamespaceManager manager) throws Exception;

    boolean removeAttribute(
        NamespaceManager manager,
        Object document,
        Object element, IName attrName);

    void removeChild(Object node, int pos);

    void replaceChild(Object node, Object oldChild, Object newNode);

    void setAttribute(
        NamespaceManager manager,
        Object document,
        Object node,
        IName name,
        String value);

    void setAttributes(
        NamespaceManager manager,
        Object document,
        Object element,
        Map<IName, String> attributes);

    void setDocumentElement(Object document, Object newElement);

    void setTextContent(Object node, String content);

}
