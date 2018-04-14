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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.istack.internal.Nullable;

/**
 * Provides available file versions.
 * (c) Patrick Scheibe 2017
 */
@SuppressWarnings("unused")
public enum HSFVersion {
    HSF_OCT_100("HSF-OCT-100", 0),
    HSF_OCT_101("HSF-OCT-101", 1),
    HSF_OCT_102("HSF-OCT-102", 2),
    HSF_OCT_103("HSF-OCT-103", 3),
    HSF_OCT_104("HSF-OCT-104", 4),
    INVALID("Invalid HSFVersion", -1);

    private static final Logger LOG = Logger.getLogger("#de.halirutan.spectralis.filestructure.HSFVersion");
    public static final int VERSION_FILE_OFFSET = 0;
    public static final int VERSION_BYTE_SIZE = 12;


    private final String versionString;
    private final int version;

    HSFVersion(String name, int number) {
        versionString = name;
        version = number;
    }

    public static HSFVersion parseVersionString(String name) {
        if(name == null) return INVALID;
        for (HSFVersion v : HSFVersion.values()) {
            if (v.getVersionString().equals(name)) {
                return v;
            }
        }
        return INVALID;
    }

    public static HSFVersion readVersion(RandomAccessFile file) {
        HSFVersion result;
        try {
            file.seek(VERSION_FILE_OFFSET);
            String versionString = Util.readStringTrimmed(file, VERSION_BYTE_SIZE);
            result = parseVersionString(versionString);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Cannot read version string.", e);
            result = INVALID;
        }
        return result;
    }

    @Nullable
    public static HSFVersion readVersion(File file) {
        HSFVersion version = INVALID;
        if (file.canRead()) {
            try {
                RandomAccessFile rFile = new RandomAccessFile(file, "r");
                version = readVersion(rFile);
            } catch (FileNotFoundException e) {
                LOG.log(Level.WARNING, "Cannot open file.", e);
            }
        }
        return version;
    }

    public String getVersionString() {
        return versionString;
    }

    public int getVersion() {
        return version;
    }

    public boolean isAtLeast(HSFVersion other) {
        return getVersion() >= other.getVersion();
    }

}
