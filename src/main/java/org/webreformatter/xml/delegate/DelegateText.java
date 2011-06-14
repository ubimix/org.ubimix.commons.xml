/**
 * 
 */
package org.webreformatter.xml.delegate;

import org.webreformatter.xml.IXmlText;

/**
 * @author kotelnikov
 */
public class DelegateText extends DelegateNode implements IXmlText {
    private static final long serialVersionUID = 3708493849366456586L;

    /**
     * Default constructor used for serialization/deserialization.
     */
    protected DelegateText() {
    }

    /**
     * @param doc
     * @param node
     */
    public DelegateText(DelegateDocument doc, Object node) {
        super(doc, node);
    }

    /**
     * @see org.webreformatter.xml.IXmlText#getContent()
     */
    public String getContent() {
        IDelegateAdapter adapter = getDomAdapter();
        return adapter.getTextContent(fNode);
    }

    /**
     * @see org.webreformatter.xml.IXmlText#setContent(java.lang.String)
     */
    public void setContent(String content) {
        IDelegateAdapter adapter = getDomAdapter();
        adapter.setTextContent(fNode, content);
    }

}
