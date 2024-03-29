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
package org.ubimix.commons.xml.atom;

import org.w3c.dom.Node;
import org.ubimix.commons.xml.XmlException;
import org.ubimix.commons.xml.XmlWrapper;

/**
 * @author kotelnikov
 */
public class AtomPerson extends AtomItem {

    public AtomPerson(Node node, XmlContext context) {
        super(node, context);
    }

    public AtomPerson(XmlWrapper wrapper) {
        super(wrapper);
    }

    public String getEmail() throws XmlException {
        return evalStr("atom:email");
    }

    public String getName() throws XmlException {
        return evalStr("atom:name");
    }

}
