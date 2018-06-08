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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Verifies whether the <code>Data</code> class is working properly.
 *
 * @author Sander Veldhuis
 */
public class DataTest {

    /**
     * A test data class with invalid parameter type.
     */
    public final class InvalidType extends Data {

        public InvalidType() {
            super(System.getProperty("user.home"));
        }

        public char aChar = 0;
    }

    /**
     * A test data class with invalid parameter access.
     */
    public final class InvalidPublic extends Data {

        public InvalidPublic() {
            super(System.getProperty("user.home"));
        }

        byte aByte = 0;
    }

    /**
     * A test data class with invalid static parameter.
     */
    public final class InvalidStatic extends Data {

        public InvalidStatic() {
            super(System.getProperty("user.home"));
        }

        public static final byte aByte = 0;
    }

    /**
     * A test data class with invalid transient parameter.
     */
    public final class InvalidTransient extends Data {

        public InvalidTransient() {
            super(System.getProperty("user.home"));
        }

        public transient byte aByte = 0;
    }

    /**
     * A test data class with invalid volatile parameter.
     */
    public final class InvalidVolatile extends Data {

        public InvalidVolatile() {
            super(System.getProperty("user.home"));
        }

        public volatile byte aByte = 0;
    }

    /**
     * A test data class with invalid final parameter.
     */
    public final class InvalidFinal extends Data {

        public InvalidFinal() {
            super(System.getProperty("user.home"));
        }

        public final byte aByte = 0;
    }

    /**
     * A test data class with all supported parameter types.
     */
    public final class Options extends Data {

        public Options() {
            super(System.getProperty("user.dir"));
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
        new File(System.getProperty("user.dir"), "Options").delete();
    }

    /**
     * Test the constructor with valid data.
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
        assert options.getFile().getAbsolutePath().equals(
                System.getProperty("user.dir") + File.separator + "Options");
        assert options.getDirectory().getAbsolutePath()
                .equals(System.getProperty("user.dir"));
        assert options.isLoaded() == false;
    }

    /**
     * Test the constructor with invalid data.
     */
    @Test
    public void testInvalidConstructor() {
        try {
            new InvalidType();
        } catch (Exception e) {
            assert e.getClass() == IllegalArgumentException.class;
            assert e.getMessage() == "Field type not valid";
        }

        try {
            new InvalidPublic();
        } catch (Exception e) {
            assert e.getClass() == IllegalArgumentException.class;
            assert e.getMessage() == "Field should be public";
        }

        try {
            new InvalidStatic();
        } catch (Exception e) {
            assert e.getClass() == IllegalArgumentException.class;
            assert e.getMessage() == "Field should not be static";
        }

        try {
            new InvalidTransient();
        } catch (Exception e) {
            assert e.getClass() == IllegalArgumentException.class;
            assert e.getMessage() == "Field should not be transient";
        }

        try {
            new InvalidVolatile();
        } catch (Exception e) {
            assert e.getClass() == IllegalArgumentException.class;
            assert e.getMessage() == "Field should not be volatile";
        }

        try {
            new InvalidFinal();
        } catch (Exception e) {
            assert e.getClass() == IllegalArgumentException.class;
            assert e.getMessage() == "Field should not be final";
        }
    }

    /**
     * Test setting the defaults.
     */
    @Test
    public void testDefaults() {
        Options options = new Options();
        try {
            options.load();
        } catch (IOException exception) {
            assert false;
        }
        assert options.aByte == 1;
        assert options.aShort == 2;
        assert options.aInt == 3;
        assert options.aLong == 4;
        assert options.aFloat == 1.2345f;
        assert options.aDouble == 2.3456d;
        assert options.aBoolean == true;
        assert options.aString.equals("Test");

        options.aByte = 0;
        options.aShort = 0;
        options.aInt = 0;
        options.aLong = 0;
        options.aFloat = 0f;
        options.aDouble = 0d;
        options.aBoolean = false;
        options.aString = "";
        assert options.aByte == 0;
        assert options.aShort == 0;
        assert options.aInt == 0;
        assert options.aLong == 0;
        assert options.aFloat == 0f;
        assert options.aDouble == 0d;
        assert options.aBoolean == false;
        assert options.aString.equals("");

        options.setDefaults();
        assert options.aByte == 1;
        assert options.aShort == 2;
        assert options.aInt == 3;
        assert options.aLong == 4;
        assert options.aFloat == 1.2345f;
        assert options.aDouble == 2.3456d;
        assert options.aBoolean == true;
        assert options.aString.equals("Test");
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
        assert new File(System.getProperty("user.dir"), "Options")
                .isFile() == true;

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

    /**
     * Test loading and saving with invalid values.
     */
    @Test
    public void testLoadingWithErrors() {
        String data = "ignored\naByte=\naString=a=b\naByte=s2";
        File file = new File(System.getProperty("user.dir"), "Options");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(data);
            writer.close();
        } catch (IOException exception) {
            // Ignore
        }

        Options options = new Options();
        try {
            options.load();
        } catch (IOException exception) {
            assert false;
        }
        assert options.aByte == 1;
        assert options.aShort == 2;
        assert options.aInt == 3;
        assert options.aLong == 4;
        assert options.aFloat == 1.2345f;
        assert options.aDouble == 2.3456d;
        assert options.aBoolean == true;
        assert options.aString.equals("a=b");
    }
}
