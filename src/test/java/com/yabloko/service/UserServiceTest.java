package com.yabloko.service;

import com.yabloko.models.Role;
import com.yabloko.models.User;
import com.yabloko.repos.UserRepo;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.*;


// так как используем Сервисный слой - нам нужен РАННЕР чтобы Spring подготовил тестовое окружение
// из интеграционного тестирования
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService; // сервис который и будем тестить

    // мокируем объекты с помощью МОКИТО
    @MockBean
    private UserRepo userRepo;
    @MockBean
    private MailSender mailSender;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void saveUser() {
        User user = new User();
        user.setEmail("random@mail.hu");
        boolean isCreated = userService.saveUser(user);

        Assert.assertTrue(isCreated); // как только мы мокируем объекты - тест будет проходить успешно
        // если мы захотим протестировать создание - понадобятся классы MailSender, PasswordEncoder, UserRepo
        // но чтобы протестить только этот метод - можно МОКировать эти объекты - через мокито
        Assert.assertNotNull(user.getActivationCode());
        // что у пользователчя установлена хоть одна роль (используем HAMCREST)
        Assert.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));

        // в мокито можно создавать SPY-объекты и узнавать сколько раз к нему обращались = Mockito.verify

        // что у UserRepo один раз вызывалось сохранение
        Mockito.verify(userRepo, Mockito.times(1)).save(user);

        // что выполнялась отправка сообщения
        Mockito.verify(mailSender, Mockito.times(1)).send(
                ArgumentMatchers.eq(user.getEmail()),
                ArgumentMatchers.eq("Activation code"),
                ArgumentMatchers.contains("Welcome to Farter!")  // ArgumentMatchers.anyString()
        );
    }

    @Test
    public void addUserFailTest() {
        User user = new User();
        user.setUsername("John");

        // эмуляция БД - то есть эмулированная БД возвращает нам пользователя с таким именем
        Mockito.doReturn(new User()) // нужно возвращать нового пользователя
                .when(userRepo) // когда на юзеррепо
                .findByUsername("John"); // вызывается метод с параметрами

        boolean isUserCreated = userService.saveUser(user);
        // следовательно здесь будет TRUE - так пользователь возвращен

        Assert.assertFalse(isUserCreated);

        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(mailSender, Mockito.times(0))
                .send(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );
    }


    // ТЕСТ АКТИВАЦИИ ЮЗЕРА
    @Test
    public void activateUser() {
        User user = new User();
        user.setActivationCode("bingo!");

//        Mockito.doReturn(user) // вернуть пользователя
//                .when(userRepo) // когда на юзеррепо
//                .findByActivationCode("activate"); // вызывается этот метод

        /*
        *  это нужно при выполнении метода
        *  userService.findByActivationCode
        *  там юзеру устанавливается NULL вместо activationCode
        *
        *
        *
User user = userRepo.findByActivationCode(activationcode); // возврат юзера с активационным кодом bingo
if (user != null){
    user.setActivationCode(null); // устанавливается NULL
    userRepo.save(user);
    return true;
}
else
    return false;

        * */

        boolean isUserActivated = userService.findByActivationCode("activate"); // вернется такой


        // бред какой то - возвращается юззер с таким активэйшн кодом - да
        // затем код удаляется  в userService.findByActivationCode - да
        // НО последовательность абсолютно не соблюдена !!!
        Assert.assertTrue(isUserActivated);
        Assert.assertNull(user.getActivationCode());

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }

    @Test
    public void activateUserFailTest() {
        boolean isUserActivated = userService.findByActivationCode("activate me");

        Assert.assertFalse(isUserActivated);

        Mockito.verify(userRepo, Mockito.times(0))
                .save(ArgumentMatchers
                        .any(User.class));
    }
}