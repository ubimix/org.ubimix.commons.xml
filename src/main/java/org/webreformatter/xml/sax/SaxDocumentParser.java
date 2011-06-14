/**
 * 
 */
package org.webreformatter.xml.sax;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.webreformatter.ns.IId;
import org.webreformatter.ns.IName;
import org.webreformatter.ns.NamespaceManager;
import org.webreformatter.xml.IXmlDocument;
import org.webreformatter.xml.IXmlDocumentParser;
import org.webreformatter.xml.IXmlElement;
import org.webreformatter.xml.IXmlText;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author kotelnikov
 */
public class SaxDocumentParser implements IXmlDocumentParser {

    private IId fDefaultNamespace;

    private IXmlDocument fDocument;

    private Stack<Map<String, IId>> fFullNsStack = new Stack<Map<String, IId>>();

    private DefaultHandler fInternalHandler = new DefaultHandler() {

        @Override
        public void characters(char[] ch, int start, int length)
            throws SAXException {
            IXmlElement top = !fStack.isEmpty() ? fStack.peek() : null;
            if (top != null) {
                String content = new String(ch, start, length);
                IXmlText text = fDocument.newText(content);
                top.addChild(text);
            }
        }

        /**
         * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
         *      java.lang.String, java.lang.String)
         */
        @Override
        public void endElement(String uri, String localName, String qName)
            throws SAXException {
            Map<String, IId> namespaces = fFullNsStack.pop();
            if (namespaces != null) {
                fRealNsStack.pop();
            }
            IXmlElement element = fStack.pop();
            if (fStack.isEmpty()) {
                fDocument.setRootElement(element);
            }
        }

        private IName getName(String qName) {
            int idx = qName.indexOf(':');
            String prefix = "";
            if (idx >= 0) {
                prefix = qName.substring(0, idx);
                qName = qName.substring(idx + 1);
            }
            if ("xmlns".equals(prefix)
                || ("".equals(prefix) && "xmlns".equals(qName)))
                return null;
            IId namespaceId = null;
            for (int i = fRealNsStack.size() - 1; namespaceId == null && i >= 0; i--) {
                Map<String, IId> map = fRealNsStack.get(i);
                namespaceId = map.get(prefix);
            }
            if (namespaceId == null) {
                namespaceId = fDefaultNamespace;
            }
            IName name = fNamespaceManager.getName(namespaceId, qName);
            return name;
        }

        private Map<String, IId> getNamespaces(Attributes attributes) {
            Map<String, IId> result = null;
            for (int i = 0; i < attributes.getLength(); i++) {
                String attrQName = attributes.getQName(i);
                String prefix = null;
                if ("xmlns".equals(attrQName)) {
                    prefix = "";
                } else if (attrQName.startsWith("xmlns:")) {
                    prefix = attrQName.substring("xmlns:".length());
                }
                if (prefix == null) {
                    continue;
                }
                String namespace = attributes.getValue(i);
                IId id = fNamespaceManager.getId(namespace);
                if (result == null) {
                    result = new HashMap<String, IId>();
                }
                result.put(prefix, id);

                IId existingNs = fNamespaceManager.getNamespaceByPrefix(prefix);
                if (existingNs == null) {
                    fNamespaceManager.setNamespacePrefix(id, prefix);
                }
            }
            return result;
        }

        @Override
        public void startElement(
            String tagNamespaceUri,
            String tagLocalName,
            String tagQName,
            Attributes attributes) throws SAXException {
            Map<String, IId> namespaces = getNamespaces(attributes);
            fFullNsStack.push(namespaces);
            if (namespaces != null) {
                fRealNsStack.push(namespaces);
            }

            IName tagName = getName(tagQName);
            IXmlElement element = fDocument.newElement(tagName);
            for (int i = 0; i < attributes.getLength(); i++) {
                String attrQName = attributes.getQName(i);
                IName attrName = getName(attrQName);
                if (attrName == null) {
                    continue;
                }
                String value = attributes.getValue(i);
                element.setAttribute(attrName, value);
            }
            IXmlElement parent = !fStack.isEmpty() ? fStack.peek() : null;
            if (parent != null) {
                parent.addChild(element);
            }
            fStack.push(element);
        }
    };

    private NamespaceManager fNamespaceManager;

    private Stack<Map<String, IId>> fRealNsStack = new Stack<Map<String, IId>>();

    private Stack<IXmlElement> fStack = new Stack<IXmlElement>();

    /**
     * 
     */
    public SaxDocumentParser() {
    }

    public void parse(IXmlDocument document, IId defaultNamespace, Reader reader) {
        try {
            try {
                fDocument = document;
                fDefaultNamespace = defaultNamespace;
                fNamespaceManager = document.getNamespaceManager();
                SAXParserFactory factory = SAXParserFactory.newInstance();
                InputSource source = new InputSource(reader);
                SAXParser saxParser = factory.newSAXParser();
                saxParser.parse(source, fInternalHandler);
            } finally {
                reader.close();
                fDocument = null;
                fDefaultNamespace = null;
                fNamespaceManager = null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see org.webreformatter.xml.IXmlDocumentParser#parse(org.webreformatter.xml.IXmlDocument,
     *      org.webreformatter.ns.IId, java.lang.String)
     */
    public void parse(IXmlDocument document, IId defaultNamespace, String xml) {
        StringReader reader = new StringReader(xml);
        parse(document, defaultNamespace, reader);
    }

}
