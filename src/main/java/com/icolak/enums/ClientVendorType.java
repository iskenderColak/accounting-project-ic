package com.icolak.enums;

public enum ClientVendorType {

    CLIENT("Client"), VENDOR("Vendor");

    private final String value;

    ClientVendorType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
