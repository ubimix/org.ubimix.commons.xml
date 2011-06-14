/**
 * 
 */
package org.webreformatter.rdf;

import org.webreformatter.ns.IValue;

/**
 * @author kotelnikov
 */
public class RdfString implements IValue {
    private static final long serialVersionUID = -6591642983531041829L;

    private String fLang;

    private transient String fString;

    private String fValue;

    /**
     * Default constructor used for serialization/deserialization.
     */
    protected RdfString() {
    }

    /**
     * 
     */
    public RdfString(String value) {
        this(value, null);
    }

    /**
     * 
     */
    public RdfString(String value, String lang) {
        fValue = value;
        fLang = lang;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RdfString)) {
            return false;
        }
        RdfString o = (RdfString) obj;
        return toString().equals(o.toString());
    }

    public String getLang() {
        return fLang;
    }

    public String getValue() {
        return fValue;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int first = fValue != null ? fValue.hashCode() : 0;
        int second = fLang != null ? fLang.hashCode() : 0;
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
                if (fValue.matches("[\r\n]")) {
                    builder.append("\"\"\"");
                    builder.append(fValue);
                    builder.append("\"\"\"");
                } else {
                    builder.append("\"");
                    builder.append(fValue);
                    builder.append("\"");
                }
                if (fLang != null) {
                    builder.append("@\"");
                    builder.append(fLang);
                    builder.append("\"");
                }
            }
            fString = builder.toString();
        }
        return fString;
    }
}
