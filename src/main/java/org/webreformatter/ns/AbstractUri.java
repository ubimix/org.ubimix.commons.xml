/**
 * 
 */
package org.webreformatter.ns;

/**
 * @author kotelnikov
 */
public abstract class AbstractUri implements IId {

    /**
     * 
     */
    private static final long serialVersionUID = -8562591130175679569L;

    /**
     * Default constructor used for serialization/deserialization.
     */
    protected AbstractUri() {
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(IId o) {
        String first = getUrl();
        String second = o.getUrl();
        return first.compareTo(second);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof IId))
            return false;
        IId o = (IId) obj;
        return compareTo(o) == 0;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        String uri = getUrl();
        return uri.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String shortUrl = getUrl();
        return shortUrl;
    }
}
