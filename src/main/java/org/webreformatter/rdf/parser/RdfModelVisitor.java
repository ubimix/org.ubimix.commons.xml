/**
 * 
 */
package org.webreformatter.rdf.parser;

import java.util.List;
import java.util.Set;

import org.webreformatter.ns.IId;
import org.webreformatter.ns.IName;
import org.webreformatter.ns.IValue;
import org.webreformatter.rdf.RdfLiteral;
import org.webreformatter.rdf.RdfModel;
import org.webreformatter.rdf.RdfResource;
import org.webreformatter.rdf.RdfString;

/**
 * @author kotelnikov
 */
public class RdfModelVisitor {

    /**
     * 
     */
    public RdfModelVisitor() {
        // TODO Auto-generated constructor stub
    }

    public void visit(RdfModel model, ITripleListener listener) {
        Set<RdfResource> resources = model.getAllResources();
        for (RdfResource resource : resources) {
            IId subject = resource.getId();
            Set<IName> properties = resource.getPropertyNames();
            for (IName predicate : properties) {
                List<IValue> values = resource.getValues(predicate);
                for (IValue value : values) {
                    if (value instanceof RdfLiteral) {
                        RdfLiteral literal = (RdfLiteral) value;
                        IName type = literal.getDatatype();
                        Object obj = literal.getValue();
                        String str = obj.toString();
                        listener.onDatatypeStatement(
                            subject,
                            predicate,
                            str,
                            type);
                    } else if (value instanceof RdfString) {
                        RdfString string = (RdfString) value;
                        String lang = string.getLang();
                        String str = string.getValue();
                        listener.onStringStatement(
                            subject,
                            predicate,
                            str,
                            lang);
                    } else if (value instanceof IId) {
                        IId id = (IId) value;
                        listener.onReferenceStatement(subject, predicate, id);
                    }
                }
            }
        }
    }

}
