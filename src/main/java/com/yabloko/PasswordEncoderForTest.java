package com.yabloko;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderForTest implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return new StringBuffer(rawPassword).reverse().toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return new StringBuffer(rawPassword).reverse().toString().equals(encodedPassword);
    }
}
