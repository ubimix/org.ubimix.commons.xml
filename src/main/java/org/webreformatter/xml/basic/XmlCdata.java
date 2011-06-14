/**
 * 
 */
package org.webreformatter.xml.basic;

import org.webreformatter.xml.IXmlCdata;

/**
 * @author kotelnikov
 */
public class XmlCdata extends XmlText implements IXmlCdata {
    private static final long serialVersionUID = 6280382447027376201L;

    /**
     * Default constructor used for serialization/deserialization.
     */
    protected XmlCdata() {
    }

    /**
     * @param document
     */
    public XmlCdata(XmlDocument document, String content) {
        super(document, content);
    }

}
