/**
 * 
 */
package org.webreformatter.xml.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.webreformatter.ns.IName;
import org.webreformatter.xml.IXmlElement;
import org.webreformatter.xml.IXmlNode;

/**
 * @author kotelnikov
 */
public class XmlElement extends XmlNode implements IXmlElement {
    private static final long serialVersionUID = 559003201274494734L;

    private Map<IName, String> fAttributes;

    private List<XmlNode> fChildren;

    private IName fName;

    /**
     * Default constructor used for serialization/deserialization.
     */
    protected XmlElement() {
    }

    /**
     * @param document
     * @param name
     */
    public XmlElement(XmlDocument document, IName name) {
        super(document);
        fName = name;
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#addChild(int,
     *      org.webreformatter.xml.IXmlNode)
     */
    public void addChild(int pos, IXmlNode node) {
        XmlNode n = (XmlNode) node;
        List<XmlNode> list = getChildList(true);
        if (pos < 0 || pos > list.size()) {
            throw new IllegalArgumentException("Index is out of range.");
        }
        n.removeFromParent();
        list.add(pos, n);
        updateChildPos(list, pos);
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#addChild(org.webreformatter.xml.IXmlNode)
     */
    public void addChild(IXmlNode node) {
        int pos = getChildCount();
        addChild(pos, node);
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#addChildren(int,
     *      java.lang.Iterable)
     */
    public void addChildren(int pos, Iterable<IXmlNode> children) {
        List<XmlNode> list = getChildList(true);
        if (pos < 0 || pos > list.size()) {
            throw new IllegalArgumentException("Index is out of range.");
        }
        if (children instanceof XmlElement) {
            XmlElement e = (XmlElement) children;
            if (e.fChildren != null) {
                list.addAll(pos, e.fChildren);
                e.fChildren.clear();
            }
        } else {
            for (IXmlNode node : children) {
                XmlNode n = (XmlNode) node;
                n.removeFromParent();
                list.add(pos, n);
                pos++;
            }
        }
        updateChildPos(list, pos);
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#getAttribute(org.webreformatter.ns.IName)
     */
    public String getAttribute(IName attrName) {
        Map<IName, String> attributes = getAttributesMap(false);
        if (attributes == null) {
            return null;
        }
        return attributes.get(attrName);
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#getAttributes()
     */
    public Map<IName, String> getAttributes() {
        Map<IName, String> attributes = getAttributesMap(false);
        Map<IName, String> result;
        if (attributes != null) {
            result = new HashMap<IName, String>(attributes);
        } else {
            result = new HashMap<IName, String>();
        }
        return result;
    }

    private Map<IName, String> getAttributesMap(boolean create) {
        if (fAttributes == null && create) {
            fAttributes = new LinkedHashMap<IName, String>();
        }
        return fAttributes;
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#getChild(int)
     */
    public IXmlNode getChild(int pos) {
        List<XmlNode> childList = getChildList(false);
        int len = childList != null ? childList.size() : 0;
        if (pos < 0 || pos >= len) {
            throw new IllegalArgumentException("Index is out of range.");
        }
        return childList.get(pos);
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#getChildCount()
     */
    public int getChildCount() {
        List<XmlNode> list = getChildList(false);
        int len = list != null ? list.size() : 0;
        return len;
    }

    private List<XmlNode> getChildList(boolean create) {
        if (fChildren == null && create) {
            fChildren = new ArrayList<XmlNode>();
        }
        return fChildren;
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#getName()
     */
    public IName getName() {
        return fName;
    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<IXmlNode> iterator() {
        List<IXmlNode> list = null;
        if (fChildren != null) {
            list = new ArrayList<IXmlNode>(fChildren);
        } else {
            list = new ArrayList<IXmlNode>();
        }
        Iterator<IXmlNode> iterator = list.iterator();
        return iterator;
    }

    public boolean removeAttribute(IName attrName) {
        Map<IName, String> attributes = getAttributesMap(false);
        if (attributes == null) {
            return false;
        }
        boolean hasAttribute = attributes.containsKey(attrName);
        attributes.remove(attrName);
        return hasAttribute;
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#removeChild(int)
     */
    public IXmlNode removeChild(int pos) {
        List<XmlNode> list = getChildList(false);
        if (list == null) {
            return null;
        }
        XmlNode child = null;
        if (pos >= 0 && pos < list.size()) {
            child = list.remove(pos);
            child.setParent(null);
            child.setPos(0);
            updateChildPos(list, pos);
        }
        return child;
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#setAttribute(org.webreformatter.ns.IName,
     *      java.lang.String)
     */
    public void setAttribute(IName attrName, String value) {
        Map<IName, String> attributes = getAttributesMap(true);
        attributes.put(attrName, value);
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#setAttributes(java.util.Map)
     */
    public void setAttributes(Map<IName, String> attributes) {
        Map<IName, String> map = getAttributesMap(true);
        map.clear();
        map.putAll(attributes);
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#setChild(int,
     *      org.webreformatter.xml.IXmlNode)
     */
    public void setChild(int pos, IXmlNode node) {
        List<XmlNode> list = getChildList(true);
        int len = list != null ? list.size() : 0;
        if (pos < 0 || pos > len) {
            throw new IllegalArgumentException("Index is out of range.");
        }
        XmlNode n = (XmlNode) node;
        n.removeFromParent();
        list.add(pos, n);
        updateChildPos(list, pos);
    }

    /**
     * @see org.webreformatter.xml.IXmlElement#setChildren(java.lang.Iterable)
     */
    public void setChildren(Iterable<IXmlNode> children) {
        if (children == this) {
            return;
        }
        List<XmlNode> list = getChildList(true);
        for (XmlNode child : list) {
            child.setParent(null);
            child.setPos(0);
        }
        list.clear();

        if (children != null) {
            if (children instanceof XmlElement) {
                XmlElement e = (XmlElement) children;
                if (e.fChildren != null) {
                    list.addAll(e.fChildren);
                    e.fChildren.clear();
                }
            } else {
                for (IXmlNode node : children) {
                    XmlNode n = (XmlNode) node;
                    n.removeFromParent();
                    list.add(n);
                }
            }
            updateChildPos(list, 0);
        }
    }

    private void updateChildPos(List<XmlNode> list, int startPos) {
        for (int i = startPos; i < list.size(); i++) {
            XmlNode child = list.get(i);
            child.setParent(this);
            child.setPos(i);
        }
    }

}
