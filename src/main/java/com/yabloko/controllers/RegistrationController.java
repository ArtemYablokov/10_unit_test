package com.yabloko.controllers;

import com.yabloko.models.User;
import com.yabloko.models.dto.CaptchaResponseDtoNew;
import com.yabloko.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

import static com.yabloko.controllers.ControllerUtils.getStringStringMap;

@Controller
public class RegistrationController {

    @Value("${recaptcha.secret}")
    String secret;
    private static final String CAPTCHA_URL =
            "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UserService userService; // переносим сюда всю логику !

    @GetMapping("/registration")
    public String showRegPage() {
        return "registration";
    }

    @PostMapping("/registration")
    public String saveUser(@RequestParam("g-recaptcha-response") String captchaResponse,
                           @RequestParam("passwordConfirm") String passwordConfirm,
                           @Valid User user,
                           BindingResult bindingResult,
                           Model model
                           // Ушло в ВАЛИДАЦИЮ
//                           @RequestParam String username,
//                           @RequestParam String password,
//                           @RequestParam String email
    ) {
        // Ушло в ВАЛИДАЦИЮ
//        if (username.equals("") || password.equals("") || email.equals("")){
//            model.addAttribute("message", "Fill all feilds !");
//            return "registration";
//        }

        String url = String.format(CAPTCHA_URL, secret, captchaResponse);

        // выполнение запроса  получение ответа - ТИП указан последним параметром :
        CaptchaResponseDtoNew response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDtoNew.class);
        // просто пустой лист объектов Collections.emptyList() тк нужен для метода

        if ( !response.isSuccess()){
            model.addAttribute("captchaError", "fill captcha !");
        }

        // ручная валидация пустого поля passwordConfirm
        boolean passwordConfirmIsEmpty = StringUtils.isEmpty(passwordConfirm);
        if (passwordConfirmIsEmpty) {
            model.addAttribute("passwordConfirmError", "fill the passwordConfirm");
        } else {
            model.addAttribute("passwordConfirm", passwordConfirm);
        }
        // ЕСЛИ ОШИБКИ
        if (bindingResult.hasErrors() || passwordConfirmIsEmpty || !response.isSuccess()) { // если каптча не верная = ошибка
            Map<String, String> errorsMap = getStringStringMap(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("user", user); // чтобы заново не писать поля пользователя
            return "registration";
        }
        // если пароли разные
        if (!user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("commonError", "Passwords doesn't matches!");
            model.addAttribute("user", user); // чтобы заново не писать поля пользователя
            return "registration";
        }
        // ЕСЛИ УЖЕ СУЩЕСТВУЕТ ТАКОЙ ЮЗЕР
        if (!userService.saveUser(user)) {
            model.addAttribute("commonError", "User already exists !");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/activate/{activationcode}")
    public String activate(Model model,
                           @PathVariable String activationcode) {

        boolean activate = userService.findByActivationCode(activationcode);
        if (activate) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "activation has been succeed");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "activation has been failed");
        }
        return "login";
    }
}