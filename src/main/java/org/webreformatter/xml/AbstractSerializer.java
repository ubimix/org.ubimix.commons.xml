/**
 * 
 */
package org.webreformatter.xml;

/**
 * @author kotelnikov
 */
public abstract class AbstractSerializer implements IXmlSerializer {

    /**
     * 
     */
    public AbstractSerializer() {
    }

    public void serializeNode(IXmlNode node) {
        serializeNode(node, null);
    }

}
