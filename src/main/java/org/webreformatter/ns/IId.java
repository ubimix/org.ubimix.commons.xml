/**
 * 
 */
package org.webreformatter.ns;

import java.io.Serializable;

/**
 * Instances of this type are used as identifiers for RDF graph nodes as well as
 * names for XML elements and attributes.
 * 
 * @author kotelnikov
 */
public interface IId extends Comparable<IId>, Serializable, IValue {

    /**
     * Returns the full URL representation of this id.
     * 
     * @return the full URL representation of this id
     */
    String getUrl();

}
