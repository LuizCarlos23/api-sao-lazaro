package org.estacio.enums;
public enum AgeRange {
    YOUNG(0), ADULT(1);

    private final int value;
    private AgeRange(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
