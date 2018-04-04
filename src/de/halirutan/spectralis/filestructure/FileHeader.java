package de.halirutan.spectralis.filestructure;

import com.sun.istack.internal.Nullable;
import de.halirutan.spectralis.data.DataFragment;
import de.halirutan.spectralis.data.FloatDataFragment;
import de.halirutan.spectralis.data.IntegerDataFragment;
import de.halirutan.spectralis.data.StringDataFragment;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public class FileHeader {

    private static final long VERSION_OFFSET = 0;
    private static final long HEADER_OFFSET = 0;

    private final RandomAccessFile file;
    private final HSFVersion myHSFVersion;
    private final Map<FileHeaderContent, DataFragment> myInfo = new EnumMap<>(FileHeaderContent.class);

    FileHeader(RandomAccessFile inFile) throws IOException {
        file = inFile;
        file.seek(VERSION_OFFSET);
        myHSFVersion = readVersion(file);
        if (myHSFVersion == null) {
            throw new IOException("Invalid file");
        }

        file.readInt();





        for (FileHeaderContent content : FileHeaderContent.values()) {
            if (myHSFVersion.compareTo(content.getVersion()) < 0) break;
            myInfo.put(content, content.readData(this.file));
        }
        int test = FileHeaderContent.GridType.getDataFragment().read(this.file);
    }


    public DataFragment get(FileHeaderContent info) {
        return myInfo.get(info);
    }

    public Integer getIntegerValue(FileHeaderContent content) {
        if (content.getDataFragment() instanceof IntegerDataFragment) {
            return (Integer) myInfo.get(content).getValue();
        }
        return null;
    }

    public Float getFloatValue(FileHeaderContent content) {
        if (content.getDataFragment() instanceof FloatDataFragment) {
            return (Float) myInfo.get(content).getValue();
        }
        return null;
    }


    private static HSFVersion readVersion(RandomAccessFile f) throws IOException {
        long oldPosition = f.getFilePointer();
        f.seek(0);
        HSFVersion result = null;
        StringDataFragment version = new StringDataFragment(12);
        String versionString = version.read(f);
        for (HSFVersion value : HSFVersion.values()) {
            if (value.getVersionString().equals(versionString)) {
                result = value;
                break;
            }
        }
        f.seek(oldPosition);
        return result;
    }

    @Nullable
    private static HSFVersion readVersion(File file) throws IOException {
        HSFVersion version = null;
        if (file.canRead()) {
            RandomAccessFile f = new RandomAccessFile(file, "r");
            version = readVersion(f);
        }
        return version;
    }

    static boolean isValidHSFFile(File file) {
        try {
            return readVersion(file) != null;
        } catch (IOException e) {
            return false;
        }
    }
}
