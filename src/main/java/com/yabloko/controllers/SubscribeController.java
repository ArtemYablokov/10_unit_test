package com.yabloko.controllers;

import com.yabloko.models.User;
import com.yabloko.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
public class SubscribeController {

    @Autowired
    UserService userService;

    @GetMapping("/subs/subscribe/{userChannel.id}")
    public String subscribe(
            @PathVariable(name = "userChannel.id") User userChannel,
            @AuthenticationPrincipal User currentUser
    ){
        userService.subscribe(userChannel, currentUser);
        return "redirect:/user-messages/" + userChannel.getId();
    }

    @GetMapping("/subs/unsubscribe/{userChannel.id}")
    public String unSubscribe(
            @PathVariable(name = "userChannel.id")User userChannel,
            @AuthenticationPrincipal User currentUser
    ){
        userService.unsubscribe(userChannel, currentUser);
        return "redirect:/user-messages/" + userChannel.getId();
    }

    @GetMapping("/subs/{pathVar}/{userChannel.id}/list")
    public String showSubs(
            Model model,
            @PathVariable(name = "userChannel.id")User userChannel,
            @PathVariable(name = "pathVar") String pathVar
    ){
        int i = 0;
        Set<User> subs;
        if (pathVar.equals("subscribers")) // or subscriptions
            subs = userChannel.getSubscribers();
        else
            subs = userChannel.getSubscriptions();

        model.addAttribute("users", subs);
        model.addAttribute("type", pathVar);
        model.addAttribute("userChannel", userChannel);

        return "subscriptions";
    }
}
