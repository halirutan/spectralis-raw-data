/*
 * Copyright (c) 2018 Patrick Scheibe
 * Affiliation: Saxonian Incubator for Clinical Translation, University Leipzig, Germany
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

package de.halirutan.spectralis.filestructure;

/**
 * All available BScan versions
 * (c) Patrick Scheibe 2017
 */
public enum BScanVersion {
    HSF_BS_100("HSF-BS-100", 0),
    HSF_BS_101("HSF-BS-101", 1),
    HSF_BS_102("HSF-BS-102", 2),
    HSF_BS_103("HSF-BS-103", 3),
    HSF_BS_104("HSF-BS-104", 4),
    INVALID("Invalid", -1);

    private final String versionString;
    private final int version;

    BScanVersion(String name, int number) {
        versionString = name;
        version = number;
    }

    public static BScanVersion parseVersionString(String name) {
        if(name == null) return INVALID;
        for (BScanVersion v : BScanVersion.values()) {
            if (v.getVersionString().equals(name)) {
                return v;
            }
        }
        return INVALID;
    }

    public String getVersionString() {
        return versionString;
    }

    public int getVersion() {
        return version;
    }

    public boolean isAtLeast(BScanVersion other) {
        return getVersion() >= other.getVersion();
    }
}
