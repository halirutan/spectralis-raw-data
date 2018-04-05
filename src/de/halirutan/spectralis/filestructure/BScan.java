package de.halirutan.spectralis.filestructure;

import de.halirutan.spectralis.data.DataFragment;
import de.halirutan.spectralis.data.FloatArrayDataFragment;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by patrick on 11.01.17.
 * (c) Patrick Scheibe 2017
 */
public class BScan {



    private final long myOffset;
    private final int mySize;

    private final float[] myData;

    private EnumMap<BScanHeaderContent, DataFragment> myHeader = null;
    private final Map<String, Float[]> mySegmentations;

    private BScan(EnumMap<BScanHeaderContent, DataFragment> bscanHeader, long offset, int size, float[] data, Map<String, Float[]> segmentations) {
        this.myOffset = offset;
        this.mySize = size;
        this.myData = data;
        this.myHeader = bscanHeader;
        this.mySegmentations = segmentations;
    }

    public static BScan read(RandomAccessFile f, FileHeader fileHeader) throws IOException {
        final long offset = f.getFilePointer();
        EnumMap<BScanHeaderContent, DataFragment> header = new EnumMap<>(BScanHeaderContent.class);
        for (BScanHeaderContent content : BScanHeaderContent.values()) {
            final DataFragment dataFragment = content.readData(f);
            header.put(content, dataFragment);
        }

        // Read available segmentation data
        final Integer offsetSeg = DataFragment.getIntegerValue(header.get(BScanHeaderContent.OffSeg));
        final Integer numSeg = DataFragment.getIntegerValue(header.get(BScanHeaderContent.NumSeg));
        final Integer sizeX = DataFragment.getIntegerValue(fileHeader.get(FileHeaderContent.SizeX));
        f.seek(offset + offsetSeg);
        Map<String , Float[]> segLines = new LinkedHashMap<>(numSeg);
        for (int i = 0; i < numSeg; i++) {
            final FloatArrayDataFragment segData = new FloatArrayDataFragment(sizeX);
            segData.read(f);
            segLines.put(BScanHeaderContent.layerNames[i], segData.getValue());
        }

        // Read BScan content
        final Integer sizeZ = DataFragment.getIntegerValue(fileHeader.get(FileHeaderContent.SizeZ));
        final Integer bScanHdrSize = DataFragment.getIntegerValue(header.get(BScanHeaderContent.BScanHdrSize));

        final ByteBuffer bscanContent = ByteBuffer.allocate(sizeX*sizeZ*DataTypes.Float);
        f.seek(offset + bScanHdrSize);
        f.read(bscanContent.array(),0, sizeX * sizeZ * DataTypes.Float);
        FloatBuffer fb = bscanContent.asFloatBuffer();
        float[] data = new float[fb.limit()];
        fb.get(data);
        return new BScan(header, offset, bScanHdrSize, data, segLines);
    }

    public long getOffset() {
        return myOffset;
    }

    public int getSize() {
        return mySize;
    }

    public float[] getData() {
        return myData;
    }

    public EnumMap<BScanHeaderContent, DataFragment> getHeader() {
        return myHeader;
    }

    public Map<String, Float[]> getSegmentations() {
        return mySegmentations;
    }
}
