/**
 * 
 */
package org.webreformatter.xml;

/**
 * An XML comment block.
 * 
 * @author kotelnikov
 */
public interface IXmlComment extends IXmlNode {

    /**
     * Return the string representation of the comment
     * 
     * @return the string representation of the comment
     */
    String getComment();

    /**
     * Sets a new comment.
     * 
     * @param comment the comment to set
     */
    void setComment(String comment);

}
