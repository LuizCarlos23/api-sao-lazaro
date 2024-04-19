package org.estacio.enums;

public enum AnimalSize {
    SMALL(0), MEDIUM(1), LARGE(2);

    private final int value;
    private AnimalSize(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
