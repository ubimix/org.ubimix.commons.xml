/* ************************************************************************** *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * This file is licensed to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * ************************************************************************** */
package org.webreformatter.rdf.parser;

import org.webreformatter.ns.IId;

public interface ITripleListener {

    void onComment(String comment);

    void onDatatypeStatement(
        IId subject,
        IId predicate,
        String object,
        IId type);

    void onReferenceStatement(IId subject, IId predicate, IId object);

    void onStringStatement(
        IId subject,
        IId predicate,
        String object,
        String lang);
}