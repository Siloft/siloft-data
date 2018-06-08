/*
 * Copyright (c) 2018 Siloft
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.siloft.data;

/**
 * This class represents program data for loading and saving data in the program
 * data section of the current system. Supported systems are Windows, Linux, and
 * Mac OS.
 * <p>
 * The file path will be constructed:
 * <ul>
 * <li>Windows: %PROGRAMDATA%\[organisation]\[program]\[class name]</li>
 * <li>Linux: /etc/[organisation]/[program]/[class name]</li>
 * <li>Mac OS: /Library/Application Support/[organisation]\[program]\[class
 * name]</li>
 * </ul>
 * <p>
 * The following data types are allowed:
 * <ul>
 * <li><code>byte</code></li>
 * <li><code>short</code></li>
 * <li><code>int</code></li>
 * <li><code>long</code></li>
 * <li><code>float</code></li>
 * <li><code>double</code>n</li>
 * <li><code>boolean</code></li>
 * <li><code>String</code></li>
 * </ul>
 * <p>
 * To define fields in program data use the following parameter definition:
 * <code>public [TYPE] [NAME];</code>
 *
 * @author Sander Veldhuis
 */
public class ProgramData extends Data {

    /** The program data path for Windows. */
    private static final String PATH_WINDOWS =
            System.getenv("PROGRAMDATA") + "\\[org]\\[prg]";

    /** The program data path for Linux. */
    private static final String PATH_LINUX = "/etc/[org]/[prg]";

    /** The program data path for MacOS. */
    private static final String PATH_MAC =
            "/Library/Application Support/[org]/[prg]";

    /**
     * Constructs a new program data.
     *
     * @param organisation
     *            the organisation name
     * @param program
     *            the program name
     *
     * @exception IllegalArgumentException
     *                if any of the fields is not valid
     */
    protected ProgramData(String organisation, String program) {
        super((System.getProperty("os.name").toLowerCase().contains("win")
                ? PATH_WINDOWS
                : (System.getProperty("os.name").toLowerCase().contains("mac")
                        ? PATH_MAC : PATH_LINUX)).replace("[org]", organisation)
                                .replace("[prg]", program));
    }
}
