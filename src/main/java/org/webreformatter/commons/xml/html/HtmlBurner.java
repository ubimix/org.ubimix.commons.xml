/**
 * 
 */
package org.webreformatter.commons.xml.html;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.webreformatter.commons.xml.XHTMLUtils;

/**
 * @author kotelnikov
 */
public class HtmlBurner {

    /**
     * @author kotelnikov
     */
    public static class TagStat {

        private int fBlockElements;

        private int fInlineElements;

        private int fLineBreakElements;

        private int fOtherXmlNodes;

        private int fSpaceBlocks;

        private int fTextBlocks;

        private int fXmlElements;

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof TagStat)) {
                return false;
            }
            TagStat o = (TagStat) obj;
            int[] array1 = toArray();
            int[] array2 = o.toArray();
            for (int i = 0; i < array1.length; i++) {
                if (array1[i] != array2[i]) {
                    return false;
                }
            }
            return true;
        }

        public int getAllElements() {
            return fBlockElements + fInlineElements + fXmlElements;
        }

        public int getAllMeaningfulNodes() {
            return getAllElements() + fOtherXmlNodes + fTextBlocks;
        }

        public int getAllTextBlocks() {
            return fTextBlocks + fSpaceBlocks;
        }

        protected int getBlockElements() {
            return fBlockElements;
        }

        protected int getInlineElements() {
            return fInlineElements;
        }

        protected int getLineBreakElements() {
            return fLineBreakElements;
        }

        protected int getOtherXmlNodes() {
            return fOtherXmlNodes;
        }

        protected int getSpaceBlocks() {
            return fSpaceBlocks;
        }

        protected int getTextBlocks() {
            return fTextBlocks;
        }

        protected int getXmlElements() {
            return fXmlElements;
        }

        @Override
        public int hashCode() {
            int hashCode = 1;
            int[] array = toArray();
            for (int value : array) {
                hashCode = 31 * hashCode + value;
            }
            return hashCode;
        }

        public void incBlockElements() {
            fBlockElements++;
        }

        public void incInlineElements() {
            fInlineElements++;
        }

        public void incLineBreakElements() {
            fLineBreakElements++;
        }

        public void incOtherXmlNodes() {
            fOtherXmlNodes++;
        }

        public void incSpaceBlocks() {
            fSpaceBlocks++;
        }

        public void incTextBlocks() {
            fTextBlocks++;
        }

        public void incXmlElements() {
            fXmlElements++;
        }

        private int[] toArray() {
            int[] array = {
                fBlockElements,
                fInlineElements,
                fLineBreakElements,
                fOtherXmlNodes,
                fSpaceBlocks,
                fTextBlocks,
                fXmlElements };
            return array;
        }

        @Override
        public String toString() {
            return "{\n"
                + "  \"blockElements\":"
                + fBlockElements
                + ",\n"
                + "  \"inlineElements\":"
                + fInlineElements
                + ",\n"
                + "  \"lineBreakElements\":"
                + fLineBreakElements
                + ",\n"
                + "  \"otherXmlNodes\":"
                + fOtherXmlNodes
                + ",\n"
                + "  \"spaceBlocks\":"
                + fSpaceBlocks
                + ",\n"
                + "  \"textBlocks\":"
                + fTextBlocks
                + ",\n"
                + "  \"xmlElements\":"
                + fXmlElements
                + "\n"
                + "}";
        }

        private void updateStat(Node child) {
            if (child instanceof Element) {
                Element e = (Element) child;
                String name = getHtmlName(e);
                if (TagDictionary.isBlockElement(name)) {
                    incBlockElements();
                } else if (TagDictionary.isInlineElement(name)) {
                    if (TagDictionary.isLineBreak(name)) {
                        incLineBreakElements();
                    } else {
                        incInlineElements();
                    }
                } else {
                    incXmlElements();
                }
            } else if (child instanceof Text) {
                Text text = (Text) child;
                String str = text.getData();
                str = reduceText(str, false);
                str = str.trim();
                if (isEmpty(str)) {
                    incSpaceBlocks();
                } else {
                    incTextBlocks();
                }
            } else {
                incOtherXmlNodes();
            }
        }

    }

    private static String getHtmlName(Attr attr) {
        return XHTMLUtils.getHTMLName(attr);
    }

    private static String getHtmlName(Element e) {
        return XHTMLUtils.getHTMLName(e);
    }

    private static boolean isEmpty(String str) {
        return "".equals(str);
    }

    private static String reduceText(String txtStr, boolean keepSpaces) {
        if (keepSpaces) {
            return txtStr;
        }
        return txtStr.replaceAll("\\s+", " ");
    }

    private static String reduceText(StringBuilder buf, boolean keepSpaces) {
        String result = buf.toString();
        result = reduceText(result, keepSpaces);
        buf.delete(0, buf.length());
        return result;
    }

    public HtmlBurner() {
    }

    public void burnHtml(Element tag) {
        burnTag(tag, false);
    }

    private List<Node> burnInlineNodes(
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
                    stat.updateStat(text);
                    newList.add(text);
                }
                stat.updateStat(node);
                newList.add(node);
            }
        }
        String str = reduceText(buf, keepSpaces);
        Text text = flushText(tag, str);
        if (text != null) {
            stat.updateStat(text);
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

    private List<Node> burnNodes(
        Element tag,
        List<Node> nodes,
        boolean keepSpaces) {
        List<Node> result = new ArrayList<Node>();
        StringBuilder buf = new StringBuilder();
        for (Node node : nodes) {
            if (node instanceof Text) {
                Text txt = (Text) node;
                String txtStr = getText(txt);
                buf.append(txtStr);
            } else if (node instanceof Element) {
                Element childTag = (Element) node;
                String childName = getHtmlName(childTag);
                if (!isExcludedTag(childName, childTag)) {
                    Text text = flushText(tag, buf);
                    if (text != null) {
                        result.add(text);
                    }
                    TagStat childStat = burnTag(childTag, keepSpaces);
                    if (keepSpaces) {
                        result.add(childTag);
                        continue;
                    }
                    // Special cases
                    if (keepIntact(childName, childTag)) {
                        result.add(childTag);
                    } else if (isExcludedContainer(childName, childTag)) {
                        List<Node> childList = getChildren(childTag);
                        childList = burnNodes(tag, childList, keepSpaces);
                        result.addAll(childList);
                    } else {
                        if (TagDictionary.isDivElement(childName)) {
                            if (childStat.getAllMeaningfulNodes() == 1) {
                                List<Node> childList = getChildren(childTag);
                                if (childStat.getTextBlocks() == 1) {
                                    childList = wrapTextInParagraphs(
                                        childTag,
                                        childList);
                                }
                                result.addAll(childList);
                            } else if (childStat.getAllMeaningfulNodes() > 1) {
                                result.add(childTag);
                            }
                        } else if (TagDictionary.isInlineElement(childName)) {
                            if (TagDictionary.isSpanElement(childName)) {
                                List<Node> childList = getChildren(childTag);
                                result.addAll(childList);
                            } else {
                                childTag = checkBlockTags(
                                    childTag,
                                    childStat,
                                    TagDictionary.DIV);
                                if (childTag != null) {
                                    result.add(childTag);
                                }
                            }
                        } else if (TagDictionary.isParagraph(childName)
                            || TagDictionary.isHeader(childName)) {
                            childTag = checkBlockTags(
                                childTag,
                                childStat,
                                TagDictionary.DIV);
                            if (childTag != null) {
                                result.add(childTag);
                            }
                        } else if (TagDictionary.isList(childName)
                            || TagDictionary.isListItem(childName)) {
                            childTag = checkBlockTags(
                                childTag,
                                childStat,
                                TagDictionary.DIV);
                            if (childTag != null) {
                                result.add(childTag);
                            }
                        } else if (TagDictionary.isTableElement(childName)) {
                            Element singleCell = getTableSingleCell(childTag);
                            if (singleCell != null) {
                                List<Node> childList = getChildren(singleCell);
                                result.addAll(childList);
                            } else {
                                result.add(childTag);
                            }
                        } else {
                            result.add(childTag);
                        }
                    }
                }
            } else if (!(node instanceof Comment)) {
                Text text = flushText(tag, buf);
                if (text != null) {
                    result.add(text);
                }
                result.add(node);
            }
        }
        Text text = flushText(tag, buf);
        if (text != null) {
            result.add(text);
        }

        result = burnInlineNodes(tag, result, keepSpaces);
        return result;
    }

    private TagStat burnTag(Element tag, boolean keepSpaces) {
        String tagName = getHtmlName(tag);
        keepSpaces |= TagDictionary.keepSpaces(tagName);
        removeUnusedAttributes(tag);
        List<Node> list = burnNodes(tag, getChildren(tag), keepSpaces);
        TagStat result = new TagStat();
        removeAllChildren(tag);
        for (Node node : list) {
            tag.appendChild(node);
            result.updateStat(node);
        }
        return result;
    }

    private Element checkBlockTags(
        Element tag,
        TagStat tagStat,
        String newTagName) {
        String childName = getHtmlName(tag);
        Element result = null;
        if (TagDictionary.isEmptyElement(childName)
            || tagStat.getAllMeaningfulNodes() > 0) {
            if (TagDictionary.isEmptyElement(childName)
                || tagStat.getBlockElements() == 0) {
                result = tag;
            } else {
                Element newParent = newHtmlElement(tag, newTagName);
                Node child = tag.getFirstChild();
                while (child != null) {
                    Node nextChild = child.getNextSibling();
                    newParent.appendChild(child);
                    child = nextChild;
                }
                result = newParent;
            }
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

    private Text flushText(Element tag, StringBuilder buf) {
        String str = buf.toString();
        buf.delete(0, str.length());
        return flushText(tag, str);
    }

    private List<Node> getChildren(Element tag) {
        List<Node> result = new ArrayList<Node>();
        Node child = tag.getFirstChild();
        while (child != null) {
            result.add(child);
            child = child.getNextSibling();
        }
        return result;
    }

    private List<Element> getTableRowCells(Element row) {
        List<Element> result = new ArrayList<Element>();
        Node child = row.getFirstChild();
        while (child != null) {
            if (child instanceof Element) {
                Element e = (Element) child;
                String name = getHtmlName(e);
                if (TagDictionary.isTableCellElement(name)) {
                    result.add(e);
                }
            }
            child = child.getNextSibling();
        }
        return result;
    }

    private List<Element> getTableRows(Element parent) {
        List<Element> result = new ArrayList<Element>();
        Node child = parent.getFirstChild();
        while (child != null) {
            if (child instanceof Element) {
                Element e = (Element) child;
                String name = getHtmlName(e);
                if (TagDictionary.isTableHeaderBodyOrRowElement(name)) {
                    if (TagDictionary.isTableRow(name)) {
                        result.add(e);
                    } else {
                        List<Element> rows = getTableRows(e);
                        result.addAll(rows);
                    }
                }
            }
            child = child.getNextSibling();
        }
        return result;
    }

    /**
     * This method detects if the given table has just one cell. If it is the
     * case then it returns a reference to the cell; otherwise this method
     * returns <code>null</code>.
     * 
     * @param table the table element
     * @return the single cell of the specified table
     */
    private Element getTableSingleCell(Element table) {
        Element cell = null;
        int cellCount = 0;
        List<Element> rows = getTableRows(table);
        for (Element row : rows) {
            List<Element> cells = getTableRowCells(row);
            cellCount += cells.size();
            if (!cells.isEmpty() && cell == null) {
                cell = cells.get(0);
            }
        }
        if (cellCount == 0) {
            // Create and return a fake cell with no content
            cell = newHtmlElement(table, TagDictionary.TD);
        } else if (cellCount > 1) {
            // Don't replace tables with more than one cells.
            cell = null;
        }
        return cell;
    }

    private String getText(Text text) {
        return text.getData();
    }

    private boolean hasId(Element tag) {
        return tag.hasAttribute(TagDictionary.ATTR_ID);
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

    protected boolean isExcludedContainer(String name, Element element) {
        return TagDictionary.isFormElement(name)
            || "font".equals(name)
            || "center".equals(name)
            || TagDictionary.BODY.equals(name);
    }

    protected boolean isExcludedTag(String name, Element element) {
        return TagDictionary.HEAD.equals(name)
            || TagDictionary.isFormElement(name)
            || TagDictionary.isFormContentElement(name)
            || TagDictionary.IFRAME.equals(name)
            || TagDictionary.LABEL.equals(name)
            || TagDictionary.NOSCRIPT.equals(name)
            || TagDictionary.OPTION.equals(name)
            || TagDictionary.SCRIPT.equals(name)
            || TagDictionary.STYLE.equals(name);
    }

    private boolean isTextflowContainer(Element tag) {
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

    private boolean isTextflowNode(Node node) {
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

    protected boolean keepIntact(String name, Element element) {
        return false;
    }

    private Element newHtmlElement(Node node, String tagName) {
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

    private void removeAllChildren(Element e) {
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

    private List<Node> wrapTextInParagraphs(Element tag, List<Node> list) {
        List<Node> result = new ArrayList<Node>();
        for (int i = 0; i < list.size();) {
            Node node = list.get(i);
            boolean inline = isTextflowNode(node);
            if (inline) {
                TagStat stat = new TagStat();
                Element paragraph = newHtmlElement(tag, TagDictionary.P);
                stat.updateStat(node);
                paragraph.appendChild(node);
                for (i++; i < list.size();) {
                    Node nextNode = list.get(i);
                    if (!isTextflowNode(nextNode)) {
                        break;
                    }
                    stat.updateStat(nextNode);
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
