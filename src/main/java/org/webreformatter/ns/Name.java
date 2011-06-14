/**
 * 
 */
package org.webreformatter.ns;

/**
 * @author kotelnikov
 */
public class Name extends AbstractUri implements IName {
    private static final long serialVersionUID = 4607579668086776602L;

    /**
     * Checks that the given XML local name is valid. If it is not valid then
     * this method rises the {@link IllegalArgumentException}.
     * 
     * @param localName the local name to check
     * @throws IllegalArgumentException if the given local name is invalid
     */
    public static void checkLocalName(String localName) {
        boolean valid = isValidLocalName(localName);
        if (!valid) {
            throw new IllegalArgumentException(
                "The specified local name is invalid.");
        }
    }

    /**
     * Checks that the given XML namespace is valid. If it is not valid then
     * this method rises the {@link IllegalArgumentException}.
     * 
     * @param namespace the namespace to check
     * @throws IllegalArgumentException if the given namespace is invalid
     */
    public static void checkNamespace(String namespace) {
        if (!isValidNamespace(namespace)) {
            throw new IllegalArgumentException("Bad namespace.");
        }
    }

    /**
     * This method defines the start position of the substring which could be
     * used as a local name for XML tags.
     * 
     * @param str the string to check
     * @return the first position of a local name in the given string
     * @see http://www.w3.org/TR/REC-xml-names/#NT-LocalPart - the local part
     *      description
     * @see http://www.w3.org/TR/REC-xml/#NT-Name - the XML names
     * @see http://www.w3.org/TR/REC-xml/#NT-NameStartChar - the first character
     *      of XML names
     * @see http://www.w3.org/TR/REC-xml/#NT-NameChar - all other characters of
     *      XML names
     */
    public static int getLocalPartPos(String str) {
        int i;
        int len = str.length();
        for (i = len - 1; i >= 0; i--) {
            char ch = str.charAt(i);
            if (isXmlNameStartChar(ch) || isXmlNameNextChar(ch)) {
                continue;
            }
            break;
        }
        i++;
        for (; i < len; i++) {
            char ch = str.charAt(i);
            if (isXmlNameStartChar(ch)) {
                break;
            }
        }
        return i;
    }

    /**
     * Checks that the given XML local name is valid and returns
     * <code>true</code> if so.
     * 
     * @param localName the local name to check
     */
    public static boolean isValidLocalName(String localName) {
        if (localName == null) {
            return false;
        }
        int pos = getLocalPartPos(localName);
        boolean valid = (pos >= 0);
        return valid;
    }

    /**
     * Checks the given XML namespace and returns <code>true</code> if it is
     * valid.
     * 
     * @param namespace the namespace to check
     * @return <code>true</code> if the given namespace is valid
     */
    public static boolean isValidNamespace(String namespace) {
        boolean valid = true;
        if (namespace == null) {
            namespace = "";
        }
        int len = namespace.length();
        if (len > 0) {
            char ch = namespace.charAt(len - 1);
            if (isXmlNameStartChar(ch)) {
                valid = false;
            }
        }
        return valid;
    }

    /**
     * Returns <code>true</code> if the specified character is an allowed
     * character for XML names. This method does not check if the specified
     * character could be considered as the first character of an XML name.
     * 
     * @param ch the character to test.
     * @return <code>true</code> if the specified character is an allowed
     *         character for XML names
     * @see http://www.w3.org/TR/REC-xml/#NT-NameChar
     */
    private static boolean isXmlNameNextChar(char ch) {
        if ((ch == '-')
            || (ch == '.')
            || (ch >= '0' && ch <= '9')
            || (ch == 0xB7)
            || (ch >= 0x0300 && ch <= 0x036F)
            || (ch >= 0x203F && ch <= 0x2040)) {
            return true;
        }
        return false;
    }

    /**
     * Returns <code>true</code> if the specified character could be considered
     * as the first character of an XML name.
     * 
     * @param ch the character to test
     * @return <code>true</code> if the specified character could be considered
     *         as the first character of an XML name
     * @see http://www.w3.org/TR/REC-xml/#NT-NameStartChar
     */
    private static boolean isXmlNameStartChar(char ch) {
        // if (ch == ':') return true;
        return (ch >= 'A' && ch <= 'Z')
            || (ch == '_')
            || (ch >= 'a' && ch <= 'z')
            || (ch >= 0xC0 && ch <= 0xD6)
            || (ch >= 0xD8 && ch <= 0xF6)
            || (ch >= 0xF8 && ch <= 0x2FF)
            || (ch >= 0x370 && ch <= 0x37D)
            || (ch >= 0x37F && ch <= 0x1FFF)
            || (ch >= 0x200C && ch <= 0x200D)
            || (ch >= 0x2070 && ch <= 0x218F)
            || (ch >= 0x2C00 && ch <= 0x2FEF)
            || (ch >= 0x3001 && ch <= 0xD7FF)
            || (ch >= 0xF900 && ch <= 0xFDCF)
            || (ch >= 0xFDF0 && ch <= 0xFFFD)
            || (ch >= 0x10000 && ch <= 0xEFFFF);
    }

    /**
     * The cached local name of this URI.
     */
    private String fLocalName;

    /**
     * The cached namespace of this URI.
     */
    private IId fNamespace;

    /**
     * This field is used to cache the resulting string representation of this
     * object.
     */
    private transient volatile String fUri;

    /**
     * Default constructor used for serialization/deserialization.
     */
    protected Name() {
    }

    /**
     * @param namespace
     * @param localName
     */
    /**
     * This constructor builds the internal URI using the given namespace and
     * local name. It checks that the namespace and local name are valid.
     * 
     * @param namespace the namespace of the URI
     * @param localName the local name
     */
    public Name(IId namespace, String localName) {
        checkLocalName(localName);
        fNamespace = namespace;
        fLocalName = localName;
    }

    /**
     * Returns the local name of this identifier
     * 
     * @return the local name of this identifier
     */
    public String getLocalName() {
        return fLocalName;
    }

    /**
     * Returns the namespace of this identifier. The namespace is never
     * <code>null</code>, but it could be an empty string.
     * 
     * @return the namespace of this identifier
     */
    public IId getNamespaceURL() {
        return fNamespace;
    }

    /**
     * @see org.webreformatter.ns.IId#getUrl()
     */
    public String getUrl() {
        if (fUri == null) {
            fUri = fNamespace != null ? fNamespace + fLocalName : fLocalName;
        }
        return fUri;
    }

}
