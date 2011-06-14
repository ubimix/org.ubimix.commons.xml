package org.webreformatter.rdf;

import org.webreformatter.ns.IName;
import org.webreformatter.xml.IXmlDocument;

public class RdfXmlLiteral extends RdfLiteral {
    private static final long serialVersionUID = -3339790063475538415L;

    private static IName getXmlDatatype(IXmlDocument value) {
        RdfIdManager manager = (RdfIdManager) value.getNamespaceManager();
        return manager.DATATYPE_XML;
    }

    /**
     * Default constructor used for serialization/deserialization.
     */
    protected RdfXmlLiteral() {
    }

    public RdfXmlLiteral(IXmlDocument value) {
        super(value, getXmlDatatype(value));
    }

}
