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

import org.webreformatter.rdf.parser.IErrorListener;

public class ErrorListener implements IErrorListener {

    public void onBadObjectLang(int row, int col) {
        //
    }

    public void onBadObjectType(int row, int col) {
        //
    }

    public void onBadPredicate(int row, int col) {
        //
    }

    public void onBadSubject(int row, int col) {
        //
    }

    public void onNoObject(int row, int col) {
        //
    }

    public void onNoPredicate(int row, int col) {
        //
    }

}