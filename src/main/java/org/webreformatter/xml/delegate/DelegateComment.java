/**
 * 
 */
package org.webreformatter.xml.delegate;

import org.webreformatter.xml.IXmlComment;

/**
 * @author kotelnikov
 */
public class DelegateComment extends DelegateNode implements IXmlComment {
    private static final long serialVersionUID = -3582144719577939217L;

    /**
     * Default constructor used for serialization/deserialization.
     */
    protected DelegateComment() {
    }

    /**
     * @param doc
     * @param node
     */
    public DelegateComment(DelegateDocument doc, Object node) {
        super(doc, node);
    }

    /**
     * @see org.webreformatter.xml.IXmlComment#getComment()
     */
    public String getComment() {
        IDelegateAdapter adapter = getDomAdapter();
        return adapter.getTextContent(getObject());
    }

    /**
     * @see org.webreformatter.xml.IXmlComment#setComment(java.lang.String)
     */
    public void setComment(String content) {
        IDelegateAdapter adapter = getDomAdapter();
        adapter.setTextContent(fNode, content);
    }

}
