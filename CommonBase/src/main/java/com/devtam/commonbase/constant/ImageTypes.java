package com.devtam.commonbase.constant;

public enum ImageTypes {
    PRODUCT_IMG(0),
    PRODUCT_DETAILS_IMG(1),
    USER_IMG(2),
    CATEGORY_IMG(3);

    private static final String[] IMAGE_TYPES_NAMING = new String[]{"PRODUCT_IMG", "PRODUCT_DETAILS_IMG", "USER_IMG", "CATEGORY_IMG"};
    private final int value;

    private ImageTypes(int value) {
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
