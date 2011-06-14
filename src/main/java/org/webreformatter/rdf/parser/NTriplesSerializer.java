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

/**
 * @author kotelnikov
 */
public final class NTriplesSerializer extends AbstractNTriplesSerializer {

    private StringBuilder fBuf = new StringBuilder();

    /**
     * 
     */
    public NTriplesSerializer() {
        super();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof NTriplesSerializer))
            return false;
        NTriplesSerializer o = (NTriplesSerializer) obj;
        return fBuf.equals(o.fBuf);
    }

    @Override
    public int hashCode() {
        return fBuf.hashCode();
    }

    @Override
    protected void print(String str) {
        fBuf.append(str);
    }

    public void reset() {
        fBuf.delete(0, fBuf.length());
    }

    @Override
    public String toString() {
        return fBuf.toString();
    }

}
