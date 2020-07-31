package com.yabloko;

import com.yabloko.controllers.MainController;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

//import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.Matchers.containsString;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// создает структуру классов, которфе авто подменяют слой MVC -> все будет происходить в ФЕЙК-окружении
// в результате не нужно создавать REST-темплейт
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")

// аннотации которые нужны для класса ТЕСТ
@RunWith(SpringRunner.class) // указываем окружение которое стартует наши тесты
@SpringBootTest
public class LoginTest {
    @Autowired
    private MainController controller;
    @Autowired
    private MockMvc mockMvc;

    //аннотация метода ТЕСТ
    @Test
    //простая проверка что Контроллер подтянут
    public void contextTest() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    public void MockMvcTest() throws Exception {
        // Запрос через подмененный MVC-слой
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk()) // обертка над методом assertThat
                .andExpect(content().string(containsString("Description of site ...")));
    }

    @Test
    public void accessDeniedTest() throws Exception {
        this.mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void badCredentials() throws Exception {
        this.mockMvc.perform(post("/login").param("username", "jonh"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void correctLoginTest() throws Exception {
        this.mockMvc.perform(formLogin().user("admin").password("admin"))
//                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
        ;
    }
}