package org.estacio.enums;

public enum MonetaryDonationType {
    PIX(0), MONEY(1), CARD(2);

    private final int value;
    private MonetaryDonationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
