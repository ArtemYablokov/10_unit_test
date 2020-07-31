package com.yabloko.config;

import com.yabloko.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // для @PreAuthorize("hasAnyAuthority('ADMIN')")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/registration", "/static/**", "/activate/*").permitAll()
                    .anyRequest()
                .authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")// задаем форму логина - логика Spring
//                .defaultSuccessUrl("/")
                    .permitAll() // разрешаем всем на ЛОГИН
                .and()
                // для запоминания СЕССИИ ЮЗЕРА
                    .rememberMe()
                .and()
                    .logout()
                    .permitAll();
//        http.csrf().disable(); //
    }



    // проверка пользователя при LOGIN
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder); // NoOpPasswordEncoder.getInstance()

        // проверка ЛОГИНА юзера до добавления UserService
        // затем эти методы реализуем через UserService :
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .passwordEncoder(NoOpPasswordEncoder.getInstance())
//                .usersByUsernameQuery("select username, password, active from usr where username=?")
                // эти три поля определены СИСТЕМОЙ !!!
//                .authoritiesByUsernameQuery("select u.username, ur.roles from " +
//                        "usr u inner join user_role ur " + // из этих таблиц, объединенных
//                        "on u.id = ur.user_id " + // через поля
//                        "where u.username=?");
                // получаем список пользователей с их ролями
    }
    // блок запускается для создания первого юзера
//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("p")
//                        .password("p")
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
}
