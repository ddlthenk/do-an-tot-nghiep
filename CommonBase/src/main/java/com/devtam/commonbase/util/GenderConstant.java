package com.devtam.commonbase.util;

public enum GenderConstant {
    MALES(0),
    FEMALES(1),
    OTHER(2);
    private static final String[] GENDER_TYPE_NAMING = new String[]{"MALES", "FEMALES", "OTHER"};
    private final int value;

    private GenderConstant(int value) {
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
            if (value < GENDER_TYPE_NAMING.length) {
                return GENDER_TYPE_NAMING[value];
            } else {
                throw new Exception(String.format("The value {%s} is not exist", value));
            }
        } catch (Exception e) {
            throw new Exception(String.format("The value {%s} is not exist", value));
        }

    }
}
