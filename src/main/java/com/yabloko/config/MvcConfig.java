package com.yabloko.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
* КОНФИГУРАЦИОННЫЙ файл
* - можно зарегестрировать ВСЕ контроллеры здесь !
*
* */

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;

    @Bean
    RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    public void addViewControllers(ViewControllerRegistry registry) { // ПОМОГАЕТ РЕГИСТРИРОВАТЬ ПРОСТОЙ КОНТРОЛЛЕР
        registry.addViewController("/login").setViewName("login"); // МАПИМ КОНТРОЛЛЕР НА ВЬЮ
    }

    // для загрузки файлов
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**") // обращение к серверу по этому УРЛ перенаправляется
                .addResourceLocations("file://" + uploadPath + "/"); // file:// это протокол
        registry.addResourceHandler("/static/**") // две звезды - раздача всей иерархии
                .addResourceLocations("classpath:/static/"); // classpath - от корня проекта
    }
}