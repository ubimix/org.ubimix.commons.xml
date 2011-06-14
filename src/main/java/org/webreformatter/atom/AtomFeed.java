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
import java.util.List;

import org.webreformatter.path.PathProcessor;
import org.webreformatter.xml.IXmlDocument;
import org.webreformatter.xml.IXmlElement;
import org.webreformatter.xml.IXmlNode;

public class AtomFeed extends AtomAbstractEntry {

    private static PathProcessor SELECTOR_ENTRY = getDeepElementFilter("atom:entry");

    private static PathProcessor SELECTOR_FEED = getDeepElementFilter("atom:feed");

    private static PathProcessor SELECTOR_SUBTITLE = getElementFilter("atom:subtitle");

    public AtomFeed(IXmlDocument doc) {
        this(doc.getRootElement());
    }

    public AtomFeed(IXmlElement root) {
        super(evalElement(root, SELECTOR_FEED));
    }

    public List<AtomEntry> getEntries() {
        List<AtomEntry> result = new ArrayList<AtomEntry>();
        List<IXmlNode> list = evalList(SELECTOR_ENTRY);
        for (IXmlNode node : list) {
            AtomEntry entry = new AtomEntry((IXmlElement) node);
            result.add(entry);
        }
        return result;
    }

    public String getSubtitle() {
        return evalStr(SELECTOR_SUBTITLE);
    }

}