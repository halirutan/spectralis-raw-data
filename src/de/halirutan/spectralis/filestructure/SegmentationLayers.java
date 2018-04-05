package de.halirutan.spectralis.filestructure;

public enum SegmentationLayers {
    ILM(0),
    RPE(1),
    NFL(2),
    GCL(3),
    IPL(4),
    INL(5),
    OPL(6),
    ONL(7),
    ELM(8),
    IOS(9),
    OPT(10),
    CHO(11),
    VIT(12),
    ANT(13);

    private int layerNum;

    SegmentationLayers(int num) {
        layerNum = num;
    }

    public int getLayerNum() {
        return layerNum;
    }
}
