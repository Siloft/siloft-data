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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents data for loading and saving data in the current system.
 * Supported systems are Windows, Linux, and Mac OS.
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
 * To define fields in the data use the following parameter definition:
 * <code>public [TYPE] [NAME];</code>
 *
 * @author Sander Veldhuis
 */
class Data {

    /** The name of the <code>byte</code> type. */
    private static final String BYTE_TYPE = "byte";

    /** The name of the <code>short</code> type. */
    private static final String SHORT_TYPE = "short";

    /** The name of the <code>int</code> type. */
    private static final String INT_TYPE = "int";

    /** The name of the <code>long</code> type. */
    private static final String LONG_TYPE = "long";

    /** The name of the <code>float</code> type. */
    private static final String FLOAT_TYPE = "float";

    /** The name of the <code>double</code> type. */
    private static final String DOUBLE_TYPE = "double";

    /** The name of the <code>boolean</code> type. */
    private static final String BOOLEAN_TYPE = "boolean";

    /** The name of the <code>String</code> type. */
    private static final String STRING_TYPE = "java.lang.String";

    /** The default data key-value pairs. */
    private final Map<String, Object> defaultData;

    /** The file for the data. */
    private final File file;

    /** Indicates whether the data is loaded. */
    private boolean isLoaded;

    /**
     * Constructs a new data.
     *
     * @param path
     *            the path of the data
     *
     * @exception IllegalArgumentException
     *                if any of the fields is not valid
     */
    protected Data(String path) {
        defaultData = new HashMap<String, Object>();
        file = new File(path, this.getClass().getSimpleName());
        validateFields();
    }

    /**
     * Loads the data from the related file.
     *
     * @throws IOException
     *             if loading the data failed
     */
    public void load() throws IOException {
        storeDefaults();

        if (!file.isFile()) {
            save();
        }

        FileInputStream inputStream = new FileInputStream(file);
        InputStreamReader inputReader =
                new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputReader);

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] option = line.split("=", 2);
            if (option.length < 2) {
                continue;
            }

            try {
                Field field = getClass().getDeclaredField(option[0]);
                String typeName = field.getGenericType().getTypeName();
                if (typeName.equals(BYTE_TYPE)) {
                    field.setByte(this, Byte.parseByte(option[1]));
                } else if (typeName.equals(SHORT_TYPE)) {
                    field.setShort(this, Short.parseShort(option[1]));
                } else if (typeName.equals(INT_TYPE)) {
                    field.setInt(this, Integer.parseInt(option[1]));
                } else if (typeName.equals(LONG_TYPE)) {
                    field.setLong(this, Long.parseLong(option[1]));
                } else if (typeName.equals(FLOAT_TYPE)) {
                    field.setFloat(this, Float.parseFloat(option[1]));
                } else if (typeName.equals(DOUBLE_TYPE)) {
                    field.setDouble(this, Double.parseDouble(option[1]));
                } else if (typeName.equals(BOOLEAN_TYPE)) {
                    field.setBoolean(this, Boolean.parseBoolean(option[1]));
                } else {
                    field.set(this, option[1].replace("\\n", "\n"));
                }
            } catch (Exception e) {
                // Ignore
            }
        }

        bufferedReader.close();
        inputReader.close();
        inputStream.close();

        isLoaded = true;
    }

    /**
     * Saves the data to the related file.
     *
     * @throws IOException
     *             if saving the data failed
     */
    public void save() throws IOException {
        storeDefaults();

        file.getParentFile().mkdirs();

        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        for (Field field : getClass().getDeclaredFields()) {
            if (field.isSynthetic()) {
                continue;
            }

            try {
                String name = field.getName();
                String typeName = field.getGenericType().getTypeName();

                if (typeName.equals(BYTE_TYPE)) {
                    bufferedWriter.write(name + "=" + field.getByte(this));
                } else if (typeName.equals(SHORT_TYPE)) {
                    bufferedWriter.write(name + "=" + field.getShort(this));
                } else if (typeName.equals(INT_TYPE)) {
                    bufferedWriter.write(name + "=" + field.getInt(this));
                } else if (typeName.equals(LONG_TYPE)) {
                    bufferedWriter.write(name + "=" + field.getLong(this));
                } else if (typeName.equals(FLOAT_TYPE)) {
                    bufferedWriter.write(name + "=" + field.getFloat(this));
                } else if (typeName.equals(DOUBLE_TYPE)) {
                    bufferedWriter.write(name + "=" + field.getDouble(this));
                } else if (typeName.equals(BOOLEAN_TYPE)) {
                    bufferedWriter.write(name + "=" + field.getBoolean(this));
                } else {
                    bufferedWriter.write(name + "=" + field.get(this));
                }
                bufferedWriter.newLine();
            } catch (Exception e) {
                // Should not happen
            }
        }

        bufferedWriter.close();
        fileWriter.close();
    }

    /**
     * Set all data to the default values.
     */
    public void setDefaults() {
        storeDefaults();

        for (Field field : getClass().getDeclaredFields()) {
            if (field.isSynthetic()) {
                continue;
            }

            try {
                String name = field.getName();
                String typeName = field.getGenericType().getTypeName();

                if (typeName.equals(BYTE_TYPE)) {
                    field.setByte(this, (byte) defaultData.get(name));
                } else if (typeName.equals(SHORT_TYPE)) {
                    field.setShort(this, (short) defaultData.get(name));
                } else if (typeName.equals(INT_TYPE)) {
                    field.setInt(this, (int) defaultData.get(name));
                } else if (typeName.equals(LONG_TYPE)) {
                    field.setLong(this, (long) defaultData.get(name));
                } else if (typeName.equals(FLOAT_TYPE)) {
                    field.setFloat(this, (float) defaultData.get(name));
                } else if (typeName.equals(DOUBLE_TYPE)) {
                    field.setDouble(this, (double) defaultData.get(name));
                } else if (typeName.equals(BOOLEAN_TYPE)) {
                    field.setBoolean(this, (boolean) defaultData.get(name));
                } else {
                    field.set(this, defaultData.get(name));
                }
            } catch (Exception e) {
                // Should not happen
            }
        }
    }

    /**
     * Returns the file of this data.
     *
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * Returns the file directory of this data.
     *
     * @return the file directory
     */
    public File getDirectory() {
        return file.getParentFile();
    }

    /**
     * Indicates whether this data is loaded.
     *
     * @return <code>true</code> if loaded, or <code>false</code> otherwise
     */
    public boolean isLoaded() {
        return isLoaded;
    }

    /**
     * Store all data default values.
     */
    private void storeDefaults() {
        if (isLoaded) {
            return;
        }

        for (Field field : getClass().getDeclaredFields()) {
            if (field.isSynthetic()) {
                continue;
            }

            try {
                String name = field.getName();
                String typeName = field.getGenericType().getTypeName();

                if (typeName.equals(BYTE_TYPE)) {
                    defaultData.put(name, field.getByte(this));
                } else if (typeName.equals(SHORT_TYPE)) {
                    defaultData.put(name, field.getShort(this));
                } else if (typeName.equals(INT_TYPE)) {
                    defaultData.put(name, field.getInt(this));
                } else if (typeName.equals(LONG_TYPE)) {
                    defaultData.put(name, field.getLong(this));
                } else if (typeName.equals(FLOAT_TYPE)) {
                    defaultData.put(name, field.getFloat(this));
                } else if (typeName.equals(DOUBLE_TYPE)) {
                    defaultData.put(name, field.getDouble(this));
                } else if (typeName.equals(BOOLEAN_TYPE)) {
                    defaultData.put(name, field.getBoolean(this));
                } else {
                    defaultData.put(name, field.get(this));
                }
            } catch (Exception e) {
                // Should not happen
            }
        }
    }

    /**
     * Validate all fields of this data.
     *
     * @exception IllegalArgumentException
     *                if any of the fields is not valid
     */
    private void validateFields() {
        for (Field field : getClass().getDeclaredFields()) {
            if (field.isSynthetic()) {
                continue;
            }

            int modifiers = field.getModifiers();
            String typeName = field.getGenericType().getTypeName();

            if (!Modifier.isPublic(modifiers)) {
                throw new IllegalArgumentException("Field should be public");
            } else if (Modifier.isStatic(modifiers)) {
                throw new IllegalArgumentException(
                        "Field should not be static");
            } else if (Modifier.isTransient(modifiers)) {
                throw new IllegalArgumentException(
                        "Field should not be transient");
            } else if (Modifier.isVolatile(modifiers)) {
                throw new IllegalArgumentException(
                        "Field should not be volatile");
            } else if (Modifier.isFinal(modifiers)) {
                throw new IllegalArgumentException("Field should not be final");
            } else if (!typeName.equals(BYTE_TYPE)
                    && !typeName.equals(SHORT_TYPE)
                    && !typeName.equals(INT_TYPE) && !typeName.equals(LONG_TYPE)
                    && !typeName.equals(FLOAT_TYPE)
                    && !typeName.equals(DOUBLE_TYPE)
                    && !typeName.equals(BOOLEAN_TYPE)
                    && !typeName.equals(STRING_TYPE)) {
                throw new IllegalArgumentException("Field type not valid");
            }
        }
    }
}
