/**
 * 
 */
package org.webreformatter.xml;

import java.util.HashMap;
import java.util.Map;

import org.webreformatter.ns.IId;
import org.webreformatter.ns.IName;
import org.webreformatter.ns.NamespaceManager;

/**
 * @author kotelnikov
 */
public abstract class AbstractXmlSerializer extends AbstractSerializer {

    /**
     * Returns the escaped string.
     * 
     * @param str the string to escape
     * @param escapeQuots if this flag is <code>true</code> then "'" and "\""
     *        symbols also will be escaped
     * @return the escaped string.
     */
    public static String escapeXmlString(String str, boolean escapeQuots) {
        if (str == null) {
            return "";
        }
        StringBuffer buf = new StringBuffer();
        char[] array = str.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == '>'
                || array[i] == '&'
                || array[i] == '<'
                || (escapeQuots && (array[i] == '\'' || array[i] == '"'))) {
                buf.append("&#x" + Integer.toHexString(array[i]) + ";");
            } else {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    private boolean fCanonicalForm;

    private NamespaceManager fNamespaceManager;

    private Map<IId, String> fPrefixes = new HashMap<IId, String>();

    public AbstractXmlSerializer(boolean canonical) {
        fCanonicalForm = canonical;
    }

    protected void doSerialize(
        IXmlNode node,
        boolean printPrefixes,
        IXmlNodeAcceptor filter) {
        if (filter != null && !filter.accept(node)) {
            return;
        }
        if (node instanceof IXmlElement) {
            IXmlElement e = (IXmlElement) node;
            printBeginElement(e, printPrefixes);
            for (IXmlNode child : e) {
                doSerialize(child, false, filter);
            }
            printEndElement(e, fPrefixes);
        }
        if (node instanceof IXmlCdata) {
            printCDATA((IXmlCdata) node);
        } else if (node instanceof IXmlText) {
            printText((IXmlText) node);
        } else if (node instanceof IXmlComment) {
            printComment((IXmlComment) node);
        }
    }

    protected void loadNamespacePrefixes(IXmlElement e) {
        IName name = e.getName();
        IId namespace = name.getNamespaceURL();
        String prefix = fNamespaceManager.getNamespacePrefix(namespace, true);
        fPrefixes.put(namespace, prefix);
        Map<IName, String> attributes = e.getAttributes();
        for (IName attrName : attributes.keySet()) {
            namespace = attrName.getNamespaceURL();
            prefix = fNamespaceManager.getNamespacePrefix(namespace, true);
            fPrefixes.put(namespace, prefix);
        }
        for (IXmlNode child : e) {
            if (child instanceof IXmlElement) {
                loadNamespacePrefixes((IXmlElement) child);
            }
        }
    }

    protected abstract void print(String string);

    protected void printBeginElement(IXmlElement e, boolean printPrefixes) {
        print("<");
        IName name = e.getName();
        printName(name, fPrefixes);
        Map<IName, String> attributes = e.getAttributes();
        for (Map.Entry<IName, String> entry : attributes.entrySet()) {
            print(" ");
            IName attrName = entry.getKey();
            String value = entry.getValue();
            printName(attrName, fPrefixes);
            print("='");
            printValue(value);
            print("'");
        }
        if (printPrefixes) {
            for (Map.Entry<IId, String> entry : fPrefixes.entrySet()) {
                print(" ");
                IId namespace = entry.getKey();
                print("xmlns");
                String prefix = entry.getValue();
                if (!"".equals(prefix)) {
                    print(":" + prefix);
                }
                print("='");
                printValue(namespace + "");
                print("'");
            }
        }
        if (!fCanonicalForm && e.getChildCount() == 0) {
            print(" /");
        }
        print(">");
    }

    protected void printCDATA(IXmlCdata node) {
        print("<![CDATA[");
        print(node.getContent());
        print("]]>");
    }

    protected void printComment(IXmlComment node) {
        print("<!--");
        String comment = node.getComment();
        comment = comment.replaceAll("<!--", "<! --").replaceAll("-->", "-- >");
        // if (!comment.startsWith(" ")) {
        // comment = " " + comment;
        // }
        // if (!comment.endsWith(" ")) {
        // comment = comment + " ";
        // }
        print(comment);
        print("-->");
    }

    protected void printEndElement(IXmlElement e, Map<IId, String> prefixes) {
        if (fCanonicalForm || e.getChildCount() > 0) {
            print("</");
            IName name = e.getName();
            printName(name, prefixes);
            print(">");
        }
    }

    protected void printName(IName name, Map<IId, String> prefixes) {
        IId namespace = name.getNamespaceURL();
        String prefix = prefixes.get(namespace);
        if (prefix.length() > 0) {
            print(prefix);
            print(":");
        }
        print(name.getLocalName());
    }

    protected void printText(IXmlText node) {
        String str = node.getContent();
        print(str);
    }

    protected void printValue(String value) {
        value = escapeXmlString(value, true);
        print(value);
    }

    public void serializeNode(IXmlNode e, IXmlNodeAcceptor filter) {
        IXmlDocument document = e.getDocument();
        fNamespaceManager = document.getNamespaceManager();
        try {
            if (e instanceof IXmlElement) {
                loadNamespacePrefixes((IXmlElement) e);
            }
            doSerialize(e, true, filter);
        } finally {
            fNamespaceManager = null;
            fPrefixes.clear();
        }
    }

}
