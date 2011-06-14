/**
 * 
 */
package org.webreformatter.rdf;

import org.webreformatter.ns.IName;
import org.webreformatter.ns.IValue;

/**
 * Container for typed literal values.
 * 
 * @author kotelnikov
 */
public class RdfLiteral implements IValue {
    private static final long serialVersionUID = 6278426719364351630L;

    /**
     * The type of the value.
     */
    private IName fDatatype;

    /**
     * A string representation of this value. This field is filled only when the
     * {@link #toString()} method is called the first time.
     */
    private transient String fString;

    /**
     * The java object representing the typed value.
     */
    private Object fValue;

    /**
     * Default constructor used for serialization/deserialization.
     */
    protected RdfLiteral() {
    }

    /**
     * 
     */
    public RdfLiteral(Object value, IName datatype) {
        fValue = value;
        fDatatype = datatype;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RdfLiteral)) {
            return false;
        }
        RdfLiteral o = (RdfLiteral) obj;
        return toString().equals(o.toString());
    }

    protected String escape(String value) {
        return value;
    }

    /**
     * Returns the identifier of the datatype of this literal.
     * 
     * @return the identifier of the datatype of this literal
     */
    public IName getDatatype() {
        return fDatatype;
    }

    /**
     * Returns the java representation of the underlying value.
     * 
     * @return the java representation of the underlying value
     */
    public Object getValue() {
        return fValue;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int first = fValue != null ? fValue.hashCode() : 0;
        int second = fDatatype != null ? fDatatype.hashCode() : 0;
        return first ^ second;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (fString == null) {
            StringBuilder builder = new StringBuilder();
            if (fValue == null) {
                builder.append("null");
            } else {
                builder.append("\"");
                String str = fValue.toString();
                str = escape(str);
                builder.append(str);
                builder.append("\"");
                if (fDatatype != null) {
                    builder.append("^^<");
                    builder.append(fDatatype);
                    builder.append(">");
                }
            }
            fString = builder.toString();
        }
        return fString;
    }
}
