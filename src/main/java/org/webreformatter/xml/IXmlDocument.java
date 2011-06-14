/**
 * 
 */
package org.webreformatter.xml;

import java.io.Serializable;

import org.webreformatter.ns.IName;
import org.webreformatter.ns.NamespaceManager;

/**
 * Instances of this type correspond to XML documents. Each document is a
 * factory of nodes used to build the document structure.
 * 
 * @author kotelnikov
 */
public interface IXmlDocument extends Serializable {

    /**
     * Creates and returns a new copy of the specified XML node. The returned
     * node could be attached to the structure of this document.
     * 
     * @param node the node to copy
     * @return a new copy of the specified XML node
     */
    <T extends IXmlNode> T getClone(T node);

    /**
     * Returns the namespace manager associated with this document
     * 
     * @return the namespace manager associated with this document
     */
    NamespaceManager getNamespaceManager();

    /**
     * Returns the root element of this document.
     * 
     * @return the root element of this document
     */
    IXmlElement getRootElement();

    /**
     * Creates and returns a new {@link IXmlCdata} instance containing the given
     * string. The returned node could be used to attach it to elements of this
     * document.
     * 
     * @param content the text content which should be wrapped in a CDATA
     *        instance.
     * @return a new CDATA instance
     */
    IXmlCdata newCdata(String content);

    /**
     * Creates and returns a new comment (see {@link IXmlComment}) object. The
     * returned node could be attached to the structure of this document.
     * 
     * @param content the text comment
     * @return a new {@link IXmlComment} instance containing the specified text
     */
    IXmlComment newComment(String content);

    /**
     * Creates and returns a new XML element with the specified name. The
     * returned node could be attached to the structure of this document.
     * 
     * @param name the name of the element
     * @return a newly created element corresponding to the specified name
     */
    IXmlElement newElement(IName name);

    /**
     * Creates and returns a newly created text node (see {@link IXmlText}). The
     * returned node could be attached to the structure of this document.
     * 
     * @param content the text content to wrap
     * @return a newly created text node
     */
    IXmlText newText(String content);

    /**
     * Sets a new namespace manager for this document
     * 
     * @param namespaceManager the namespace manager to set
     */
    void setNamespaceManager(NamespaceManager namespaceManager);

    /**
     * Sets a new root element for this document. The old root will be removed.
     * 
     * @param element the element to set as a root.
     */
    void setRootElement(IXmlElement element);

}
