package com.yabloko.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority { // реализация нужна для USERDETAILS
    USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
