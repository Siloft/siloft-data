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
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
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

import java.io.IOException;

/**
 * Class containing an example implementation of the usage of user data and
 * program data.
 *
 * @author Sander Veldhuis
 */
public class DataExample {

    /**
     * Entry method to start this application.
     *
     * @param args
     *            arguments for this application
     */
    public static void main(String[] args) {
        AppVersion appVersion = new AppVersion();
        Options options = new Options();

        try {
            appVersion.load();

            System.out.println(
                    appVersion.version_major + "." + appVersion.version_minor
                            + "." + appVersion.version_build);

            appVersion.version_major++;
            appVersion.version_minor++;
            appVersion.version_build++;

            appVersion.save();

            System.out.println(
                    appVersion.version_major + "." + appVersion.version_minor
                            + "." + appVersion.version_build);
        } catch (IOException e) {
            System.err.println(
                    "Failed loading/saving app version: " + e.getMessage());
        }

        try {
            options.load();

            System.out
                    .println(options.screenWidth + "x" + options.screenHeight);
            System.out.println(options.username);

            options.screenWidth++;
            options.screenHeight++;
            options.username += "0";

            options.save();

            System.out
                    .println(options.screenWidth + "x" + options.screenHeight);
            System.out.println(options.username);
        } catch (IOException e) {
            System.err.println(
                    "Failed loading/saving options: " + e.getMessage());
        }
    }
}
