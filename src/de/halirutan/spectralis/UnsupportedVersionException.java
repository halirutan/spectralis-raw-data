package de.halirutan.spectralis;

import de.halirutan.spectralis.filestructure.BScanVersion;
import de.halirutan.spectralis.filestructure.HSFVersion;

/**
 * Exception when methods try to access information that are only available in later versions.
 */
public class UnsupportedVersionException extends SpectralisException {
    private static final String REQUIRES_AT_LEAST = "Requires at least ";

    public UnsupportedVersionException(HSFVersion shouldBe) {
        super(REQUIRES_AT_LEAST + shouldBe);
    }

    public UnsupportedVersionException(BScanVersion shouldBe) {
        super(REQUIRES_AT_LEAST + shouldBe);
    }
}
