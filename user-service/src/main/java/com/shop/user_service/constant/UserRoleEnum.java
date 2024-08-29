package com.shop.user_service.constant;

public enum UserRoleEnum {
    USER(Authority.USER),
    ADMIN(Authority.ADMIN);

    private final String authority;


    public String getAuthority() {
        return authority;
    }

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public static class Authority{
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}