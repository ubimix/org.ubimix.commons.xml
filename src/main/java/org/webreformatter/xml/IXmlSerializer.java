/**
 * 
 */
package org.webreformatter.xml;

/**
 * @author kotelnikov
 */
public interface IXmlSerializer {

    /**
     * Instances of this type are used to filter XML nodes provided.
     * 
     * @author kotelnikov
     */
    public interface IXmlNodeAcceptor {
        boolean accept(IXmlNode node);
    }

    void serializeNode(IXmlNode node);

    void serializeNode(IXmlNode node, IXmlNodeAcceptor filter);
}
