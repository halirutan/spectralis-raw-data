package de.halirutan.spectralis.filestructure;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public enum Version{
    HSF_OCT_100("HSF-OCT-100", 0),
    HSF_OCT_101("HSF-OCT-101", 1),
    HSF_OCT_102("HSF-OCT-102", 2),
    HSF_OCT_103("HSF-OCT-103", 3);

    String myVersionString;
    int myVersionNumber;

    Version(String versionString, int versionNumber) {
        myVersionString = versionString;
        myVersionNumber = versionNumber;
    }

}
