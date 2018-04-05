package de.halirutan.spectralis.filestructure;

/**
 * Created by patrick on 14.02.18.
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
