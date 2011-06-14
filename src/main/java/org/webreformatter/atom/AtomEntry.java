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

import org.webreformatter.path.PathProcessor;
import org.webreformatter.xml.IXmlElement;

public class AtomEntry extends AtomAbstractEntry {

    private static PathProcessor SELECTOR_CONTENT = getElementFilter("atom:content");

    protected static PathProcessor SELECTOR_LINK_ALTERNATE = getLinkSelector("alternate");

    protected static PathProcessor SELECTOR_LINK_EDIT = getLinkSelector("edit");

    public AtomEntry(IXmlElement element) {
        super(element);
    }

    public String getAlternateLink() {
        return evalStrAttr(SELECTOR_LINK_ALTERNATE, ATTR_HREF);
    }

    public String getContent() {
        return evalStr(SELECTOR_CONTENT);
    }

    public String getContentType() {
        IXmlElement e = evalElement(SELECTOR_CONTENT);
        return e != null ? e.getAttribute(ATTR_TYPE) : "text/plain";
    }

    public String getEditLink() {
        return evalStrAttr(SELECTOR_LINK_EDIT, ATTR_HREF);
    }

}