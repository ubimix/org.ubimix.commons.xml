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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.webreformatter.ns.IName;
import org.webreformatter.path.PathProcessor;
import org.webreformatter.xml.IXmlElement;
import org.webreformatter.xml.IXmlNode;

public abstract class AtomAbstractEntry extends AtomItem {

    protected static IName ATTR_HREF = getName("atom:href");

    protected static IName ATTR_REL = getName("atom:rel");

    protected static IName ATTR_TYPE = getName("atom:type");

    protected static PathProcessor SELECTOR_AUTHOR = getElementFilter("atom:author");

    protected static PathProcessor SELECTOR_CATEGORY = getElementFilter("atom:category");

    protected static PathProcessor SELECTOR_CONTRIBUTOR = getElementFilter("atom:contributor");

    protected static PathProcessor SELECTOR_LINK = getElementFilter("atom:link");

    protected static PathProcessor SELECTOR_LINK_SELF = getLinkSelector("self");

    protected static PathProcessor SELECTOR_PUBLISHED = getElementFilter("atom:published");

    protected static PathProcessor SELECTOR_SUMMARY = getElementFilter("atom:summary");

    protected static PathProcessor SELECTOR_TITLE = getElementFilter("atom:title");

    protected static PathProcessor SELECTOR_UPDATED = getElementFilter("atom:updated");

    protected static PathProcessor getLinkSelector(String string) {
        return newFilterBuilder().node().element("atom:link").attrs(
            "atom:rel",
            string).buildPath();
    }

    public AtomAbstractEntry(IXmlElement element) {
        super(element);
    }

    public List<AtomPerson> getAuthors() {
        return getPersonList(SELECTOR_AUTHOR);
    }

    public List<AtomCategory> getCategories() {
        List<AtomCategory> result = new ArrayList<AtomCategory>();
        List<IXmlNode> list = evalList(SELECTOR_CATEGORY);
        for (IXmlNode node : list) {
            AtomCategory category = new AtomCategory((IXmlElement) node);
            result.add(category);
        }
        return result;
    }

    public List<AtomPerson> getContributors() {
        return getPersonList(SELECTOR_CONTRIBUTOR);
    }

    public String getLink() {
        List<IXmlNode> list = evalList(SELECTOR_LINK);
        String result = null;
        for (IXmlNode node : list) {
            if (!(node instanceof IXmlElement))
                continue;
            IXmlElement e = (IXmlElement) node;
            String rel = e.getAttribute(ATTR_REL);
            if (rel != null)
                continue;
            result = e.getAttribute(ATTR_HREF);
            if (result != null)
                break;
        }
        return result;
    }

    public String getLink(String link) {
        PathProcessor selector = getLinkSelector(link);
        return evalStrAttr(selector, ATTR_HREF);
    }

    public String getLinkType(String link) {
        PathProcessor selector = getLinkSelector(link);
        return evalStrAttr(selector, ATTR_TYPE);
    }

    protected List<AtomPerson> getPersonList(PathProcessor selector) {
        List<AtomPerson> result = new ArrayList<AtomPerson>();
        List<IXmlNode> list = evalList(selector);
        for (IXmlNode node : list) {
            AtomPerson person = new AtomPerson((IXmlElement) node);
            result.add(person);
        }
        return result;
    }

    public Date getPublished() {
        return evalDate(SELECTOR_PUBLISHED);
    }

    public String getSelfLink() {
        return evalStrAttr(SELECTOR_LINK_SELF, ATTR_HREF);
    }

    public String getSummary() {
        return evalStr(SELECTOR_SUMMARY);
    }

    public String getTitle() {
        return evalStr(SELECTOR_TITLE);
    }

    public Date getUpdated() {
        return evalDate(SELECTOR_UPDATED);
    }

}