/**
 * 
 */
package org.webreformatter.rdf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.webreformatter.ns.IId;
import org.webreformatter.ns.IName;
import org.webreformatter.ns.IValue;

/**
 * @author kotelnikov
 */
public class RdfModel implements Serializable {
    private static final long serialVersionUID = 3832256680199628296L;

    /**
     * The identifier corresponding to this model
     */
    private IId fId;

    /**
     * The internal map
     */
    private Map<IId, RdfResource> fResources = new HashMap<IId, RdfResource>();

    /**
     * Default constructor used for serialization/deserialization.
     */
    protected RdfModel() {
    }

    /**
     * The default constructor for this model
     * 
     * @param id the identifier of the model
     */
    public RdfModel(IId id) {
        fId = id;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RdfModel)) {
            return false;
        }
        RdfModel o = (RdfModel) obj;
        return fId.equals(o.getId());
    }

    /**
     * Returns a set of all resources managed by this model
     * 
     * @return a set of all resources managed by this model
     */
    public Set<RdfResource> getAllResources() {
        getModelResource();
        Set<RdfResource> set = new HashSet<RdfResource>(fResources.values());
        return set;
    }

    /**
     * Returns the identifier of this model
     * 
     * @return the identifier of this model
     */
    public IId getId() {
        return fId;
    }

    /**
     * Returns the resource corresponding to this model
     * 
     * @return the resource corresponding to this model
     */
    public RdfResource getModelResource() {
        return getResource(fId, true);
    }

    /**
     * Returns a set of property identifiers
     * 
     * @return identifiers of all properties
     */
    public Set<IName> getPropertyIds() {
        Set<IName> result = new LinkedHashSet<IName>();
        for (RdfResource resource : getAllResources()) {
            for (IName property : resource.getPropertyNames()) {
                result.add(property);
            }
        }
        return result;
    }

    /**
     * Creates and returns the resource corresponding to the given identifier.
     * 
     * @param resourceId the identifier of the resource to create
     * @param create if this flag is <code>true</code> and there is no resource
     *        corresponding to the given id then this method creates a new
     *        resource
     * @return the resource corresponding to the specified identifier
     */
    public RdfResource getResource(IId resourceId, boolean create) {
        RdfResource resource = fResources.get(resourceId);
        if (resource == null && (create || fId.equals(resourceId))) {
            resource = new RdfResource(this, resourceId);
            fResources.put(resourceId, resource);
        }
        return resource;
    }

    /**
     * Returns a set of all resources managed by this model
     * 
     * @return a set of all resources managed by this model
     */
    public Set<RdfResource> getResources() {
        Set<RdfResource> result = new HashSet<RdfResource>();
        result.addAll(fResources.values());
        return result;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return fId.hashCode();
    }

    /**
     * Tries to remove a resource corresponding to the given identifier.
     * 
     * @param resourceId the identifier of the resource to remove
     * @return <code>true</code> if a resource with this identifier was removed
     */
    public boolean removeResource(IName resourceId) {
        return fResources.remove(resourceId) != null;
    }

    /**
     * Transforms the given RDF value into a java object and returns it.
     * 
     * @param value the RDF value to transform
     * @return the java object corresponding to the given RDF value
     */
    public Object toJavaValue(IValue value) {
        if (value instanceof RdfString) {
            return ((RdfString) value).getValue();
        } else if (value instanceof IId) {
            return value;
        } else if (value instanceof RdfLiteral) {
            RdfLiteral literal = (RdfLiteral) value;
            return literal.getValue();
        }
        return null;
    }

    /**
     * Returns the list of values of the property with the specified identifier.
     * 
     * @param propertyId the identifier of the property
     * @return a list of values of the property with the specified identifier.
     */
    public List<Object> toJavaValues(List<IValue> values) {
        List<Object> result = new ArrayList<Object>();
        for (IValue value : values) {
            Object object = toJavaValue(value);
            result.add(object);
        }
        return result;
    }

    /**
     * Returns an {@link RdfString} instance corresponding to the given string.
     * 
     * @param string the string value
     * @return an {@link RdfString} instance corresponding to the given string
     */
    public IValue toRdfValue(String string) {
        return new RdfString(string);
    }

    /**
     * Returns an {@link RdfString} instance corresponding to the given string
     * and the given locale.
     * 
     * @param string the string value
     * @param lang the language (locale) of the string
     * @return an {@link RdfString} instance corresponding to the given string
     */
    public IValue toRdfValue(String string, String lang) {
        return new RdfString(string, lang);
    }

    /**
     * Returns the specified value as an RDF resource.
     * 
     * @param value the value to load as a resource
     * @param create if this flag is <code>true</code> and this model does not
     *        contain a resource corresponding to the specified identifier then
     *        this method creates a new such resource
     * @return the specified value as an RDF resource
     */
    public RdfResource toResource(IValue value, boolean create) {
        if (value instanceof IName) {
            return getResource((IName) value, create);
        }
        return null;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RdfModel<" + fId + ">";
    }
}
