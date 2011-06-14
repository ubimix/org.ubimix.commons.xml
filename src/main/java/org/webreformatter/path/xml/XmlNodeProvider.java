/**
 * 
 */
package org.webreformatter.path.xml;

import java.util.Iterator;

import org.webreformatter.path.INodeProvider;
import org.webreformatter.xml.IXmlElement;
import org.webreformatter.xml.IXmlNode;

/**
 * @author kotelnikov
 */
public class XmlNodeProvider implements INodeProvider {

    /**
     * 
     */
    public XmlNodeProvider() {
    }

    /**
     * @see org.webreformatter.path.INodeProvider
     *      <T>#getChildren(java.lang.Object)
     */
    public Iterator<?> getChildren(final Object parent) {
        if (!(parent instanceof IXmlElement))
            return null;
        final IXmlElement element = (IXmlElement) parent;
        final int len = element.getChildCount();
        if (len == 0)
            return null;
        return new Iterator<IXmlNode>() {

            private int fPos;

            public boolean hasNext() {
                return fPos < len;
            }

            public IXmlNode next() {
                if (!hasNext())
                    return null;
                IXmlNode child = element.getChild(fPos);
                fPos++;
                return child;
            }

            public void remove() {
            }

        };
    }

}
