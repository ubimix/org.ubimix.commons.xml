/**
 * 
 */
package org.webreformatter.path.rdf;

import org.webreformatter.ns.IName;
import org.webreformatter.ns.IValue;
import org.webreformatter.path.INodeSelector;

/**
 * @author kotelnikov
 */
public class RdfStatementSelector implements INodeSelector {

    private INodeSelector.Accept fDefaultSelectResult;

    private INodeSelector fObjectSelector;

    private IName fPredicate;

    public RdfStatementSelector(IName predicate) {
        this(predicate, null, INodeSelector.Accept.NO);
    }

    public RdfStatementSelector(IName predicate, INodeSelector objectSelector) {
        this(predicate, objectSelector, INodeSelector.Accept.NO);
    }

    public RdfStatementSelector(
        IName predicate,
        INodeSelector objectSelector,
        INodeSelector.Accept defaultSelectResult) {
        fPredicate = predicate;
        fDefaultSelectResult = defaultSelectResult;
        fObjectSelector = objectSelector;
    }

    public INodeSelector.Accept accept(Object node) {
        INodeSelector.Accept result = INodeSelector.Accept.NO;
        if (node instanceof RdfStatement) {
            RdfStatement statement = (RdfStatement) node;
            IName predicate = statement.getPredicate();
            result = fDefaultSelectResult;
            if (fPredicate != null && fPredicate.equals(predicate)) {
                result = INodeSelector.Accept.YES;
            }
            if (fObjectSelector != null) {
                IValue object = statement.getObject();
                result = fObjectSelector.accept(object);
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof RdfStatementSelector)) {
            return false;
        }
        RdfStatementSelector o = (RdfStatementSelector) obj;
        return equals(fPredicate, o.fPredicate)
            && equals(fObjectSelector, o.fObjectSelector);
    }

    private boolean equals(Object a, Object b) {
        return a != null && b != null ? a.equals(b) : a == b;
    }

    @Override
    public int hashCode() {
        int a = fPredicate != null ? fPredicate.hashCode() : 0;
        int b = fObjectSelector != null ? fObjectSelector.hashCode() : 0;
        return a ^ b;
    }

    @Override
    public String toString() {
        return "ElementFilter[" + fPredicate + ":" + fObjectSelector + "]";
    }

}