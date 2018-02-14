package de.halirutan.spectralis.data;

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

    private int myType;
    private String myDescription;

    GridType(int i, final String description) {
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
                return GridType.CIRCULAR1;
            case 2:
                return GridType.CIRCULAR2;
            case 3:
                return GridType.CIRCULAR_ETDRS;
            case 8:
                return GridType.RECTANGULAR_15;
            case 9:
                return GridType.RECTANGULAR_20;
            case 10:
                return GridType.RECTANGULAR_POLE;
            default:
                return GridType.NO_GRID;
        }
    }

}
