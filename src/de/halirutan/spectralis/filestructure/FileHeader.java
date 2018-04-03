package de.halirutan.spectralis.filestructure;

import com.sun.istack.internal.Nullable;
import de.halirutan.spectralis.SpectralisException;
import de.halirutan.spectralis.data.DataFragment;
import de.halirutan.spectralis.data.FloatDataFragment;
import de.halirutan.spectralis.data.IntegerDataFragment;
import de.halirutan.spectralis.data.StringDataFragment;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.EnumMap;

/**
 * Created by patrick on 09.01.17.
 * (c) Patrick Scheibe 2017
 */
public class FileHeader {

    private static final long VERSION_OFFSET = 0;
    private static final long HEADER_OFFSET = 0;

    private RandomAccessFile myRandomAccessFile = null;
    private HSFVersion myHSFVersion = null;
    public final EnumMap<FileHeaderContent, DataFragment> myInfo = new EnumMap<>(FileHeaderContent.class);

    private FileHeader() {    }

    public static FileHeader readHeader(RandomAccessFile f) throws IOException {
        FileHeader h = new FileHeader();
        f.seek(VERSION_OFFSET);
        h.myHSFVersion = readVersion(f);
        h.myRandomAccessFile = f;
        f.seek(HEADER_OFFSET);
        for (FileHeaderContent content : FileHeaderContent.values()) {
            if (h.myHSFVersion.compareTo(content.getVersion()) < 0) break;
            h.myInfo.put(content, content.readData(f));
        }
        return h;
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
        final long oldPosition = f.getFilePointer();
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
    public static HSFVersion readVersion(File file) throws IOException {
        HSFVersion version = null;
        if (file.canRead()) {
            RandomAccessFile f = new RandomAccessFile(file, "r");
            version = readVersion(f);

        }
        return version;
    }

    public static boolean isValidHSFFile(File file) {
        try {
            return readVersion(file) != null;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
