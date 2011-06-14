/**
 * 
 */
package org.webreformatter.path.rdf;

import org.webreformatter.ns.IId;
import org.webreformatter.ns.IName;
import org.webreformatter.ns.IValue;

/**
 * @author kotelnikov
 */
public class RdfStatement {

    private IValue fObject;

    private IName fPredicate;

    private IId fSubject;

    public RdfStatement(IId subject, IName predicate, IValue object) {
        fSubject = subject;
        fPredicate = predicate;
        fObject = object;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof RdfStatement))
            return false;
        RdfStatement o = (RdfStatement) obj;
        return fSubject.equals(o.fSubject)
            && fPredicate.equals(o.fPredicate)
            && fObject.equals(o.fObject);
    }

    protected IValue getObject() {
        return fObject;
    }

    protected IName getPredicate() {
        return fPredicate;
    }

    protected IId getSubject() {
        return fSubject;
    }

    @Override
    public int hashCode() {
        int a = fSubject.hashCode();
        int b = fPredicate.hashCode();
        int c = fObject.hashCode();
        return a ^ b ^ c;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("RdfStatement{");
        buf.append(fSubject);
        buf.append(";");
        buf.append(fPredicate);
        buf.append(";");
        buf.append(fObject);
        buf.append("}");
        return buf.toString();
    }
}