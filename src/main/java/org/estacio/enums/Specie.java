package org.estacio.enums;

public enum Specie {
    DOG(0), CAT(1);

    private final int value;
    private Specie(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
