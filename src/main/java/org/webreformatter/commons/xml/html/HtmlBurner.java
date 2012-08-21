/**
 * 
 */
package org.webreformatter.commons.xml.html;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * @author kotelnikov
 */
public class HtmlBurner extends InlineHtmlBurner {

    public HtmlBurner() {
    }

    public void burnHtml(Element tag) {
        burnTag(tag, false);
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
                    TagStat childStat = null;
                    if (keepIntact(childName, childTag)) {
                        result.add(childTag);
                        continue;
                    } else {
                        childStat = burnTag(childTag, keepSpaces);
                        if (keepSpaces) {
                            result.add(childTag);
                            continue;
                        }
                    }

                    // Special cases
                    if (isExcludedContainer(childName, childTag)) {
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
            updateStat(node, result);
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

    protected boolean keepIntact(String name, Element element) {
        return false;
    }

}
