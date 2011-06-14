/**
 * 
 */
package org.webreformatter.xml;

/**
 * Instances of this type correspond to text nodes in XML documents.
 * 
 * @author kotelnikov
 */
public interface IXmlText extends IXmlNode {

    /**
     * Returns the string representation of the content of this node.
     * 
     * @return the string representation of the content of this node
     */
    String getContent();

    /**
     * Sets a new content for this node.
     * 
     * @param content the content to set
     */
    void setContent(String content);
}