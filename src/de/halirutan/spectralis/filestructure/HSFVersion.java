package de.halirutan.spectralis.filestructure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.istack.internal.Nullable;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public enum HSFVersion {
    HSF_OCT_100("HSF-OCT-100", 0),
    HSF_OCT_101("HSF-OCT-101", 1),
    HSF_OCT_102("HSF-OCT-102", 2),
    HSF_OCT_103("HSF-OCT-103", 3),
    HSF_OCT_104("HSF-OCT-104", 4),
    INVALID("Invalid", -1);

    private static final Logger LOG = Logger.getLogger("#de.halirutan.spectralis.filestructure.HSFVersion");

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
            file.seek(0);
            String version = Util.readString(file, 12);
            result = parseVersionString(version.trim());
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
                RandomAccessFile f = new RandomAccessFile(file, "r");
                version = readVersion(f);
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
        return version >= other.version;
    }


}
