/**
 * 
 */
package org.webreformatter.xml;

/**
 * @author kotelnikov
 */
public class TextSerializer extends AbstractTextSerializer {

    protected StringBuilder fBuilder = new StringBuilder();

    public TextSerializer() {
        super();
    }

    public TextSerializer(boolean normalizeSpaces) {
        super(normalizeSpaces);
    }

    @Override
    protected void print(String string) {
        fBuilder.append(string);
    }

    @Override
    public void serializeNode(IXmlNode node) {
        fBuilder.delete(0, fBuilder.length());
        super.serializeNode(node);
    }

    @Override
    public void serializeNode(IXmlNode node, IXmlNodeAcceptor filter) {
        fBuilder.delete(0, fBuilder.length());
        super.serializeNode(node, filter);
    }

    @Override
    public String toString() {
        return fBuilder.toString();
    }

}
