package com.yabloko.controllers;

import com.yabloko.models.Role;
import com.yabloko.models.User;
import com.yabloko.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    // СПИСОК ЮЗЕРОВ
    @PreAuthorize("hasAnyAuthority('ADMIN')") // только АДМИНУ доступен МАПИНГ
    @GetMapping
    public String userList(Model model){
        model.addAttribute("userList", userService.findAll());
        return "userlist";
    }

    // страница редактирования ЮЗЕРА
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("{user}")
    public String editUser( Model model,
            @PathVariable("user") User user){ // имя PathVariable задавать необязательно !
        // переменная USER - в УРЛе - это ID НО!
        // через Spring можно напрямую получать сразу ЮЗЕРА без USERREPO !!!
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "edituser";
    }

    // обработка запроса на редактирование
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping
    public String userSafe(
            @RequestParam("userId") User user, // по параметру ID из формы получаем ЮЗЕРА
            @RequestParam Map<String, String> form, // переменное количество параметров
            @RequestParam String username
                           ){
        userService.adminEditUser(user, form, username);

        return "redirect:/user";
    }

    @GetMapping("/profile")
    public String getProfile(Model model,
                             @AuthenticationPrincipal User user){
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("password", user.getPassword());
        return "profile";
    }

    @PostMapping("/profile")
    public String postProfile(@AuthenticationPrincipal User user,
                              @RequestParam String email,
                              @RequestParam String password,
                              Model model
    ){
        if ( email.equals("") || password.equals("") ){
            model.addAttribute("message", "Fill all feilds !");
            return "redirect:/user/profile";
        }

        userService.updateProfile(user, password, email);
        return "redirect:/user/profile";
    }
}