package de.halirutan.spectralis.filestructure;

import java.util.logging.Logger;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public enum BScanVersion {
    HSF_BS_100("HSF-BS-100", 0),
    HSF_BS_101("HSF-BS-101", 1),
    HSF_BS_102("HSF-BS-102", 2),
    HSF_BS_103("HSF-BS-103", 3),
    HSF_BS_104("HSF-BS-104", 4),
    INVALID("Invalid", -1);

    private static final Logger LOG = Logger.getLogger("#de.halirutan.spectralis.filestructure.BScanVersion");

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
