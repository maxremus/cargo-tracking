package org.example.cargotracking.entity;

public enum LoadStatus {

    PENDING("ЧАКАЩ"),
    LOADING("НАТОВАРЕН"),
    DELIVERED("ДОСТАВЕН");


    private final String displayName;

    LoadStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
