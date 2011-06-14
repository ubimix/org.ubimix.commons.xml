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

import java.util.Iterator;

import org.webreformatter.ns.IId;
import org.webreformatter.ns.NamespaceManager;

/**
 * @author kotelnikov
 */
public class NTriplesParser {

    protected static final char NULL = '\0';

    protected IErrorListener fErrorListener;

    protected ITripleListener fListener;

    private NamespaceManager fNamespaceManager;

    /**
     * 
     */
    public NTriplesParser(ITripleListener listener) {
        this(NamespaceManager.INSTANCE, listener, new ErrorListener());
    }

    public NTriplesParser(NamespaceManager manager, ITripleListener listener) {
        this(manager, listener, new ErrorListener());
    }

    /**
     * 
     */
    public NTriplesParser(
        NamespaceManager manager,
        ITripleListener listener,
        IErrorListener errorListener) {
        super();
        fNamespaceManager = manager;
        fListener = listener;
        fErrorListener = errorListener;
    }

    private int appendEncodedChar(
        char[] line,
        int i,
        StringBuilder buf,
        int encodingLen) {
        char ch = '\0';
        int start = i;
        int end = Math.min(line.length, start + encodingLen);
        for (; i < end; i++) {
            ch = line[i];
            if ((ch >= '0' && ch <= '9')
                || (ch >= 'a' && ch <= 'f')
                || (ch >= 'A' && ch <= 'F')) {
                continue;
            }
            break;
        }
        if (i > start) {
            String str = new String(line, start, i - start);
            int code = Integer.parseInt(str, 16);
            ch = (char) code;
        }
        if (buf != null) {
            buf.append(ch);
        }
        return i;
    }

    private int appendEscapedSymbol(char[] array, int i, StringBuilder buf) {
        if (i >= array.length)
            return i;
        char ch = array[i];
        boolean appended = false;
        i++;
        switch (ch) {
            case 't':
                ch = '\t';
                break;
            case 'n':
                ch = '\n';
                break;
            case 'r':
                ch = '\r';
                break;
            case '\\':
                ch = '\\';
                break;
            case 'u':
                i = appendEncodedChar(array, i, buf, 4);
                appended = true;
                break;
            case 'U':
                i = appendEncodedChar(array, i, buf, 8);
                appended = true;
                break;
            case 'x':
                i = appendEncodedChar(array, i, buf, 2);
                appended = true;
                break;
        }
        if (!appended && buf != null) {
            buf.append(ch);
        }
        return i;
    }

    protected NamespaceManager getNamespaceManager() {
        return fNamespaceManager;
    }

    /**
     * @param ch the character to check
     * @return <code>true</code> if the specified character is a quotation
     *         character
     */
    protected char getQuot(char ch) {
        return isQuot(ch) ? ch : NULL;
    }

    protected boolean isQuot(char ch) {
        return ch == '\'' || ch == '"';
    }

    /**
     * @param ch the character to check
     * @return <code>true</code> if the specified character is a space character
     */
    protected boolean isSpace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r';
    }

    public void parse(Iterable<String> iterable) {
        parse(iterable.iterator());
    }

    public void parse(Iterator<String> iterator) {
        int n = 0;
        while (iterator.hasNext()) {
            String line = iterator.next();
            parseLine(n++, line);
        }
    }

    public void parse(String str) {
        String[] lines = str.split("[\r\n]+");
        int n = 0;
        for (String line : lines) {
            parseLine(n++, line);
        }
    }

    public void parseLine(int row, String line) {
        char[] array = line.toCharArray();
        int col = skipSpaces(array, 0);
        if (col >= array.length) // Just an empty line
            return;
        char ch = array[col];
        if (ch == '#') { // A comment line
            int len = array.length - col - 1;
            String comment = len > 0 ? new String(array, col + 1, len) : "";
            comment = unescape(comment);
            fListener.onComment(comment);
            return;
        }
        StringBuilder buf = new StringBuilder();
        int prev = col;
        col = skipReference(array, col, buf);
        if (prev == col) {
            fErrorListener.onBadSubject(row, col);
            return;
        }
        String subject = buf.toString();

        col = skipSpaces(array, col);
        if (col >= array.length) {
            fErrorListener.onNoPredicate(row, col);
            return;
        }

        prev = col;
        buf.delete(0, buf.length());
        col = skipReference(array, col, buf);
        if (prev == col) {
            fErrorListener.onBadPredicate(row, col);
            return;
        }
        String predicate = buf.toString();

        col = skipSpaces(array, col);
        if (col >= array.length) {
            fErrorListener.onNoObject(row, col);
            return;
        }

        String object = null;
        String lang = null;
        String type = null;
        boolean reference = false;
        prev = col;
        buf.delete(0, buf.length());
        col = skipReference(array, col, buf);
        if (prev < col) {
            object = buf.toString();
            reference = true;
        } else {
            buf.delete(0, buf.length());
            col = skipQuot(array, col, buf);
            if (prev == col) {
                fErrorListener.onNoObject(row, col);
                return;
            }
            object = buf.toString();
            object = unescape(object);
            buf.delete(0, buf.length());
            if (col != prev && col < array.length - 1) {
                col++;
                col = skipSpaces(array, col);
                if (array[col] == '^'
                    && col < array.length - 1
                    && array[col + 1] == '^') {
                    col = skipReference(array, col + 2, buf);
                    type = buf.toString();
                    if (type.length() == 0) {
                        fErrorListener.onBadObjectType(row, col - 2);
                    }
                } else if (array[col] == '@') {
                    int i = col;
                    for (col++; col < array.length
                        && !isSpace(array[col])
                        && array[col] != '.'; col++) {
                        buf.append(array[col]);
                    }
                    lang = buf.toString();
                    if (lang.length() == 0) {
                        fErrorListener.onBadObjectLang(row, i);
                    }
                }
            }
        }
        IId subjectId = fNamespaceManager.getId(subject);
        IId predicateId = fNamespaceManager.getId(predicate);
        if (reference) {
            IId objectId = fNamespaceManager.getId(object);
            fListener.onReferenceStatement(subjectId, predicateId, objectId);
        } else {
            if (type == null) {
                fListener.onStringStatement(
                    subjectId,
                    predicateId,
                    object,
                    lang);
            } else {
                IId typeId = fNamespaceManager.getId(type);
                fListener.onDatatypeStatement(
                    subjectId,
                    predicateId,
                    object,
                    typeId);
            }
        }
    }

    protected void setNamespaceManager(NamespaceManager namespaceManager) {
        fNamespaceManager = namespaceManager;
    }

    protected int skipQuot(char[] array, int pos) {
        if (pos >= array.length)
            return pos;
        char quot = getQuot(array[pos]);
        if (quot == NULL)
            return pos;
        for (pos++; quot != NULL && pos < array.length; pos++) {
            char ch = array[pos];
            if (quot == ch) {
                quot = NULL;
                break;
            }
        }
        return pos;
    }

    protected int skipQuot(char[] array, int pos, StringBuilder buf) {
        if (pos >= array.length)
            return pos;
        char quot = getQuot(array[pos]);
        if (quot == NULL)
            return pos;
        boolean escaped = false;
        pos++;
        while (pos < array.length) {
            char ch = array[pos];
            if (escaped) {
                int start = pos;
                pos = appendEscapedSymbol(array, pos, null);
                buf.append(array, start, pos - start);
                escaped = false;
            } else {
                escaped = ch == '\\';
                if (quot == ch) {
                    break;
                }
                buf.append(ch);
                pos++;
            }
        }
        return pos;
    }

    private int skipReference(char[] array, int i, StringBuilder buf) {
        if (i >= array.length)
            return i;
        int result = i;
        if (array[i] == '<') {
            for (++i; i < array.length; i++) {
                if (array[i] == '>') {
                    i++;
                    result = i;
                    break;
                }
                buf.append(array[i]);
            }
        } else if (array[i] != '\'' && array[i] != '\"') {
            for (; i < array.length && !isSpace(array[i]); i++) {
                buf.append(array[i]);
            }
            result = i;
        }
        return result;
    }

    protected int skipSpaces(char[] array, int pos) {
        for (; pos < array.length && isSpace(array[pos]); pos++) {
        }
        return pos;
    }

    private String unescape(String line) {
        StringBuilder buf = new StringBuilder();
        char[] array = line.toCharArray();
        boolean escaped = false;
        int i = 0;
        while (i < array.length) {
            char ch = line.charAt(i);
            if (escaped) {
                escaped = false;
                i = appendEscapedSymbol(array, i, buf);
            } else {
                if (ch == '\\') {
                    escaped = true;
                } else {
                    buf.append(ch);
                }
                i++;
            }
        }
        return buf.toString();
    }
}
