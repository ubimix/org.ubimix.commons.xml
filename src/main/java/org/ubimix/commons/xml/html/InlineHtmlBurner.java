/**
 * 
 */
package org.ubimix.commons.xml.html;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.ubimix.commons.xml.XHTMLUtils;

/**
 * @author kotelnikov
 */
public class InlineHtmlBurner {

    protected static String getHtmlName(Attr attr) {
        return XHTMLUtils.getHTMLName(attr);
    }

    protected static String getHtmlName(Element e) {
        return XHTMLUtils.getHTMLName(e);
    }

    protected static boolean isEmpty(String str) {
        return "".equals(str);
    }

    protected static String reduceText(String txtStr, boolean keepSpaces) {
        if (keepSpaces) {
            return txtStr;
        }
        return txtStr.replaceAll("\\s+", " ");
    }

    protected static String reduceText(StringBuilder buf, boolean keepSpaces) {
        String result = buf.toString();
        result = reduceText(result, keepSpaces);
        buf.delete(0, buf.length());
        return result;
    }

    public static void updateStat(Node child, TagStat stat) {
        if (child instanceof Element) {
            Element e = (Element) child;
            String name = getHtmlName(e);
            if (TagDictionary.isBlockElement(name)) {
                stat.incBlockElements();
            } else if (TagDictionary.isInlineElement(name)) {
                if (TagDictionary.isLineBreak(name)) {
                    stat.incLineBreakElements();
                } else {
                    stat.incInlineElements();
                }
            } else {
                stat.incXmlElements();
            }
        } else if (child instanceof Text) {
            Text text = (Text) child;
            String str = text.getData();
            str = reduceText(str, false);
            str = str.trim();
            if (isEmpty(str)) {
                stat.incSpaceBlocks();
            } else {
                stat.incTextBlocks();
            }
        } else {
            stat.incOtherXmlNodes();
        }
    }

    protected List<Node> burnInlineNodes(
        Element tag,
        List<Node> list,
        boolean keepSpaces) {
        List<Node> newList = new ArrayList<Node>();
        TagStat stat = new TagStat();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            Node node = list.get(i);
            if (node instanceof Text) {
                Text text = (Text) node;
                String str = getText(text);
                buf.append(str);
            } else {
                String str = reduceText(buf, keepSpaces);
                Text text = flushText(tag, str);
                if (text != null) {
                    updateStat(text, stat);
                    newList.add(text);
                }
                updateStat(node, stat);
                newList.add(node);
            }
        }
        String str = reduceText(buf, keepSpaces);
        Text text = flushText(tag, str);
        if (text != null) {
            updateStat(text, stat);
            newList.add(text);
        }

        if (!keepSpaces
            && !isTextflowContainer(tag)
            || stat.getBlockElements() > 0) {
            // Wrap all text blocks in paragraphs
            newList = wrapTextInParagraphs(tag, newList);
        }
        return newList;
    }

    public TagStat burnInlineTag(Element tag, boolean keepSpaces) {
        String tagName = getHtmlName(tag);
        keepSpaces |= TagDictionary.keepSpaces(tagName);
        removeUnusedAttributes(tag);
        List<Node> list = burnInlineNodes(tag, getChildren(tag), keepSpaces);
        TagStat result = new TagStat();
        removeAllChildren(tag);
        for (Node node : list) {
            tag.appendChild(node);
            updateStat(node, result);
        }
        return result;
    }

    private Text flushText(Element tag, String str) {
        Text text = null;
        Document doc = tag.getOwnerDocument();
        if (str.length() > 0) {
            text = doc.createTextNode(str);
        }
        return text;
    }

    protected Text flushText(Element tag, StringBuilder buf) {
        String str = buf.toString();
        buf.delete(0, str.length());
        return flushText(tag, str);
    }

    protected List<Node> getChildren(Element tag) {
        List<Node> result = new ArrayList<Node>();
        Node child = tag.getFirstChild();
        while (child != null) {
            result.add(child);
            child = child.getNextSibling();
        }
        return result;
    }

    protected String getText(Text text) {
        return text.getData();
    }

    protected boolean isExcludedAttribute(String name, Attr attr) {
        name = name.toLowerCase();
        if (name.startsWith("on")) {
            // Remove all handlers
            return true;
        }
        if (TagDictionary.isImportantAttribute(name)) {
            // Keep important HTML attributes
            return false;
        }
        if (TagDictionary.isHtmlAttribute(name)) {
            // Remove all non-important HTML attributes
            return true;
        }
        // Remove other attributes
        return true;
    }

    protected boolean isTextflowContainer(Element tag) {
        String name = getHtmlName(tag);
        if (!TagDictionary.isHtmlElement(name)) {
            // A simple XML node can be used as a container of in-line elements.
            return true;
        }
        boolean result = TagDictionary.isTextflowContainer(name)
            || TagDictionary.isInlineElement(name);
        return result;
    }

    protected boolean isTextflowElement(Element e) {
        String name = getHtmlName(e);
        boolean result;
        if (!TagDictionary.isHtmlElement(name)) {
            // No HTML name means that it is a simple XML node.
            // A simple XML node can appear between in-line elements.
            result = true;
        } else {
            result = TagDictionary.isInlineElement(name);
        }
        return result;
    }

    protected boolean isTextflowNode(Node node) {
        if (node instanceof Text || node instanceof Entity) {
            return true;
        }
        if (node instanceof Document) {
            return false;
        }
        boolean result = false;
        if (node instanceof Element) {
            result = isTextflowElement((Element) node);
        }
        return result;
    }

    protected Element newHtmlElement(Node node, String tagName) {
        Document doc = node.getOwnerDocument();
        Element result = null;
        String namespace = node.getNamespaceURI();
        if (namespace != null) {
            result = doc.createElementNS(namespace, tagName);
        } else {
            result = doc.createElement(tagName);
        }
        return result;
    }

    protected void removeAllChildren(Element e) {
        Node child = e.getFirstChild();
        while (child != null) {
            Node next = child.getNextSibling();
            e.removeChild(child);
            child = next;
        }
    }

    protected void removeUnusedAttributes(Element e) {
        NamedNodeMap attributes = e.getAttributes();
        for (int i = attributes.getLength() - 1; i >= 0; i--) {
            Attr attr = (Attr) attributes.item(i);
            String name = getHtmlName(attr);
            if (isExcludedAttribute(name, attr)) {
                e.removeAttributeNode(attr);
            }
        }
    }

    protected List<Node> wrapTextInParagraphs(Element tag, List<Node> list) {
        List<Node> result = new ArrayList<Node>();
        for (int i = 0; i < list.size();) {
            Node node = list.get(i);
            boolean inline = isTextflowNode(node);
            if (inline) {
                TagStat stat = new TagStat();
                Element paragraph = newHtmlElement(tag, TagDictionary.P);
                updateStat(node, stat);
                paragraph.appendChild(node);
                for (i++; i < list.size();) {
                    Node nextNode = list.get(i);
                    if (!isTextflowNode(nextNode)) {
                        break;
                    }
                    updateStat(nextNode, stat);
                    paragraph.appendChild(nextNode);
                    i++;
                }
                if (stat.getAllMeaningfulNodes() > 0) {
                    result.add(paragraph);
                }
            } else {
                i++;
                result.add(node);
            }
        }
        return result;
    }

}
