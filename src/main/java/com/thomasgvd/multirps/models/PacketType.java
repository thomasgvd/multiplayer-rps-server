package com.thomasgvd.multirps.models;

public enum PacketType {
    CONNECTION(0),
    MOVEMENT(1);

    private final int value;

    private PacketType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
