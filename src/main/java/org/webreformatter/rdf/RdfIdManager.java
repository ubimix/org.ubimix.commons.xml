/**
 * 
 */
package org.webreformatter.rdf;

import org.webreformatter.ns.IId;
import org.webreformatter.ns.IName;
import org.webreformatter.ns.NamespaceManager;

/**
 * @author kotelnikov
 */
public class RdfIdManager extends NamespaceManager {
    private static int fIdCounter = 1;

    private static final long serialVersionUID = -4886402889319571645L;

    /**
     * The RDF namespace
     */
    public final IId _NS_RDF = getId("http://www.w3.org/1999/02/22-rdf-syntax-ns#");

    /**
     * XML Schema namespace. It is used to create pre-defined datatypes.
     */
    public final IId _NS_XMLS = getId("http://www.w3.org/2001/XMLSchema#");

    public final IName DATATYPE_BOOLEAN = getName(_NS_XMLS, "boolean");

    public final IName DATATYPE_BYTE = getName(_NS_XMLS, "byte");

    public final IName DATATYPE_DATE = getName(_NS_XMLS, "date");

    public final IName DATATYPE_DOUBLE = getName(_NS_XMLS, "double");

    public final IName DATATYPE_FLOAT = getName(_NS_XMLS, "float");

    public final IName DATATYPE_INT = getName(_NS_XMLS, "int");

    public final IName DATATYPE_LONG = getName(_NS_XMLS, "long");

    public final IName DATATYPE_SHORT = getName(_NS_XMLS, "short");

    public final IName DATATYPE_XML = getName(_NS_RDF, "Literal");

    private String fSeed;

    /**
     * 
     */
    public RdfIdManager() {
        this("" + System.currentTimeMillis());
    }

    /**
     * This constructor sets the random seed used for generation of unique
     * identifiers.
     * 
     * @param seed the random seed used to generate identifiers
     */
    public RdfIdManager(String seed) {
        setRandomSeed(seed);
    }

    /**
     * @return the base of the
     */
    protected String getIdBase() {
        return fSeed + "_" + (fIdCounter++) + "_" + Math.random();
    }

    /**
     * Returns a newly created random generated identifier.
     * 
     * @return a newly created random generated identifier
     */
    public IId newId() {
        SHA1 sha = new SHA1();
        String random = getIdBase();
        sha.update(random);
        String hash = sha.getDigestString();
        String localName = "id-" + hash;
        return getId(localName);
    }

    public void setRandomSeed(String seed) {
        fSeed = seed;
    }

}
