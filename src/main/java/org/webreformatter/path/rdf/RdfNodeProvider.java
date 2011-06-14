/**
 * 
 */
package org.webreformatter.path.rdf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.webreformatter.ns.IId;
import org.webreformatter.ns.IName;
import org.webreformatter.ns.IValue;
import org.webreformatter.path.INodeProvider;
import org.webreformatter.rdf.RdfModel;
import org.webreformatter.rdf.RdfResource;

/**
 * @author kotelnikov
 */
public class RdfNodeProvider implements INodeProvider {

    private RdfModel fModel;

    /**
     * 
     */
    public RdfNodeProvider(RdfModel model) {
        fModel = model;
    }

    /**
     * @see org.webreformatter.path.INodeProvider
     *      <T>#getChildren(java.lang.Object)
     */
    public Iterator<?> getChildren(final Object parent) {
        if (!(parent instanceof RdfStatement)
            && !(parent instanceof RdfResource)
            && !(parent instanceof IId))
            return null;

        RdfResource subject = null;
        if (parent instanceof RdfResource) {
            subject = (RdfResource) parent;
        } else if (parent instanceof IId) {
            IId subjectId = (IId) parent;
            subject = fModel.getResource(subjectId, false);
        } else {
            RdfStatement statement = (RdfStatement) parent;
            IValue object = statement.getObject();
            if (object instanceof IId) {
                IId id = (IId) object;
                subject = fModel.getResource(id, false);
            }
        }

        Iterator<?> result = null;
        if (subject != null) {
            List<RdfStatement> statements = new ArrayList<RdfStatement>();
            Set<IName> properties = subject.getPropertyNames();
            for (IName property : properties) {
                List<IValue> values = subject.getValues(property);
                IId subjectId = subject.getId();
                for (IValue value : values) {
                    RdfStatement statement = new RdfStatement(
                        subjectId,
                        property,
                        value);
                    statements.add(statement);
                }
            }
            result = statements.iterator();
        }
        return result;
    }
}
