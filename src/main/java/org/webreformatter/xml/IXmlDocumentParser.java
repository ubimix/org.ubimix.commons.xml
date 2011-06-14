/**
 * 
 */
package org.webreformatter.xml;

import org.webreformatter.ns.IId;

/**
 * Instances of this type are used as factories and parsers for XML documents.
 * 
 * @author kotelnikov
 */
public interface IXmlDocumentParser {

    /**
     * Returns an XML document corresponding to the given serialized XML.
     * 
     * @param document the document factory used to create node
     * @param defaultNamespace the default namespace which is associated with
     *        nodes without explicit namespaces
     * @param xml the serialized XML to parse
     */
    void parse(IXmlDocument document, IId defaultNamespace, String xml);

}
