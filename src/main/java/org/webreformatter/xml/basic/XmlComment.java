/**
 * 
 */
package org.webreformatter.xml.basic;

import org.webreformatter.xml.IXmlComment;

/**
 * @author kotelnikov
 */
public class XmlComment extends XmlNode implements IXmlComment {
    private static final long serialVersionUID = 3471836253514506784L;

    private String fComment;

    /**
     * Default constructor used for serialization/deserialization.
     */
    protected XmlComment() {
    }

    /**
     * @param document
     */
    public XmlComment(XmlDocument document, String content) {
        super(document);
        fComment = content;
    }

    /**
     * @see org.webreformatter.xml.IXmlComment#getComment()
     */
    public String getComment() {
        return fComment;
    }

    /**
     * @see org.webreformatter.xml.IXmlComment#setComment(java.lang.String)
     */
    public void setComment(String comment) {
        fComment = comment != null ? comment : "";
    }

}
