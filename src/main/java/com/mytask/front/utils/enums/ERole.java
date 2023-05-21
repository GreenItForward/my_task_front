package com.mytask.front.utils.enums;

public enum ERole {
    ADMINISTRATEUR,
    MEMBRE;

    private final String value;

    ERole() {
        this.value = this.name();
    }
    ERole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ERole findByName(String name) {
        for (ERole eRole : ERole.values()) {
            if (eRole.name().equals(name)) {
                return eRole;
            }
        }
        return null;
    }
}
