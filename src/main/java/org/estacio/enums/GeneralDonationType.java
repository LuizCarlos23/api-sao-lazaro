package org.estacio.enums;

public enum GeneralDonationType{
    FOOD(0), PET_FOOD(1), MEDICINE(2), CLEANING_MATERIAL(3);

    private final int value;
    private GeneralDonationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
