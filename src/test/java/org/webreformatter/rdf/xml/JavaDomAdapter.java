/**
 * 
 */
package org.webreformatter.rdf.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.webreformatter.ns.IId;
import org.webreformatter.ns.IName;
import org.webreformatter.ns.NamespaceManager;
import org.webreformatter.xml.delegate.IDelegateAdapter;

/**
 * @author kotelnikov
 */
public class JavaDomAdapter implements IDelegateAdapter {

    private static final String KEY_CDATA = "_d";

    private static final String KEY_CHILDREN = "_c";

    private static final String KEY_COMMENT = "_x";

    private static final String KEY_NAME = "_";

    private static final String KEY_NS = "_ns";

    private static final String KEY_ROOT = "_r";

    private static final String KEY_TEXT = "_t";

    /**
     * 
     */
    public JavaDomAdapter() {
    }

    /**
     * @see org.webreformatter.xml.delegate.IDelegateAdapter#appendChild(java.lang.Object,
     *      java.lang.Object)
     */
    public void appendChild(Object parent, Object child) {
        Map<String, Object> map = cast(parent);
        List<Object> children = getChildList(map, true);
        children.add(child);
    }

    @SuppressWarnings("unchecked")
    private <T> T cast(Object parent) {
        return (T) parent;
    }

    private Object clone(
        NamespaceManager oldNsManager,
        Object oldDocument,
        NamespaceManager newNsManager,
        Object newDocument,
        Object nodeToCopy) {
        Object clone = null;
        if (isElement(nodeToCopy)) {
            IName name = getElementName(oldNsManager, oldDocument, nodeToCopy);
            clone = createElement(newNsManager, newDocument, name);
            Map<IName, String> attributes = getAttributes(
                oldNsManager,
                oldDocument,
                nodeToCopy);
            setAttributes(newNsManager, newDocument, clone, attributes);
            int childCount = getChildNumber(nodeToCopy);
            for (int i = 0; i < childCount; i++) {
                Object child = getChild(nodeToCopy, i);
                Object childClone = clone(
                    oldNsManager,
                    oldDocument,
                    newNsManager,
                    newDocument,
                    child);
                appendChild(clone, childClone);
            }
        } else if (isCDATASection(nodeToCopy)) {
            String text = getTextContent(nodeToCopy);
            clone = createCDATASection(newDocument, text);
        } else if (isText(nodeToCopy)) {
            String text = getTextContent(nodeToCopy);
            clone = createTextNode(newDocument, text);
        } else if (isComment(nodeToCopy)) {
            String text = getTextContent(nodeToCopy);
            clone = createComment(newDocument, text);
        }
        return clone;
    }

    /**
     * @see org.webreformatter.xml.delegate.IDelegateAdapter#createCDATASection(java.lang.Object,
     *      java.lang.String)
     */
    public Object createCDATASection(Object document, String content) {
        Map<String, Object> map = newMap();
        map.put(KEY_CDATA, content);
        return map;
    }

    /**
     * @see org.webreformatter.xml.delegate.IDelegateAdapter#createComment(java.lang.Object,
     *      java.lang.String)
     */
    public Object createComment(Object document, String content) {
        Map<String, Object> map = newMap();
        map.put(KEY_COMMENT, content);
        return map;
    }

    public Object createElement(
        NamespaceManager manager,
        Object document,
        IName name) {
        Map<String, Object> docMap = cast(document);
        Map<String, Object> nsMap = getNsMap(docMap, true);
        String qualifiedName = getQualifiedName(manager, nsMap, name, true);
        Map<String, Object> elementMap = newMap();
        elementMap.put(KEY_NAME, qualifiedName);
        return elementMap;
    }

    /**
     * @see org.webreformatter.xml.delegate.IDelegateAdapter#createTextNode(java.lang.Object,
     *      java.lang.String)
     */
    public Object createTextNode(Object document, String content) {
        Map<String, Object> map = newMap();
        map.put(KEY_TEXT, content);
        return map;
    }

    public String getAttribute(NamespaceManager manager, Object document,

    Object element, IName attrName) {
        Map<String, Object> docMap = cast(document);
        Map<String, Object> nsMap = getNsMap(docMap, false);
        if (nsMap == null) {
            return null;
        }
        String qualifiedName = getQualifiedName(manager, nsMap, attrName, false);
        Map<String, Object> map = cast(element);
        return cast(map.get(qualifiedName));
    }

    public Map<IName, String> getAttributes(
        NamespaceManager manager,
        Object document,
        Object element) {
        Map<String, Object> map = cast(element);
        Map<String, Object> docMap = cast(document);
        Map<String, Object> nsMap = getNsMap(docMap, false);
        Map<IName, String> result = new HashMap<IName, String>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String qualifiedName = entry.getKey();
            if (isSystemAttr(qualifiedName)) {
                continue;
            }
            IName attrName = getName(manager, nsMap, qualifiedName);
            String value = cast(entry.getValue());
            result.put(attrName, value);
        }
        return result;
    }

    /**
     * @see org.webreformatter.xml.delegate.IDelegateAdapter#getChild(java.lang.Object,
     *      int)
     */
    public Object getChild(Object node, int pos) {
        Map<String, Object> map = cast(node);
        List<Object> list = getChildList(map, false);
        int len = list != null ? list.size() : 0;
        return pos >= 0 && pos < len ? list.get(pos) : null;
    }

    private List<Object> getChildList(Map<String, Object> map, boolean create) {
        List<Object> list = cast(map.get(KEY_CHILDREN));
        if (list == null && create) {
            list = new ArrayList<Object>();
            map.put(KEY_CHILDREN, list);
        }
        return list;
    }

    /**
     * @see org.webreformatter.xml.delegate.IDelegateAdapter#getChildNumber(java.lang.Object)
     */
    public int getChildNumber(Object node) {
        Map<String, Object> map = cast(node);
        List<Object> list = getChildList(map, false);
        return list != null ? list.size() : 0;
    }

    /**
     * @see org.webreformatter.xml.delegate.IDelegateAdapter#getDocumentElement(java.lang.Object)
     */
    public Object getDocumentElement(Object document) {
        Map<String, Object> map = cast(document);
        return map.get(KEY_ROOT);
    }

    /**
     * @see org.webreformatter.xml.delegate.IDelegateAdapter#getElementName(NamespaceManager,
     *      Object, java.lang.Object)
     */
    public IName getElementName(
        NamespaceManager manager,
        Object document,
        Object element) {
        Map<String, Object> map = cast(element);
        String qualifiedName = cast(map.get(KEY_NAME));
        Map<String, Object> docMap = cast(document);
        Map<String, Object> nsMap = getNsMap(docMap, false);
        IName name = getName(manager, nsMap, qualifiedName);
        return name;
    }

    private IName getName(
        NamespaceManager manager,
        Map<String, Object> nsMap,
        String qualifiedName) {
        int idx = qualifiedName.indexOf(":");
        String prefix = "";
        String localName = qualifiedName;
        if (idx > 0) {
            prefix = qualifiedName.substring(0, idx);
            localName = qualifiedName.substring(idx + 1);
        }
        String uri = (String) (nsMap != null ? cast(nsMap.get(prefix)) : "");
        if (uri == null) {
            uri = "";
        }
        IId namespaceURI = manager.getId(uri);
        IName name = manager.getName(namespaceURI, localName);
        return name;
    }

    private String getNamespacePrefix(
        NamespaceManager manager,
        Map<String, Object> nsMap,
        IName name,
        boolean create) {
        String prefix = null;
        IId namespaceURI = name.getNamespaceURL();
        String str = !isEmpty(namespaceURI) ? namespaceURI.toString() : "";
        for (Map.Entry<String, Object> entry : nsMap.entrySet()) {
            String namespace = cast(entry.getValue());
            if (str.equals(namespace)) {
                prefix = entry.getKey();
                break;
            }
        }
        if (prefix == null && create) {
            prefix = manager.getPrefix(name);
            if (prefix != null) {
                nsMap.put(prefix, str);
            }
        }
        return prefix;
    }

    private Map<String, Object> getNsMap(
        Map<String, Object> docMap,
        boolean create) {
        Map<String, Object> nsMap = cast(docMap.get(KEY_NS));
        if (nsMap == null && create) {
            nsMap = newMap();
            docMap.put(KEY_NS, nsMap);
        }
        return nsMap;
    }

    private String getQualifiedName(
        NamespaceManager manager,
        Map<String, Object> nsMap,
        IName name,
        boolean create) {
        String prefix = getNamespacePrefix(manager, nsMap, name, create);
        String qualifiedName = name.getLocalName();
        if (!isEmpty(prefix)) {
            qualifiedName = prefix + ":" + qualifiedName;
        }
        return qualifiedName;
    }

    /**
     * @see org.webreformatter.xml.delegate.IDelegateAdapter#getTextContent(java.lang.Object)
     */
    public String getTextContent(Object node) {
        Map<String, Object> map = cast(node);
        String str = cast(map.get(KEY_TEXT));
        if (str == null) {
            str = cast(map.get(KEY_CDATA));
        }
        if (str == null) {
            str = cast(map.get(KEY_COMMENT));
        }
        return str;
    }

    /**
     * @see org.webreformatter.xml.delegate.IDelegateAdapter#importNode(NamespaceManager,
     *      Object, NamespaceManager, java.lang.Object, java.lang.Object)
     */
    public Object importNode(
        NamespaceManager oldNsManager,
        Object sourceDocument,
        NamespaceManager targetNsManager,
        Object targetDocument,
        Object nodeToCopy) {
        Object clone = clone(
            oldNsManager,
            sourceDocument,
            targetNsManager,
            targetDocument,
            nodeToCopy);
        return clone;
    }

    /**
     * @see org.webreformatter.xml.delegate.IDelegateAdapter#insertBefore(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public void insertBefore(Object parent, Object newChild, Object refChild) {
        Map<String, Object> map = cast(parent);
        List<Object> list = getChildList(map, true);
        int pos = list.indexOf(refChild);
        if (pos < 0) {
            pos = list.size();
        }
        list.add(pos, newChild);
    }

    /**
     * @see org.webreformatter.xml.delegate.IDelegateAdapter#isCDATASection(java.lang.Object)
     */
    public boolean isCDATASection(Object node) {
        Map<String, Object> map = cast(node);
        return map.containsKey(KEY_CDATA);
    }

    /**
     * @see org.webreformatter.xml.delegate.IDelegateAdapter#isComment(java.lang.Object)
     */
    public boolean isComment(Object node) {
        Map<String, Object> map = cast(node);
        return map.containsKey(KEY_COMMENT);
    }

    /**
     * @see org.webreformatter.xml.delegate.IDelegateAdapter#isElement(java.lang.Object)
     */
    public boolean isElement(Object node) {
        Map<String, Object> map = cast(node);
        return map.containsKey(KEY_NAME);
    }

    private boolean isEmpty(Object o) {
        return o == null || "".equals(o.toString());
    }

    private boolean isSystemAttr(String qualifiedName) {
        return qualifiedName.startsWith("_");
    }

    /**
     * @see org.webreformatter.xml.delegate.IDelegateAdapter#isText(java.lang.Object)
     */
    public boolean isText(Object node) {
        Map<String, Object> map = cast(node);
        return map.containsKey(KEY_TEXT);
    }

    /**
     * @see org.webreformatter.xml.delegate.IDelegateAdapter#newDocument(NamespaceManager)
     */
    public Object newDocument(NamespaceManager manager) throws Exception {
        Map<String, Object> map = newMap();
        return map;
    }

    private Map<String, Object> newMap() {
        return new TreeMap<String, Object>();
    }

    public boolean removeAttribute(
        NamespaceManager manager,
        Object document,
        Object element,
        IName attrName) {
        Map<String, Object> docMap = cast(document);
        Map<String, Object> nsMap = getNsMap(docMap, false);
        if (nsMap == null) {
            return false;
        }
        String qualifiedName = getQualifiedName(manager, nsMap, attrName, false);
        if (qualifiedName == null) {
            return false;
        }
        Map<String, Object> map = cast(element);
        boolean contains = map.containsKey(qualifiedName);
        map.remove(qualifiedName);
        return contains;
    }

    /**
     * @see org.webreformatter.xml.delegate.IDelegateAdapter#removeChild(java.lang.Object,
     *      int)
     */
    public void removeChild(Object node, int pos) {
        Map<String, Object> map = cast(node);
        List<Object> children = getChildList(map, false);
        int len = children != null ? children.size() : 0;
        if (pos >= 0 && pos < len) {
            children.remove(pos);
        }
    }

    /**
     * @see org.webreformatter.xml.delegate.IDelegateAdapter#replaceChild(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public void replaceChild(Object node, Object oldChild, Object newNode) {
        Map<String, Object> map = cast(node);
        List<Object> children = getChildList(map, false);
        if (children == null) {
            return;
        }
        int idx = children.indexOf(oldChild);
        if (idx >= 0) {
            children.set(idx, newNode);
        }
    }

    /**
     * @see org.webreformatter.xml.delegate.IDelegateAdapter#setAttribute(NamespaceManager,
     *      Object, java.lang.Object, IName, java.lang.String)
     */
    public void setAttribute(
        NamespaceManager manager,
        Object document,
        Object node,
        IName name,
        String value) {
        Map<String, Object> docMap = cast(document);
        Map<String, Object> nsMap = getNsMap(docMap, true);
        String qualifiedName = getQualifiedName(manager, nsMap, name, true);
        Map<String, Object> map = cast(node);
        map.put(qualifiedName, value);
    }

    public void setAttributes(
        NamespaceManager manager,
        Object document,
        Object element,
        Map<IName, String> attributes) {
        Map<String, Object> docMap = cast(document);
        Map<String, Object> nsMap = getNsMap(docMap, true);
        Map<String, Object> map = cast(element);
        for (String qualifiedName : map
            .keySet()
            .toArray(new String[map.size()])) {
            if (isSystemAttr(qualifiedName)) {
                continue;
            }
            map.remove(qualifiedName);
        }
        for (Map.Entry<IName, String> entry : attributes.entrySet()) {
            IName name = entry.getKey();
            String qualifiedName = getQualifiedName(manager, nsMap, name, true);
            String value = entry.getValue();
            map.put(qualifiedName, value);
        }
    }

    /**
     * @see org.webreformatter.xml.delegate.IDelegateAdapter#setDocumentElement(java.lang.Object,
     *      java.lang.Object)
     */
    public void setDocumentElement(Object document, Object newElement) {
        Map<String, Object> docMap = cast(document);
        docMap.put(KEY_ROOT, newElement);
    }

    /**
     * @see org.webreformatter.xml.delegate.IDelegateAdapter#setTextContent(java.lang.Object,
     *      java.lang.String)
     */
    public void setTextContent(Object node, String content) {
        Map<String, Object> map = cast(node);
        map.put(KEY_TEXT, content);
    }

}
