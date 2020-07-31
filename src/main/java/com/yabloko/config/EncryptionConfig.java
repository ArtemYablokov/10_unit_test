package com.yabloko.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class EncryptionConfig {
    @Bean
        // БИН можно создавать в любом КЛАССЕ
        // перенос в отдельный класс для избежания ошибки при деплое на сервер
    PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(8);
    }
}
