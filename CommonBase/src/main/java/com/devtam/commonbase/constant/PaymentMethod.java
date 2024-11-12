package com.devtam.commonbase.constant;

public enum PaymentMethod {
    COD(1),
    MOMO(2),
    VNPAY(3);

    private static final String[] NAMING = new String[]{"", "COD", "MOMO", "VNPAY"};
    private final int value;

    private PaymentMethod(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static String getObjectName(int value) throws Exception {
        try {
            if (value < NAMING.length) {
                return NAMING[value];
            } else {
                throw new Exception(String.format("The value {%s} is not exist", value));
            }
        } catch (Exception e) {
            throw new Exception(String.format("The value {%s} is not exist", value));
        }

    }
}
