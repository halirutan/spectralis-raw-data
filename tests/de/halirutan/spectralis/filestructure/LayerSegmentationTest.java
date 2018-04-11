package de.halirutan.spectralis.filestructure;

import java.util.Map;

import de.halirutan.spectralis.Util;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by patrick on 11.04.18.
 * (c) Patrick Scheibe 2018
 */
public class LayerSegmentationTest {

    @Test
    public final void testLayer() throws Exception {
        HSFFile hsfFile = new HSFFile(Util.getVolFile(Util.LAYERS));
        LayerSegmentation layerSegmentation = hsfFile.getLayerSegmentation(1);
        Map<LayerNames, RetinalLayer> layers = layerSegmentation.getLayers();
        for (RetinalLayer l : layers.values()) {
            float[] values = l.getLayer();
            Assert.assertNotNull(values);
            StringBuilder builder = new StringBuilder(l.getName().name());
            builder.append(": ");
            for (float value : values) {
                Assert.assertTrue("Layer value not positive", value > 0);
                builder.append(String.format("0x%8s ", Integer.toHexString(Float.floatToIntBits(value)).replace(' ', '0')));
            }
            System.out.println(builder);
            System.out.println(HSFFile.INVALID_FLOAT_VALUE);
        }
    }

}