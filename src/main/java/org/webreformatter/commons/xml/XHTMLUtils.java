/**
 * 
 */
package org.webreformatter.commons.xml;

import org.w3c.dom.Node;

/**
 * @author kotelnikov
 */
public class XHTMLUtils {

    public static final String _NS_XHTML = "http://www.w3.org/1999/xhtml";

    public static String getHTMLName(Node tag) {
        String ns = tag.getNamespaceURI();
        String name = null;
        if (_NS_XHTML.equals(ns)) {
            name = tag.getLocalName();
        } else if (ns == null) {
            name = tag.getNodeName();
        }
        if (name != null) {
            name = name.toLowerCase();
        }
        return name;
    }

}
