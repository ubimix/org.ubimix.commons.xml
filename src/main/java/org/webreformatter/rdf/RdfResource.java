/**
 * 
 */
package org.webreformatter.rdf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
public class RdfResource implements Serializable {
    private static final long serialVersionUID = 8047971003031440914L;

    private IId fId;

    private RdfModel fModel;

    private Map<IName, List<IValue>> fProperties = new LinkedHashMap<IName, List<IValue>>();

    /**
     * Default constructor used for serialization/deserialization.
     */
    protected RdfResource() {
    }

    public RdfResource(RdfModel model, IId resourceId) {
        fModel = model;
        fId = resourceId;
    }

    /**
     * Adds a new link to the specified resource. Note that this method DOES NOT
     * add the resource to the underlying model, it just sets a link to the
     * resource ID.
     * 
     * @param propertyName the name of the property
     * @param resource the resource to add
     */
    public void addResource(IName propertyName, RdfResource resource) {
        addValue(propertyName, resource.getId());
    }

    /**
     * Adds a new value of the property with the specified identifier
     * 
     * @param propertyName the identifier of the property to set
     * @param value a new value to add
     */
    public void addValue(IName propertyName, IValue value) {
        List<IValue> list = getValues(propertyName, true);
        list.add(value);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RdfResource)) {
            return false;
        }
        RdfResource o = (RdfResource) obj;
        return fId.equals(o.getId());
    }

    /**
     * Returns the identifier corresponding to this resource
     * 
     * @return the identifier corresponding to this resource
     */
    public IId getId() {
        return fId;
    }

    /**
     * Returns the set of properties defined for this resource.
     * 
     * @return the set of properties defined for this resource
     */
    public Set<IName> getPropertyNames() {
        Set<IName> result = new LinkedHashSet<IName>(fProperties.keySet());
        return result;
    }

    public RdfString getRdfStringValue(IName propertyName, String lang) {
        List<IValue> list = getValues(propertyName, false);
        RdfString result = null;
        if (list != null) {
            for (IValue value : list) {
                if (value instanceof RdfString) {
                    RdfString rdfString = (RdfString) value;
                    if (lang == null || lang.equals(rdfString.getLang())) {
                        result = rdfString;
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns the list of string values of the property .
     * 
     * @param propertyName the identifier of the property
     * @return the list of all string values of the property .
     */
    public List<RdfString> getRdfStringValues(IName propertyName) {
        return getRdfStringValues(propertyName, null);
    }

    /**
     * Returns the list of string values of the property in the specified
     * language.
     * 
     * @param propertyName the identifier of the property
     * @param lang the language; if this value is <code>null</code> then this
     *        method returns all string values of the specified property
     * @return the list of string values of the property in the specified
     *         language.
     */
    public List<RdfString> getRdfStringValues(IName propertyName, String lang) {
        List<IValue> list = getValues(propertyName, false);
        List<RdfString> result = new ArrayList<RdfString>();
        if (list != null) {
            for (IValue value : list) {
                if (value instanceof RdfString) {
                    RdfString rdfString = (RdfString) value;
                    if (lang == null || lang.equals(rdfString.getLang())) {
                        result.add(rdfString);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns the first target resources of the specified property (if any). If
     * the given <code>create</code> flag is <code>true</code> then this method
     * creates target resources not registered in the model; otherwise this
     * method returns the first existing target resource.
     * 
     * @param propertyName the identifier of the property
     * @return a list of target resources of the property with the specified
     *         identifier.
     */
    public RdfResource getResource(IName propertyName, boolean create) {
        List<IValue> list = getValues(propertyName);
        RdfResource result = null;
        for (IValue value : list) {
            if (value instanceof IId) {
                IId resourceId = (IId) value;
                RdfResource resource = fModel.getResource(resourceId, create);
                if (resource != null) {
                    result = resource;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Returns the list of target resources of the specified property. If the
     * given <code>create</code> flag is <code>true</code> then this method
     * creates target resources not registered in the model; otherwise the
     * resulting list contains only already existing resources.
     * 
     * @param propertyName the identifier of the property
     * @return a list of target resources of the property with the specified
     *         identifier.
     */
    public List<RdfResource> getResources(IName propertyName, boolean create) {
        List<IValue> list = getValues(propertyName);
        List<RdfResource> result = new ArrayList<RdfResource>();
        for (IValue value : list) {
            if (value instanceof IName) {
                IName resourceId = (IName) value;
                RdfResource resource = fModel.getResource(resourceId, create);
                if (resource != null) {
                    result.add(resource);
                }
            }
        }
        return result;
    }

    /**
     * Returns the first value (if any) of the specified property.
     * 
     * @param propertyName the identifier of the property
     * @return the first value (if any) of the specified property.
     */
    public IValue getValue(IName propertyName) {
        List<IValue> list = getValues(propertyName, false);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    /**
     * Returns the list of values of the property with the specified identifier.
     * 
     * @param propertyName the identifier of the property
     * @return a list of values of the property with the specified identifier.
     */
    public List<IValue> getValues(IName propertyName) {
        List<IValue> list = getValues(propertyName, false);
        List<IValue> result = new ArrayList<IValue>();
        if (list != null) {
            result.addAll(list);
        }
        return result;
    }

    /**
     * Returns an internally used list of values of the property with the
     * specified identifier.
     * 
     * @param propertyName the identifier of the property
     * @param create if there is no value list corresponding to the given
     *        property id and this flag is <code>true</code> then this method
     *        creates and returns a new value list
     * @return an internally used list of values of the property with the
     *         specified identifier
     */
    private List<IValue> getValues(IName propertyName, boolean create) {
        List<IValue> list = fProperties.get(propertyName);
        if (list == null && create) {
            list = new ArrayList<IValue>();
            fProperties.put(propertyName, list);
        }
        return list;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return fId.hashCode();
    }

    /**
     * Sets a new link to the specified resource. Note that this method DOES NOT
     * add the resource to the underlying model, it just sets a link to the
     * resource ID.
     * 
     * @param propertyName the name of the property
     * @param resource the resource to add
     */
    public void setResource(IName propertyName, RdfResource resource) {
        setValue(propertyName, resource.getId());
    }

    /**
     * Sets the new value of the specified property. This method replaces all
     * existing values by the new one.
     * 
     * @param propertyName the URI of the property to set
     * @param value the new value to set
     */
    public void setValue(IName propertyName, IValue value) {
        List<IValue> list = getValues(propertyName, true);
        list.clear();
        list.add(value);
    }

    /**
     * Sets the new string value of the specified property. This method replaces
     * all existing values by the new one.
     * 
     * @param propertyName the URI of the property to set
     * @param value the new value to set
     */
    public void setValue(IName propertyName, String value, String lang) {
        setValue(propertyName, new RdfString(value, lang));
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RdfResource<" + fId.toString() + ">";
    }

}
