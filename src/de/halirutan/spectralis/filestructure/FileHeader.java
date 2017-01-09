package de.halirutan.spectralis.filestructure;

import com.sun.istack.internal.Nullable;
import de.halirutan.spectralis.data.StringDataFragment;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public class FileHeader {

    private final File myFile;
    private Version myVersion = null;
    public List<FileHeaderContent> myInfo = new ArrayList<>();

    public FileHeader(File file) throws IOException {
        myFile = file;
        if (myFile.exists() && myFile.canRead() && isValidHSFFile(file)) {
            myVersion = readVersion(file);
            RandomAccessFile f = new RandomAccessFile(file, "r");
            for (FileHeaderContent content : FileHeaderContent.values()) {
                if(myVersion.compareTo(content.version) < 0) break;
                content.readData(f);
                myInfo.add(content);
            }
        }
    }

    @Nullable
    public static Version readVersion(File file) {
        if (file.canRead()) {
            try {
                RandomAccessFile f = new RandomAccessFile(file, "r");
                StringDataFragment version = new StringDataFragment(12);
                String versionString = version.read(f);
                for (Version value : Version.values()) {
                    if (value.myVersionString.equals(versionString)) {
                        return value;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static boolean isValidHSFFile(File file){
        return readVersion(file) != null;
    }
}
