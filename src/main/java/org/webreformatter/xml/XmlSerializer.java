/**
 * 
 */
package org.webreformatter.xml;

/**
 * @author kotelnikov
 */
public class XmlSerializer extends AbstractXmlSerializer {

    protected StringBuilder fBuilder = new StringBuilder();

    public XmlSerializer(boolean canonical) {
        super(canonical);
    }

    @Override
    protected void print(String string) {
        fBuilder.append(string);
    }

    @Override
    public void serializeNode(IXmlNode e, IXmlNodeAcceptor filter) {
        fBuilder.delete(0, fBuilder.length());
        super.serializeNode(e, filter);
    }

    @Override
    public String toString() {
        return fBuilder.toString();
    }

}
