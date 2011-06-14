/**
 * 
 */
package org.webreformatter.xml.basic;

import org.webreformatter.xml.IXmlText;

/**
 * @author kotelnikov
 */
public class XmlText extends XmlNode implements IXmlText {
    private static final long serialVersionUID = -7775416969242334562L;

    private String fContent;

    /**
     * Default constructor used for serialization/deserialization.
     */
    protected XmlText() {
        super();
    }

    /**
     * @param document
     */
    public XmlText(XmlDocument document, String content) {
        super(document);
        fContent = content;
    }

    /**
     * @see org.webreformatter.xml.IXmlText#getContent()
     */
    public String getContent() {
        return fContent;
    }

    /**
     * @see org.webreformatter.xml.IXmlText#setContent(java.lang.String)
     */
    public void setContent(String content) {
        fContent = content != null ? content : "";
    }

}
