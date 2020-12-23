package com.thomasgvd.multirps;

public enum PacketType {
    CONNECTION(0),
    MOVEMENT(1),
    DISCONNECT(2);

    private final int value;

    private PacketType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
