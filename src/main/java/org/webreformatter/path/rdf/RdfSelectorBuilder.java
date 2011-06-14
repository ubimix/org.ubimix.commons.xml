/**
 * 
 */
package org.webreformatter.path.rdf;

import org.webreformatter.ns.IName;
import org.webreformatter.ns.NamespaceManager;
import org.webreformatter.path.INodeSelector;
import org.webreformatter.path.PathProcessor;
import org.webreformatter.path.PathSelectorBuilder;
import org.webreformatter.path.xml.TextSelector;
import org.webreformatter.rdf.RdfModel;

/**
 * @author kotelnikov
 */
public class RdfSelectorBuilder extends PathSelectorBuilder {

    private NamespaceManager fNamespaceManager;

    public RdfSelectorBuilder() {
        this(new NamespaceManager());
    }

    public RdfSelectorBuilder(NamespaceManager manager) {
        fNamespaceManager = manager;
    }

    public RdfSelectorBuilder add(
        IName propertyName,
        INodeSelector objectSelector) {
        RdfStatementSelector selector = new RdfStatementSelector(
            propertyName,
            objectSelector);
        return add(selector);
    }

    public RdfSelectorBuilder add(IName propertyName, String textMask) {
        INodeSelector objectSelector = new TextSelector(textMask);
        return add(propertyName, objectSelector);
    }

    @Override
    public RdfSelectorBuilder add(INodeSelector selector) {
        super.add(selector);
        return this;
    }

    public PathProcessor buildPath(RdfModel model) {
        return buildPath(new RdfNodeProvider(model));
    }

    @Override
    public RdfSelectorBuilder clear() {
        super.clear();
        return this;
    }

    public NamespaceManager getNamespaceManager() {
        return fNamespaceManager;
    }

    @Override
    public RdfSelectorBuilder node() {
        super.node();
        return this;
    }

    public void setNamespaceManager(NamespaceManager namespaceManager) {
        fNamespaceManager = namespaceManager;
    }

}