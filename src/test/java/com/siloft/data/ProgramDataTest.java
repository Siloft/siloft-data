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

import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Verifies whether the <code>ProgramData</code> class is working properly.
 *
 * @author Sander Veldhuis
 */
public class ProgramDataTest {

    /**
     * A test program data class with all supported parameter types.
     */
    public final class Options extends ProgramData {
        public Options() {
            super("siloft", "unittest");
        }

        public byte aByte = 1;
        public short aShort = 2;
        public int aInt = 3;
        public long aLong = 4;
        public float aFloat = 1.2345f;
        public double aDouble = 2.3456d;
        public boolean aBoolean = true;
        public String aString = "Test";
    }

    /**
     * Cleanup all created files after testing.
     *
     * @throws InterruptedException
     */
    @After
    public void cleanup() throws InterruptedException {
        new File(
                "C:\\Documents and Settings\\All Users\\Application Data\\siloft\\unittest\\Options")
                        .delete();
        new File("C:\\ProgramData\\siloft\\unittest\\Options").delete();
        new File("/etc/siloft/unittest/Options").delete();
        new File("/Library/Application Support/siloft/unittest/Options")
                .delete();
    }

    /**
     * Test the constructor with valid program data.
     */
    @Test
    public void testValidConstructor() {
        Options options = new Options();
        assert options.aByte == 1;
        assert options.aShort == 2;
        assert options.aInt == 3;
        assert options.aLong == 4;
        assert options.aFloat == 1.2345f;
        assert options.aDouble == 2.3456d;
        assert options.aBoolean == true;
        assert options.aString.equals("Test");
        assert (options.getFile().getAbsolutePath()
                .equals("C:\\Documents and Settings\\All Users\\Application Data\\siloft\\unittest\\Options") == true
                || options.getFile().getAbsolutePath()
                        .equals("C:\\ProgramData\\siloft\\unittest\\Options") == true
                || options.getFile().getAbsolutePath()
                        .equals("/etc/siloft/unittest/Options") == true
                || options.getFile().getAbsolutePath().equals(
                        "/Library/Application Support/siloft/unittest/Options") == true);
        assert (options.getDirectory().getAbsolutePath()
                .equals("C:\\Documents and Settings\\All Users\\Application Data\\siloft\\unittest") == true
                || options.getDirectory().getAbsolutePath()
                        .equals("C:\\ProgramData\\siloft\\unittest") == true
                || options.getDirectory().getAbsolutePath()
                        .equals("/etc/siloft/unittest") == true
                || options.getDirectory().getAbsolutePath().equals(
                        "/Library/Application Support/siloft/unittest") == true);
        assert options.isLoaded() == false;
    }

    /**
     * Test loading and saving.
     */
    @Test
    public void testLoadingSaving() {
        Options options1 = new Options();
        try {
            options1.load();
        } catch (IOException exception) {
            assert false;
        }
        assert options1.isLoaded() == true;
        assert options1.aByte == 1;
        assert options1.aShort == 2;
        assert options1.aInt == 3;
        assert options1.aLong == 4;
        assert options1.aFloat == 1.2345f;
        assert options1.aDouble == 2.3456d;
        assert options1.aBoolean == true;
        assert options1.aString.equals("Test");
        assert (new File(
                "C:\\Documents and Settings\\All Users\\Application Data\\siloft\\unittest\\Options")
                        .isFile() == true
                || new File("C:\\ProgramData\\siloft\\unittest\\Options")
                        .isFile() == true
                || new File("/etc/siloft/unittest/Options").isFile() == true
                || new File(
                        "/Library/Application Support/siloft/unittest/Options")
                                .isFile() == true);

        options1.aByte = 4;
        options1.aShort = 3;
        options1.aInt = 2;
        options1.aLong = 1;
        options1.aFloat = 2.3456f;
        options1.aDouble = 1.2345d;
        options1.aBoolean = false;
        options1.aString = "Tryout";
        try {
            options1.save();
        } catch (IOException exception1) {
            assert false;
        }

        Options options2 = new Options();
        try {
            options2.load();
        } catch (IOException exception) {
            assert false;
        }
        assert options2.aByte == 4;
        assert options2.aShort == 3;
        assert options2.aInt == 2;
        assert options2.aLong == 1;
        assert options2.aFloat == 2.3456f;
        assert options2.aDouble == 1.2345d;
        assert options2.aBoolean == false;
        assert options2.aString.equals("Tryout");
    }
}
