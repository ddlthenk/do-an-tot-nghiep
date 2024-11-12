package com.devtam.commonbase.util;

public enum StateConstant {
    FALSE(0),
    TRUE(1),
    WAITING(2);
    private static final String[] STATE_TYPE_NAMING = new String[]{"FALSE", "TRUE", "WAITING"};
    private final int value;


    private StateConstant(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public String getName() throws Exception {
        return getObjectName(this.value);
    }

    public static String getObjectName(int value) throws Exception {
        try {
            if (value < STATE_TYPE_NAMING.length) {
                return STATE_TYPE_NAMING[value];
            } else {
                throw new Exception(String.format("The value {%s} is not exist", value));
            }
        } catch (Exception e) {
            throw new Exception(String.format("The value {%s} is not exist", value));
        }

    }
}
