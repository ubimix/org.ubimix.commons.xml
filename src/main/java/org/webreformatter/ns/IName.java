package org.webreformatter.ns;

public interface IName extends IId {

    /**
     * Returns the local name of this identifier
     * 
     * @return the local name of this identifier
     */
    String getLocalName();

    /**
     * Returns the namespace of this identifier. The namespace is never
     * <code>null</code>, but it could be an empty string.
     * 
     * @return the namespace of this identifier
     */
    IId getNamespaceURL();

}
