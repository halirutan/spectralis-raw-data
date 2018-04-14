package de.halirutan.spectralis.filestructure;

/**
 * Provides names of the available retinal layers. Tests have shown that up to 17 layers are provided. Unfortunately,
 * the manual only describes 14 of them. This is the reason for the last unknown entries.
 * (c) Patrick Scheibe 2018
 */
public enum LayerNames {
    ILM(0), RPE(1), NFL(2), GCL(3), IPL(4), INL(5), OPL(6), ONL(7), ELM(8), IOS(9), OPT(10), CHO(11), VIT(12), ANT(13),
    UNKNOWN1(14), UNKNOWN2(15), UNKNOWN3(16), UNKNOWN4(17), UNKNOWN5(18);
    private final int layerNum;

    LayerNames(int num) {
        layerNum = num;
    }

    public int getLayerNum() {
        return layerNum;
    }
}
