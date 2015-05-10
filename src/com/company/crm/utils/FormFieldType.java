package com.company.crm.utils;

public enum FormFieldType {
    NAME("name"),
    LOCATION("location"),
    NOTES("notes"),
    CONTRACT_DATE("contractDate"),
    LOGO_FILE("logoFile");

    private final String name;

    private FormFieldType(final String text) {
        this.name = text;
    }

    @Override
    public String toString() {
        return name;
    }
}
