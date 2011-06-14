/**
 * 
 */
package org.webreformatter.ns;

/**
 * Instances of this type are used as identifiers for RDF graph nodes.
 * 
 * @author kotelnikov
 */
public class Id extends AbstractUri {

    private static final long serialVersionUID = -6618149736991767880L;

    /**
     * A string representation of this object. This field is used only in the
     * {@link #toString()} method.
     */
    protected String fUri;

    /**
     * Default constructor used for serialization/deserialization.
     */
    protected Id() {
    }

    /**
     * This constructor sets the URI corresponding to this ID.
     * 
     * @param uri the URI
     * @param localnamePos the position of the localname in this url
     */
    public Id(String uri) {
        if (uri == null)
            uri = "";
        fUri = uri;
    }

    /**
     * Returns the full URL representation of this id.
     * 
     * @return the full URL representation of this id
     */
    public String getUrl() {
        return fUri;
    }

}
