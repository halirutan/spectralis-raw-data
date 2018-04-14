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
 * Grid-types as provided by the OCT
 * (c) Patrick Scheibe 2018
 */
public enum GridType {
    NO_GRID(0, "No grid given"),
    CIRCULAR1(1, "Circular grid with diameters 1.00, 2.00, and 3.00 mm"),
    CIRCULAR2(2, "Circular grid with diameters 1.00, 2.22, and 3.45 mm"),
    CIRCULAR_ETDRS(3, "Circular ETDRS grid with diameters 1.00, 3.00, and 6.00 mm"),
    RECTANGULAR_15(8, "Rectangular grid 15deg PMB with 2x5 cells"),
    RECTANGULAR_20(9, "Rectangular grid 20deg PMB with 2x10 cells"),
    RECTANGULAR_POLE(10, "Rectangular grid posterior pole with 8x8 cells");

    private final int myType;
    private final String myDescription;

    GridType(int i, String description) {
        myType = i;
        myDescription = description;
    }

    @SuppressWarnings("unused")
    public int getType() {
        return myType;
    }

    public String getDescription() {
        return myDescription;
    }

    public static GridType getGridType(int typeId) {
        switch (typeId) {
            case 1:
                return CIRCULAR1;
            case 2:
                return CIRCULAR2;
            case 3:
                return CIRCULAR_ETDRS;
            case 8:
                return RECTANGULAR_15;
            case 9:
                return RECTANGULAR_20;
            case 10:
                return RECTANGULAR_POLE;
            default:
                return NO_GRID;
        }
    }

}
