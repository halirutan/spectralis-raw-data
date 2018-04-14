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
