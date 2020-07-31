package com.yabloko.controllers;

import com.yabloko.models.Message;
import com.yabloko.models.User;
import com.yabloko.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static com.yabloko.controllers.ControllerUtils.saveFile;

@Controller
public class MessageController {

    @Autowired
    private MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadPath;

    // отображение всех сообщений ЮЗЕРА
    @GetMapping("/user-messages/{currentUserId}")
    public String getMessage(@AuthenticationPrincipal User currentUser,
                             @PathVariable("currentUserId") User user, // получаем юзера через его ID и все  !!!
                             Model model,
                             @RequestParam(required = false, name = "message") Message message // по параметру получаем ОБЪЕКТ
    ) {
        model.addAttribute("isCurrentUser", currentUser.equals(user)); // реализован EQUALS только через ID
        model.addAttribute("listOfMessages", user.getMessages());

        // если нет параметра message -> это не запрос на редактирование -> не отображаем панель редактирования
        model.addAttribute("message", message);

        model.addAttribute("userChannel", user); // чья страница
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());

        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));

        return "userMessage";
    }

    // запрос на РЕДАКТИРОВАНИЕ сообщения БЕЗ добавление нового !!!
    // УРЛ : /user-messages/${message.author.id}?message=${message.id} ИГНОРИРУЕМ все что после currentUserId
    @PostMapping("/user-messages/{currentUserId}")
    public String editMessage(
            @PathVariable("currentUserId") Long user, // получаем только ID !!!
            @AuthenticationPrincipal User currentUser,
            @RequestParam("id") Message message, // передаем через скрытый параметр - бред какой то МЕССЕДЖ просто из формы берется
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
//        if (message.getAuthor().equals(currentUser)) {
            if (!StringUtils.isEmpty(text)) {
                message.setText(text);
            }
            if (!StringUtils.isEmpty(tag)) {
                message.setTag(tag);
            }
            // логика сохранения файла полностью повторяется !!!
            saveFile(message, file, uploadPath);

            messageRepo.save(message);
//        }
        return "redirect:/user-messages/" + user; // ID
    }

}