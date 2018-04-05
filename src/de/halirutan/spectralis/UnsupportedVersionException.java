package de.halirutan.spectralis;

import de.halirutan.spectralis.filestructure.HSFVersion;

public class UnsupportedVersionException extends SpectralisException {
    public UnsupportedVersionException(HSFVersion shouldBe) {
        super("Requires at least " + shouldBe);
    }
}
