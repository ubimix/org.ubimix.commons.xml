/**
 * 
 */
package org.webreformatter.ns;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Namespace managers are used to map namespace prefixes into full namespace
 * URLs and to transform namespaces into the corresponding prefixes.
 * 
 * @author kotelnikov
 */
public class NamespaceManager implements Serializable {
    /**
     * Objects of this type are used to manage namespace information - the
     * relation between the namespaces and the corresponding prefixes.
     */
    static class NamespaceInfo implements Serializable {
        private static final long serialVersionUID = 1540637212502514481L;

        /**
         * The namespace URL
         */
        private IId fNamespace;

        /**
         * Prefix corresponding to the namespace.
         */
        private String fPrefix;

        /**
         * This constructor sets the namespace URL and the corresponding current
         * prefix value. The prefix value could be changed in the future using
         * the {@link #setPrefix(String)} method.
         * 
         * @param namespace the namespace URL
         * @param prefix the prefix corresponding to this namespace
         */
        public NamespaceInfo(IId namespace, String prefix) {
            fNamespace = namespace;
            fPrefix = prefix;
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof NamespaceManager.NamespaceInfo)) {
                return false;
            }
            NamespaceManager.NamespaceInfo o = (NamespaceManager.NamespaceInfo) obj;
            return equals(fNamespace, o.fNamespace)
                && equals(fPrefix, o.fPrefix);
        }

        /**
         * @param a the first string to compare
         * @param b the second string to compare
         * @return <code>true</code> if the given strings are equal or both of
         *         them are <code>null</code>
         */
        private boolean equals(Object a, Object b) {
            return a == null || b == null ? a == b : a.equals(b);
        }

        /**
         * Returns the namspace URL
         * 
         * @return the namespace URL
         */
        public IId getNamespace() {
            return fNamespace;
        }

        /**
         * Returns the prefix corresponding to the namespace.
         * 
         * @return the prefix corresponding to the namespace
         */
        public String getPrefix() {
            return fPrefix;
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            int a = fPrefix != null ? fPrefix.hashCode() : 0;
            int b = fNamespace != null ? fNamespace.hashCode() : 0;
            return a ^ b;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "{" + fPrefix + "=" + fNamespace + "}";
        }

    }

    public static NamespaceManager INSTANCE = new NamespaceManager();

    private static final long serialVersionUID = 807090855409291453L;

    /**
     * The internal counter used to generate new namespace prefixes.
     * 
     * @see #newPrefix()
     */
    private int fNamespaceCounter;

    /**
     * This map is used to access to namespace info by the namespaces
     */
    private Map<IId, NamespaceInfo> fNamespaces = new HashMap<IId, NamespaceInfo>();

    /**
     * This map is used to access to namespace info by prefixes.
     */
    private Map<String, NamespaceInfo> fPrefixes = new HashMap<String, NamespaceInfo>();

    /**
     * The namespace associated with the "xmlns" prefix.
     */
    public final IId NS_XMLNS = getId("http://www.w3.org/2000/xmlns/");

    /**
     * 
     */
    public NamespaceManager() {
        setNamespacePrefix(NS_XMLNS, "xmlns");
    }

    /**
     * Returns an identifier corresponding to the given URI.
     * 
     * @param uri the URI
     * @return a new ID corresponding to the given URI
     */
    public IId getId(String uri) {
        return newId(uri);
    }

    /**
     * Returns the name corresponding to the specified identifier
     * 
     * @param id the identifier
     * @return the name corresponding to the specified identifier
     */
    public IName getName(IId id) {
        return getName(id.toString());
    }

    /**
     * Returns an identifier (an XML name) corresponding to the given namespace
     * and local name.
     * 
     * @param namespace the namespace of the node to return
     * @param localName the local name
     * @return a new ID corresponding to the given namespace and local name
     */
    public IName getName(IId namespace, String localName) {
        return newId(namespace, localName);
    }

    /**
     * Returns an Id instance corresponding to the specified URI. This method
     * interprets the specified URI as an XML name containing two parts - a
     * namespace and a local name. The namespace could be defined by a prefix
     * registered in this manager. In this case it will be expanded to the full
     * URL.
     * 
     * @param uri the uri for which the corresponding identifier should be
     *        returned
     * @return a RdfId instance corresponding to the specified URI
     */
    public IName getName(String uri) {
        IId namespace = null;
        String localName = "";
        int startIndex = uri.indexOf("://"); // URL
        if (startIndex >= 0) {
            startIndex += 3;
        } else {
            startIndex = 0;
        }

        int idx = uri.lastIndexOf("#");
        if (idx <= startIndex) {
            idx = -1;
        }
        if (idx < 0) {
            idx = uri.lastIndexOf("/");
            if (idx <= startIndex) {
                idx = -1;
            }
        }
        if (idx >= 0) {
            String str = uri.substring(0, idx + 1);
            namespace = getId(str);
            localName = uri.substring(idx + 1);
        } else {
            if (startIndex == 0) {
                // The given URL does not contain a scheme part
                idx = uri.lastIndexOf(":");
                String prefix;
                if (idx >= 0) {
                    prefix = uri.substring(0, idx);
                    localName = uri.substring(idx + 1);
                } else {
                    prefix = "";
                    localName = uri;
                }
                namespace = getNamespaceByPrefix(prefix);
                if (namespace == null) {
                    throw new IllegalArgumentException(
                        "The namespace prefix not found. Prefix '"
                            + prefix
                            + "'.");
                }
            } else {
                localName = uri;
            }
        }
        return getName(namespace, localName);
    }

    /**
     * Returns an identifier (an XML name) corresponding to the given namespace
     * and local name.
     * 
     * @param namespace the namespace of the node to return
     * @param localName the local name
     * @return a new ID corresponding to the given namespace and local name
     */
    public IName getName(String namespace, String localName) {
        IId namespaceId = getId(namespace);
        return getName(namespaceId, localName);
    }

    /**
     * Returns the namespace corresponding to the specified prefix
     * 
     * @param prefix the prefix for which the corresponding namespaces should be
     *        returned
     * @return the namespace corresponding to the specified prefix
     */
    public IId getNamespaceByPrefix(String prefix) {
        // if ("_".equals(prefix)) {
        // return null;
        // }
        NamespaceInfo info = fPrefixes.get(prefix);
        return info != null ? info.getNamespace() : null;
    }

    /**
     * Returns the prefix corresponding to the given namespace
     * 
     * @param namespace the namespace for which the prefix should be returned
     * @param create if this flag is <code>true</code> and there is no
     *        registered prefixes for the given namespace then this method
     *        creates and returns a new prefix
     * @return the prefix corresponding to the given identifier
     */
    public String getNamespacePrefix(IId namespace, boolean create) {
        if (isEmpty(namespace)) {
            return "";
        }
        NamespaceInfo info = fNamespaces.get(namespace);
        String prefix = info != null ? info.getPrefix() : null;
        if (prefix == null && create) {
            prefix = newPrefix();
            setNamespacePrefix(namespace, prefix);
        }
        return prefix;
    }

    /**
     * Returns the prefix corresponding to the given namespace
     * 
     * @param namespace the namespace for which the prefix should be returned
     * @param create if this flag is <code>true</code> and there is no
     *        registered prefixes for the given namespace then this method
     *        creates and returns a new prefix
     * @return the prefix corresponding to the given identifier
     */
    public String getNamespacePrefix(String namespace, boolean create) {
        // Name.checkNamespace(namespace);
        IId namespaceId = getId(namespace);
        return getNamespacePrefix(namespaceId, create);
    }

    /**
     * Returns a set of namespaces.
     * 
     * @return a set of namespaces
     */
    public Set<IId> getNamespaces() {
        return fNamespaces.keySet();
    }

    /**
     * Returns the prefix corresponding to the namespace of this id.
     * 
     * @param name the identifier for which the corresponding namespace prefix
     *        should be returned
     * @return the prefix corresponding to the namespace of this id.
     */
    public String getPrefix(IName name) {
        return getPrefix(name, false);
    }

    /**
     * Returns the prefix corresponding to the namespace of the given id.
     * 
     * @param create if this flag is <code>true</code> and there is no
     *        registered prefix for the internal namespace then a new prefix
     *        will be generated
     * @return the prefix corresponding to the namespace of this id.
     */
    public String getPrefix(IName name, boolean create) {
        IId namespaceURL = name.getNamespaceURL();
        if (namespaceURL == null) {
            return null;
        }
        String prefix = getNamespacePrefix(namespaceURL, create);
        return prefix;
    }

    /**
     * Returns a short form of the given identifier (the namespace is replaced
     * by the prefix).
     * 
     * @param id the identifier for which the short form should be returned
     * @return a short form of the given identifier (the namespace is replaced
     *         by the prefix);
     */
    public String getShortForm(IId id) {
        if (!(id instanceof IName)) {
            return id != null ? id.toString() : null;
        }
        IName name = (IName) id;
        IId namespaceUrl = name.getNamespaceURL();
        String prefix = getNamespacePrefix(namespaceUrl, true);
        String localName = name.getLocalName();
        String result;
        if (prefix == null || "".equals(prefix)) {
            result = localName;
        } else {
            result = prefix + ":" + localName;
        }
        return result;
    }

    private boolean isEmpty(IId namespace) {
        if (namespace == null) {
            return true;
        }
        return "".equals(namespace.getUrl());
    }

    /**
     * @param namespace
     * @param localName
     * @return a newly created identifier corresponding to the given namespace
     *         and local name
     */
    private IName newId(IId namespace, String localName) {
        return new Name(namespace, localName);
    }

    /**
     * Creates and returns a new identifier corresponding to the given full URL.
     * 
     * @param fullUrl the full URL
     * @param pos the first position of the local name.
     * @return a new identifier corresponding to the given full URL
     */
    private IId newId(String fullUrl) {
        int delimiterPos = Name.getLocalPartPos(fullUrl);
        IId result = null;
        if (delimiterPos >= fullUrl.length()) {
            result = new Id(fullUrl);
        } else {
            String localName = fullUrl.substring(delimiterPos);
            String namespace = fullUrl.substring(0, delimiterPos);
            IId namespaceId = getId(namespace);
            result = newId(namespaceId, localName);
        }
        return result;
    }

    /**
     * Generates and returns a new namespace prefix.
     * 
     * @return a newly created namespace prefix
     */
    protected String newPrefix() {
        return "ns" + (fNamespaceCounter++);
    }

    /**
     * @param namespace the namespace associated with the new
     * @param prefix the new prefix to set
     */
    public void setNamespacePrefix(IId namespace, String prefix) {
        NamespaceInfo info = fPrefixes.get(prefix);
        if (info != null) {
            fPrefixes.remove(info.getPrefix());
            fNamespaces.remove(info.getNamespace());
        }
        info = fNamespaces.get(namespace);
        if (info != null) {
            fPrefixes.remove(info.getPrefix());
            fNamespaces.remove(info.getNamespace());
        }
        info = new NamespaceInfo(namespace, prefix);
        fNamespaces.put(namespace, info);
        fPrefixes.put(prefix, info);
    }

    /**
     * @param namespace the namespace associated with the new
     * @param prefix the new prefix to set
     */
    public void setNamespacePrefix(String namespace, String prefix) {
        // Name.checkNamespace(namespace);
        IId id = getId(namespace);
        setNamespacePrefix(id, prefix);
    }

}
