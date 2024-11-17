package com.datn.commonbase.util;

public enum RoleConstant {
    ROOT(0),
    USER(1),
    ADMIN(2);
    private static final String[] ROLES_TYPE_NAMING = new String[]{"ROOT", "USER", "ADMIN"};
    private final int value;


    private RoleConstant(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    //    public static RoleConstant getById(long id) throws Exception {
//        int objectType = ShardManagerUtil.getTypeId(id);
//        if (objectType >= 0 && objectType < OBJECT_TYPES.length) {
//            return OBJECT_TYPES[objectType];
//        } else {
//            throw new Exception(String.format("The id {%d} is not exit", id));
//        }
//    }
    public String getName() throws Exception {
        return getObjectName(this.value);
    }

    public static String getObjectName(int value) throws Exception {
        try {
            if (value < ROLES_TYPE_NAMING.length) {
                return ROLES_TYPE_NAMING[value];
            } else {
                throw new Exception(String.format("The value {%s} is not exist", value));
            }
        } catch (Exception e) {
            throw new Exception(String.format("The value {%s} is not exist", value));
        }

    }
}
