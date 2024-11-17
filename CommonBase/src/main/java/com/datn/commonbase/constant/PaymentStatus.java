package com.datn.commonbase.constant;

public enum PaymentStatus {
    IN_CART(0),
    PAID(1),
    CANCELED(2),
    APPROVED(3),
    WAITING(4),
    SHIPPED(5),
    DONE(6),
    WAITING_REFUND(7);

    private static final String[] NAMING = new String[]{"IN_CART", "PAID", "CANCELED", "APPROVED", "WAITING", "SHIPPED", "DONE", "WAITING_REFUND"};
    private final int value;

    PaymentStatus(int value) {
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
