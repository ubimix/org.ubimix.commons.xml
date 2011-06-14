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

/**
 * @author kotelnikov
 */
public abstract class AbstractNTriplesSerializer implements ITripleListener {

    /**
     * 
     */
    public AbstractNTriplesSerializer() {
        super();
    }

    protected String escape(String str) {
        str = str.replace("\\", "\\\\");
        str = str.replace("\r\n", "\\n");
        str = str.replace("\n\\r", "\\n");
        str = str.replace("\r", "\\n");
        str = str.replace("\n", "\\n");
        str = str.replace("\t", "\\t");
        return str;
    }

    public void onComment(String comment) {
        print("#");
        comment = escape(comment);
        println(comment);
    }

    public void onDatatypeStatement(
        IId subject,
        IId predicate,
        String object,
        IId type) {
        printReference(subject);
        printSpace();
        printReference(predicate);
        printSpace();
        printLiteral(object);
        if (type != null) {
            print("^^");
            printReference(type);
        }
        println(" .");
    }

    public void onReferenceStatement(IId subject, IId predicate, IId object) {
        printReference(subject);
        printSpace();
        printReference(predicate);
        printSpace();
        printReference(object);
        println(" .");
    }

    public void onStringStatement(
        IId subject,
        IId predicate,
        String object,
        String lang) {
        printReference(subject);
        printSpace();
        printReference(predicate);
        printSpace();
        printLiteral(object);
        if (lang != null) {
            print("@");
            print(lang);
        }
        println(" .");
    }

    protected abstract void print(String str);

    protected void printEOL() {
        print("\n");
    }

    private void printLiteral(String object) {
        object = escape(object);
        object = object.replace("\'", "\\\'");
        object = object.replace("\"", "\\\"");
        print("\"");
        print(object);
        print("\"");
    }

    protected void println(String str) {
        print(str);
        printEOL();
    }

    protected void printReference(IId ref) {
        String str = ref.toString();
        if (str.startsWith("_:")) {
            print(str);
        } else {
            print("<");
            print(str);
            print(">");
        }
    }

    protected void printSpace() {
        print(" ");
    }

}
