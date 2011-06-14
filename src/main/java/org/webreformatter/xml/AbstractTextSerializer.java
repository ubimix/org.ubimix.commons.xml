/**
 * 
 */
package org.webreformatter.xml;

/**
 * @author kotelnikov
 */
public abstract class AbstractTextSerializer extends AbstractSerializer {

    private boolean fNormalizeSpaces;

    private boolean fSpace;

    public AbstractTextSerializer() {
        this(true);
    }

    public AbstractTextSerializer(boolean normalizeSpaces) {
        fNormalizeSpaces = normalizeSpaces;
    }

    protected void doSerializeNode(IXmlNode node, IXmlNodeAcceptor filter) {
        if (filter != null && !filter.accept(node))
            return;
        if (node instanceof IXmlElement) {
            IXmlElement e = (IXmlElement) node;
            onBeginElement(e);
            for (IXmlNode child : e) {
                doSerializeNode(child, filter);
            }
            onEndElement(e);
        }
        if (node instanceof IXmlCdata) {
            printCDATA((IXmlCdata) node);
        } else if (node instanceof IXmlText) {
            printText((IXmlText) node);
        }
    }

    private String normalizeSpaces(String content) {
        if (!fNormalizeSpaces)
            return content;
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < content.length(); i++) {
            char ch = content.charAt(i);
            if (ch == ' '
                || ch == '\0'
                || ch == '\n'
                || ch == '\r'
                || ch == '\t') {
                if (!fSpace) {
                    buf.append(" ");
                }
                fSpace = true;
            } else {
                buf.append(ch);
                fSpace = false;
            }
        }
        return buf.toString();
    }

    protected void onBeginElement(IXmlElement e) {
    }

    protected void onEndElement(IXmlElement e) {
    }

    protected abstract void print(String string);

    private void printCDATA(IXmlCdata node) {
        String str = normalizeSpaces(node.getContent());
        print(str);
    }

    private void printText(IXmlText node) {
        String str = normalizeSpaces(node.getContent());
        print(str);
    }

    public void serializeNode(IXmlNode node, IXmlNodeAcceptor filter) {
        doSerializeNode(node, filter);
    }

}
