/**
 * 
 */
package org.webreformatter.path.xml;

import java.util.LinkedHashMap;
import java.util.Map;

import org.webreformatter.ns.IName;
import org.webreformatter.ns.NamespaceManager;
import org.webreformatter.path.INodeSelector;
import org.webreformatter.path.PathProcessor;
import org.webreformatter.path.PathSelectorBuilder;

public class XmlSelectorBuilder extends PathSelectorBuilder {

    private NamespaceManager fNamespaceManager;

    public XmlSelectorBuilder() {
        this(new NamespaceManager());
    }

    public XmlSelectorBuilder(NamespaceManager manager) {
        fNamespaceManager = manager;
    }

    @Override
    public XmlSelectorBuilder add(INodeSelector selector) {
        super.add(selector);
        return this;
    }

    public XmlSelectorBuilder attrs(Map<IName, String> map) {
        Map<IName, INodeSelector> m = new LinkedHashMap<IName, INodeSelector>();
        for (Map.Entry<IName, String> entry : map.entrySet()) {
            IName name = entry.getKey();
            String mask = entry.getValue();
            INodeSelector selector = mask != null
                ? new TextSelector(mask)
                : null;
            m.put(name, selector);
        }
        return attrSelectors(m);
    }

    public XmlSelectorBuilder attrs(String... attributes) {
        Map<IName, String> map = toMap(attributes);
        return attrs(map);
    }

    public XmlSelectorBuilder attrSelectors(Map<IName, INodeSelector> map) {
        int len = fList.size();
        XmlElementSelector elementFilter = null;
        INodeSelector filter = len > 0 ? fList.get(len - 1) : null;
        if (filter instanceof XmlElementSelector) {
            elementFilter = (XmlElementSelector) filter;
        }
        XmlElementSelector newFilter = new XmlElementSelector(
            elementFilter,
            map);
        if (elementFilter != null) {
            fList.set(len - 1, newFilter);
        } else {
            fList.add(newFilter);
        }
        return this;
    }

    public PathProcessor buildPath() {
        return buildPath(new XmlNodeProvider());
    }

    @Override
    public XmlSelectorBuilder clear() {
        super.clear();
        return this;
    }

    public XmlSelectorBuilder element(IName name) {
        return element(name, false);
    }

    private XmlSelectorBuilder element(IName name, boolean deep) {
        INodeSelector.Accept defaultSelectResult = deep
            ? INodeSelector.Accept.MAYBE
            : INodeSelector.Accept.NO;
        XmlElementSelector selector = new XmlElementSelector(
            name,
            defaultSelectResult,
            null);
        return add(selector);
    }

    public XmlSelectorBuilder element(String name) {
        IName n = getName(name);
        return element(n, false);
    }

    public XmlSelectorBuilder element(String name, boolean deep) {
        IName n = getName(name);
        return element(n, deep);
    }

    private IName getName(String name) {
        return name != null ? fNamespaceManager.getName(name) : null;
    }

    public NamespaceManager getNamespaceManager() {
        return fNamespaceManager;
    }

    @Override
    public XmlSelectorBuilder node() {
        super.node();
        return this;
    }

    public void setNamespaceManager(NamespaceManager namespaceManager) {
        fNamespaceManager = namespaceManager;
    }

    public XmlSelectorBuilder text(String mask) {
        XmlTextSelector selector = new XmlTextSelector(mask);
        return add(selector);
    }

    private Map<IName, String> toMap(String... attributes) {
        Map<IName, String> result = new LinkedHashMap<IName, String>();
        for (int i = 0; i < attributes.length; i++) {
            String name = attributes[i];
            i++;
            String value = i < attributes.length ? attributes[i] : "";
            IName n = getName(name);
            result.put(n, value);
        }
        return result;
    }

}