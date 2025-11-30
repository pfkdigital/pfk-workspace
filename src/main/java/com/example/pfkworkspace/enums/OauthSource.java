package com.example.pfkworkspace.enums;

public enum OauthSource {
    GOOGLE("google"),
    GITHUB("github"),
    UNKNOWN("unknown");

    private final String value;

    OauthSource(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static OauthSource fromValue(String v) {
        if (v == null) return UNKNOWN;
        for (OauthSource s : values()) {
            if (s.value.equalsIgnoreCase(v)) return s;
        }
        return UNKNOWN;
    }
}
