/**
 * 
 */
package org.webreformatter.xml.delegate;

import org.webreformatter.xml.IXmlCdata;

/**
 * @author kotelnikov
 */
public class DelegateCdata extends DelegateText implements IXmlCdata {
    private static final long serialVersionUID = 6922677789589876787L;

    /**
     * Default constructor used for serialization/deserialization.
     */
    protected DelegateCdata() {
    }

    /**
     * @param doc
     * @param node
     */
    public DelegateCdata(DelegateDocument doc, Object node) {
        super(doc, node);
    }

}
