package org.estacio.enums;

public enum MedicineType {
    Pill(0), Liquid(1), Injectable(2), Spray(3), Ointment(4);

    private final int value;
    private MedicineType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
