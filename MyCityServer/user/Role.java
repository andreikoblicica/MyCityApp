package com.example.community.user;

public enum Role {
    ADMIN("Admin"), REGULAR_USER("Regular User"), INSTITUTION_USER("Institution User");

    private final String text;

    Role(String text){
        this.text=text;
    }

    public String getText(){
        return this.text;
    }

    public static Role fromString(String text) {
        for (Role role : Role.values()) {
            if (role.text.equalsIgnoreCase(text)) {
                return role;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        return text;
    }
}
