package com.devtam.commonbase.constant;

public enum VoucherType {

    FREE_SHIP(0),
    DISCOUNT(1);
    private static final String[] IMAGE_TYPES_NAMING = new String[]{"FREE_SHIP", "DISCOUNT"};
    private final int value;

    private VoucherType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static String getObjectName(int value) throws Exception {
        try {
            if (value < IMAGE_TYPES_NAMING.length) {
                return IMAGE_TYPES_NAMING[value];
            } else {
                throw new Exception(String.format("The value {%s} is not exist", value));
            }
        } catch (Exception e) {
            throw new Exception(String.format("The value {%s} is not exist", value));
        }

    }
}
