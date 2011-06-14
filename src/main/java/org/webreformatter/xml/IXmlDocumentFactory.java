/**
 * 
 */
package org.webreformatter.xml;

import java.io.Serializable;

import org.webreformatter.ns.NamespaceManager;

/**
 * Instances of this type are used as factories and parsers for XML documents.
 * 
 * @author kotelnikov
 */
public interface IXmlDocumentFactory extends Serializable {

    /**
     * Returns the namespace manager used by this factory.
     * 
     * @return the namespace manager used by this factory
     */
    NamespaceManager getNamespaceManager();

    /**
     * Returns a newly created XML document.
     * 
     * @return a newly created XML document.
     */
    IXmlDocument newDocument();

}
