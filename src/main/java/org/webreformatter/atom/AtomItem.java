/*
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. This file is licensed to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.webreformatter.atom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.webreformatter.ns.IName;
import org.webreformatter.ns.NamespaceManager;
import org.webreformatter.path.IPathNodeCollector;
import org.webreformatter.path.PathProcessor;
import org.webreformatter.path.xml.XmlSelectorBuilder;
import org.webreformatter.xml.IXmlElement;
import org.webreformatter.xml.IXmlNode;
import org.webreformatter.xml.TextSerializer;

public abstract class AtomItem {

    static public SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss'Z'");

    static public SimpleDateFormat[] DATE_FORMATS = {
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"),
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"),
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ"),

        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"),
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"),
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm"),

        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"),
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'") };

    protected static NamespaceManager NAMESPACE_MANAGER = new NamespaceManager();

    public static final String NS_ATOM = "http://www.w3.org/2005/Atom";

    public static final String PREFIX_ATOM = "atom";

    private static PathProcessor SELECTOR_ID;

    static {
        NAMESPACE_MANAGER.setNamespacePrefix(NS_ATOM, PREFIX_ATOM);
        NAMESPACE_MANAGER.setNamespacePrefix("", "");
        SELECTOR_ID = newFilterBuilder().node().element("atom:id").buildPath();
    }

    public static IXmlElement evalElement(
        IXmlElement node,
        PathProcessor selector) {
        final IXmlElement[] result = { null };
        IPathNodeCollector collector = new IPathNodeCollector() {
            public boolean setResult(Object node) {
                if (!(node instanceof IXmlElement))
                    return true;
                result[0] = (IXmlElement) node;
                return false;
            }
        };
        selector.select(node, collector);
        return result[0];
    }

    public static List<IXmlNode> evalList(
        IXmlElement node,
        PathProcessor selector) {
        final List<IXmlNode> result = new ArrayList<IXmlNode>();
        IPathNodeCollector collector = new IPathNodeCollector() {
            public boolean setResult(Object node) {
                result.add((IXmlNode) node);
                return true;
            }
        };
        selector.select(node, collector);
        return result;
    }

    public static IXmlNode evalNode(IXmlElement node, PathProcessor selector) {
        final IXmlNode[] result = { null };
        IPathNodeCollector collector = new IPathNodeCollector() {
            public boolean setResult(Object node) {
                result[0] = (IXmlNode) node;
                return false;
            }
        };
        selector.select(node, collector);
        return result[0];
    }

    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    protected static PathProcessor getDeepElementFilter(String name) {
        return newFilterBuilder().element(name, true).buildPath();
    }

    protected static PathProcessor getElementFilter(String name) {
        return newFilterBuilder().node().element(name).buildPath();
    }

    protected static IName getName(String string) {
        return NAMESPACE_MANAGER.getName(string);
    }

    public static XmlSelectorBuilder newFilterBuilder() {
        return new XmlSelectorBuilder(NAMESPACE_MANAGER);
    }

    public static Date parseDate(String date) {
        if (date == null)
            return null;
        date = date.trim();
        if ("".equals(date))
            return null;
        Date result = null;
        for (SimpleDateFormat format : DATE_FORMATS) {
            try {
                result = format.parse(date);
                break;
            } catch (ParseException e) {
            }
        }
        return result;
    }

    public static String toString(IXmlNode node) {
        if (node == null)
            return null;
        TextSerializer serializer = new TextSerializer(false);
        serializer.serializeNode(node);
        return serializer.toString();
    }

    protected IXmlElement fRoot;

    public AtomItem(IXmlElement element) {
        setRoot(element);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof AtomItem))
            return false;
        AtomItem o = (AtomItem) obj;
        return fRoot.equals(o.fRoot);
    }

    public Date evalDate(PathProcessor filters) {
        String str = evalStr(filters);
        Date date = str != null ? parseDate(str) : null;
        return date;
    }

    public IXmlElement evalElement(PathProcessor filters) {
        return evalElement(fRoot, filters);
    }

    public List<IXmlNode> evalList(PathProcessor filters) {
        return evalList(fRoot, filters);
    }

    public String evalStr(PathProcessor filters) {
        IXmlNode node = evalNode(fRoot, filters);
        return toString(node);
    }

    public String evalStrAttr(IName attr) {
        return evalStrAttr(null, attr);
    }

    public String evalStrAttr(PathProcessor filters, IName attr) {
        IXmlElement node = filters != null ? evalElement(filters) : fRoot;
        return node != null ? node.getAttribute(attr) : null;
    }

    public String getId() {
        return evalStr(SELECTOR_ID);
    }

    @Override
    public int hashCode() {
        return fRoot.hashCode();
    }

    protected void setRoot(IXmlElement element) {
        fRoot = element;
    }

    @Override
    public String toString() {
        return fRoot.toString();
    }

}