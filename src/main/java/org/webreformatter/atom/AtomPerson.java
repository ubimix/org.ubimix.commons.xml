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

/**
 * @author kotelnikov
 */
public class AtomPerson extends AtomItem {

    private static PathProcessor SELECTOR_EMAIL = getElementFilter("atom:email");

    private static PathProcessor SELECTOR_NAME = getElementFilter("atom:name");

    public AtomPerson(IXmlElement element) {
        super(element);
    }

    public String getEmail() {
        return evalStr(SELECTOR_EMAIL);
    }

    public String getName() {
        return evalStr(SELECTOR_NAME);
    }

}
