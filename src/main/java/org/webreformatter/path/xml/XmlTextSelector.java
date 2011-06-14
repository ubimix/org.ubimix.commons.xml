/**
 * 
 */
package org.webreformatter.path.xml;

import org.webreformatter.xml.IXmlText;

public class XmlTextSelector extends TextSelector {

    public XmlTextSelector(String mask) {
        super(mask);
    }

    @Override
    protected String getTextValue(Object node) {
        String result = null;
        if (node instanceof IXmlText) {
            IXmlText text = (IXmlText) node;
            result = text.getContent();
        }
        return result;
    }

}