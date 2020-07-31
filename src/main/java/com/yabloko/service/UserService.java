package com.yabloko.service;

import com.yabloko.models.Role;
import com.yabloko.models.User;
import com.yabloko.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service // = @Component
public class UserService implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    MailSender mailSender;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${hostname}")
    String hostname;

    // этот метод вызывается при логине
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        // ЕСЛИ ПОЛЬЗОВАТЕЛЬ НЕ НАЙДЕН ( НАПРИМЕР ПУСТОЕ ПОЛЕ )
        if (user == null) {
            throw new UsernameNotFoundException("User not found :( "); // ошибку выведем через БУТСТРАП = Bad credentials
        }
        return user;
    }

    public boolean saveUser(User user){
        if (userRepo.findByUsername(user.getUsername()) != null)
            return false;

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(Role.USER)); // возвращает SET - интерфейс !!!
        user.setActive(true);
        user.setActivationCode(UUID.randomUUID().toString()); // генератор рандомного массива из 16 БАЙТОВ

        userRepo.save(user);

        // отправка на почту активатора
        sendMessage(user);

        return true;
    }

    private void sendMessage(User user) {
        if ( !StringUtils.isEmpty(user.getEmail()) ){
            String message = String.format("Hello, %s \n" +
                            "Welcome to Farter!\n" +
                            "To confirm actovation visit: http://%s/activate/%s" ,
                    user.getUsername(),
                    hostname, // хостнейм для деплоя изменяем через пропертиз
                    user.getActivationCode() );
            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public boolean findByActivationCode(String activationcode) {
        User user = userRepo.findByActivationCode(activationcode);

        if (user != null){
            user.setActivationCode(null);
            userRepo.save(user);
            return true;
        }
        else
            return false;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void adminEditUser(User user, Map<String, String> form, String username) {
        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values()) // МАССИВ РОЛЕЙ
                .map(Role::name) // переводим в STRING
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) { // отсекаем из формы все кроме ролей !
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepo.save(user); // тогда операция должна заменить старого юзера (по АЙДИ по идее) !
    }

    public void updateProfile(User user, String password, String email) {
        String oldPassword = user.getPassword();
        String oldEmail = user.getEmail();

        boolean emailIsChanged = !oldEmail.equals(email);

        if ( !oldPassword.equals(password) ){
            user.setPassword(password);
        }
        if ( emailIsChanged ){
            user.setEmail(email);
            user.setActivationCode(UUID.randomUUID().toString());
            sendMessage(user);
        }
        userRepo.save(user);
    }

    public void subscribe(User userChannel, User currentUser) {
        userChannel.getSubscribers().add(currentUser);
        userRepo.save(userChannel); // произойдет замена так как ай ди совпадут ??
    }

    public void unsubscribe(User userChannel, User currentUser) {
        userChannel.getSubscribers().remove(currentUser);
        userRepo.save(userChannel);
    }
}
