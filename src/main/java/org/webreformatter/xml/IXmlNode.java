/**
 * 
 */
package org.webreformatter.xml;

import java.io.Serializable;

/**
 * The parent type for all XML nodes.
 * 
 * @author kotelnikov
 */
public interface IXmlNode extends Serializable {

    /**
     * Returns the document owning this node (this object is a node in the
     * structured of the returned document).
     * 
     * @return the owner document.
     */
    IXmlDocument getDocument();

}
