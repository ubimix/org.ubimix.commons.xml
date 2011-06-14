package org.webreformatter.xml;

import java.util.Map;

import org.webreformatter.ns.IName;

/**
 * An XML element. Each XML element is represented as a named entity having
 * attributes and child nodes.
 * 
 * @author kotelnikov
 */
public interface IXmlElement extends IXmlNode, Iterable<IXmlNode> {

    /**
     * Inserts the given node in the specified position.
     * 
     * @param pos the position where the node should be inserted.
     * @param node the node to insert
     */
    void addChild(int pos, IXmlNode node);

    /**
     * Inserts the given node at the end.
     * 
     * @param node the node to insert
     */
    void addChild(IXmlNode node);

    /**
     * Inserts the child nodes in the specified position.
     * 
     * @param pos the position where the children should be inserted
     * @param children the children to insert
     */
    void addChildren(int pos, Iterable<IXmlNode> children);

    /**
     * Returns the value of the attribute with the specified name.
     * 
     * @param attrName the name of the attribute
     * @return the value of the specified attribute
     */
    String getAttribute(IName attrName);

    /**
     * Returns a map of element attributes. This method never returns
     * <code>null</code>. Note that the namespace declaration attributes are not
     * returned by the {@link #getAttributes()} method.
     * 
     * @return a map of element attributes
     */
    Map<IName, String> getAttributes();

    /**
     * Returns a child node from the specified position. If the position is out
     * of range then this method returns <code>null</code>.
     * 
     * @param pos the position of the child to return.
     * @return a child node from the specified position.
     */
    IXmlNode getChild(int pos);

    /**
     * Returns the number of children of this element.
     * 
     * @return the number of children of this element
     */
    int getChildCount();

    /**
     * Returns the name of this element.
     * 
     * @return the name of this element
     */
    IName getName();

    /**
     * Removes an attribute with the specified name.
     * 
     * @param attrName the name of the attribute to remove
     * @return <code>true</code> if an attribute with the specified name was
     *         successfully removed
     */
    boolean removeAttribute(IName attrName);

    /**
     * Removes the child node from the specified position.
     * 
     * @param pos the position of the node to remove
     * @return the child node from the specified position.
     */
    IXmlNode removeChild(int pos);

    /**
     * Sets a new element attribute (or replaces an existing attribute with the
     * same name).
     * 
     * @param attrName the name of the attribute to set
     * @param value the attribute value
     */
    void setAttribute(IName attrName, String value);

    /**
     * This method sets new attributes in this element. Note that this method
     * could not be used to set prefix-namespace mapping. To change the
     * namespace mapping the {@link #setNamespaceMapping(Map)}
     * 
     * @param attributes the attributes to set.
     */
    void setAttributes(Map<IName, String> attributes);

    /**
     * Sets the given node in the specified position. The specified position
     * have to be in the [0, size] range where <code>size</code> is the number
     * of already existing children of this element.
     * 
     * @param pos the position where the new child should be added
     * @param node the node to set
     */
    void setChild(int pos, IXmlNode node);

    /**
     * Replaces existing child nodes by nodes from the given list.
     * 
     * @param children a collection of child nodes to set
     */
    void setChildren(Iterable<IXmlNode> children);

}
