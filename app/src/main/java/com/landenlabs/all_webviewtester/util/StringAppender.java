/*
 * Copyright (c) 2015 Dennis Lang (LanDen Labs) landenlabs@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 *  following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *  @author Dennis Lang  (3/21/2015)
 *  @see http://landenlabs.com/
 */

package com.landenlabs.all_webviewtester.util;

import java.util.ArrayList;

/**
 * Optimized String Appender. Fewer allocation calls then StringBuilder or StringBuffer.
 * Created by Dennis Lang on 8/28/2015.
 */
public class StringAppender extends ArrayList<CharSequence> {

    private char[] m_allParts;

    /***
     * Start building a string.
     * @param charseq
     * @return this
     */
    public StringAppender start(CharSequence charseq) {
        clear();
        return append(charseq.toString());
    }

    /***
     * Append to working string
     * @param charseq
     * @return this
     */
    public StringAppender append(CharSequence charseq) {
        add(charseq.toString());
        return this;
    }

    /***
     * @return length of all parts.
     */
    public int length() {
        int length = 0;
        for (CharSequence str : this) {
            length += str.length();
        }
        return length;
    }

    /***
     * @return combination of parts.
     */
    public String toString() {
        int totalLength = length();
        if (totalLength == 0)
            return "";

        if (m_allParts == null || m_allParts.length < totalLength)
            m_allParts = new char[totalLength];

        int pos = 0;
        for (CharSequence str : this) {
            for (int idx = 0; idx < str.length(); idx++)
                m_allParts[pos++] = str.charAt(idx);
        }

        // return String.valueOf(m_allParts);
        return new String(m_allParts, 0, totalLength);
    }
}
