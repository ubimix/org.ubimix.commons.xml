/**
 * 
 */
package org.webreformatter.rdf;

import java.util.Date;

import org.webreformatter.ns.IName;
import org.webreformatter.ns.IValue;

/**
 * @author kotelnikov
 */
public class RdfValueFactory extends RdfIdManager {
    private static final long serialVersionUID = -974888442975307639L;

    public RdfValueFactory() {
        super();
    }

    public RdfValueFactory(String seed) {
        super(seed);
    }

    /**
     * This method wraps the specified value into an RdfValue instance. It could
     * be one of the following types: {@link RdfString} - for string values;
     * {@link RdfLiteral} - for typed objects (Integer, Float, Boolean, ...) or
     * {@link IName} for references or {@link RdfResource} instances.
     * 
     * @param value the value to wrap
     * @return the representation of the given value as an {@link IValue} object
     */
    public IValue toRdfValue(Object value) {
        if (value instanceof IValue) {
            return (IValue) value;
        }
        IValue result = null;
        if (value instanceof RdfResource) {
            result = ((RdfResource) value).getId();
        } else if (value instanceof String) {
            result = new RdfString((String) value);
        } else {
            IName type = null;
            if (value instanceof Boolean) {
                type = DATATYPE_BOOLEAN;
            } else if (value instanceof Byte) {
                type = DATATYPE_BYTE;
            } else if (value instanceof Date) {
                type = DATATYPE_DATE;
            } else if (value instanceof Double) {
                type = DATATYPE_DOUBLE;
            } else if (value instanceof Float) {
                type = DATATYPE_FLOAT;
            } else if (value instanceof Integer) {
                type = DATATYPE_INT;
            } else if (value instanceof Long) {
                type = DATATYPE_LONG;
            } else if (value instanceof Short) {
                type = DATATYPE_SHORT;
            }
            if (type != null) {
                result = new RdfLiteral(value, type);
            }
        }
        if (result == null) {
            String str = value != null ? value.toString() : null;
            result = new RdfString(str);
        }
        return result;
    }

}
