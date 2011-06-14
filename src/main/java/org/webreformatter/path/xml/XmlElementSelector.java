/**
 * 
 */
package org.webreformatter.path.xml;

import java.util.Map;

import org.webreformatter.ns.IName;
import org.webreformatter.path.INodeSelector;
import org.webreformatter.xml.IXmlElement;

public class XmlElementSelector implements INodeSelector {

    private Map<IName, INodeSelector> fAttributeSelectors;

    private INodeSelector.Accept fDefaultSelectResult;

    private IName fName;

    public XmlElementSelector(
        IName name,
        INodeSelector.Accept defaultSelectResult,
        Map<IName, INodeSelector> attributeSelectors) {
        fName = name;
        fDefaultSelectResult = defaultSelectResult;
        fAttributeSelectors = attributeSelectors;
    }

    /**
     * A copy constructor used to change the attribute values.
     * 
     * @param filter the filter used as a template
     * @param attributeSelectors attribute filter values
     */
    public XmlElementSelector(
        XmlElementSelector filter,
        Map<IName, INodeSelector> attributeSelectors) {
        fName = filter != null ? filter.fName : null;
        fDefaultSelectResult = filter != null
            ? filter.fDefaultSelectResult
            : INodeSelector.Accept.NO;
        fAttributeSelectors = attributeSelectors;
    }

    public INodeSelector.Accept accept(Object node) {
        INodeSelector.Accept result = INodeSelector.Accept.NO;
        if (node instanceof IXmlElement) {
            IXmlElement e = (IXmlElement) node;
            if (fName != null) {
                if (fName.equals(e.getName())) {
                    result = checkAttributes(
                        e,
                        INodeSelector.Accept.YES,
                        INodeSelector.Accept.NO);
                } else {
                    result = fDefaultSelectResult;
                }
            } else {
                result = checkAttributes(
                    e,
                    fDefaultSelectResult,
                    fDefaultSelectResult);
            }
        }
        return result;
    }

    private INodeSelector.Accept checkAttributes(
        IXmlElement e,
        INodeSelector.Accept defaultResult,
        INodeSelector.Accept negativeResult) {
        INodeSelector.Accept result = defaultResult;
        if (fAttributeSelectors != null) {
            result = INodeSelector.Accept.YES;
            for (Map.Entry<IName, INodeSelector> entry : fAttributeSelectors
                .entrySet()) {
                IName attrName = entry.getKey();

                String value = e.getAttribute(attrName);
                if (value == null) {
                    result = negativeResult;
                    break;
                }

                INodeSelector selector = entry.getValue();
                if (selector != null) {
                    Accept attrResult = selector.accept(value);
                    result = sum(result, attrResult);
                    if (result == INodeSelector.Accept.NO) {
                        result = negativeResult;
                        break;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof XmlElementSelector)) {
            return false;
        }
        XmlElementSelector o = (XmlElementSelector) obj;
        return equals(fName, o.fName)
            && equals(fAttributeSelectors, o.fAttributeSelectors);
    }

    private boolean equals(Object a, Object b) {
        return a != null && b != null ? a.equals(b) : a == b;
    }

    @Override
    public int hashCode() {
        int a = fName != null ? fName.hashCode() : 0;
        int b = fAttributeSelectors != null
            ? fAttributeSelectors.hashCode()
            : 0;
        return a ^ b;
    }

    protected Accept sum(Accept first, Accept second) {
        return first.and(second);
    }

    @Override
    public String toString() {
        return "ElementFilter[" + fName + ":" + fAttributeSelectors + "]";
    }

}