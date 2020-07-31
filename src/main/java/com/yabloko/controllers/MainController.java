package com.yabloko.controllers;


import com.yabloko.models.Message;
import com.yabloko.models.User;
import com.yabloko.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static com.yabloko.controllers.ControllerUtils.getStringStringMap;
import static com.yabloko.controllers.ControllerUtils.saveFile;


@Controller
public class MainController {
    @Autowired
    MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String hello(
            @RequestParam(name = "name", required = false, defaultValue = "NONAME") String parameter,
            Model model) //  вместо Model можно использовать простую MAP ( model.put )
    {
        model.addAttribute("name", parameter); // имя параметра передаваемого МОДЕЛИ
        return "hello"; // имя мусташ
    }

    @GetMapping("/main")
    public String getMain(Model model,
                          @RequestParam(required = false) String filter,
                          @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC)
                          Pageable pageable,
                          @RequestParam(required = false, defaultValue = "10") String size
                          //количество отображаемых сообщений на странице
                          // - чтобы не терять при переходе на страницы
                          ) {
        return checkFilter(model, filter, pageable, size);
    }

    private String checkFilter(Model model,
            /*@RequestParam(required = false)*/ String filter,
                               Pageable pageable,
                               String size
    ) {
        if (filter != null && !filter.equals("")) {
            model.addAttribute("page", messageRepo.findByTag(filter, pageable));
            model.addAttribute("filter", filter);
            return "main";
        }
//        Page page = new P
        model.addAttribute("page", messageRepo.findAll(pageable)); // Iterable - не List !!!
        model.addAttribute("url", "/main"); // для пагинатора
        model.addAttribute("size", size); // для пагинатора - значение количества страниц

        return "main";
    }

    // СОХРАНЕНИЕ сообщения с главной страницы
    @PostMapping("/main")
    public String addMessage(
            @AuthenticationPrincipal User user, // ЮЗЕР берется из аутентификации
            @Valid Message message, // получаем из формы + ВАЛИДАЦИЯ
            // при выполнении ВАЛИДАЦИИ нажно получать и список ошибок ! =
            BindingResult bindingResult, // он должен идти сразу до MODEL
            Model model,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String filter,
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC)
                    Pageable pageable,
            @RequestParam(required = false, defaultValue = "10") String size
            //количество отображаемых сообщений на странице
            // - чтобы не терять при переходе на страницы

    ) throws IOException {
//        if (message.getText().equals("") || message.getTag().equals(""))
//            return "redirect:/mess";
        message.setAuthor(user);

        // ОШИБКИ ВАЛИДАЦИИ - НЕ СОХРАНЯЕМ СМС - ВЕРНЕМ main НО с ошибками и введенным СМС
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = getStringStringMap(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
            // БЕЗ ОШИБОК - СОХРАНЯЕМ В БД MESSAGE
        } else {
            // ЛОГИКА РАБОТЫ С ФАЙЛОМ
            saveFile(message, file, uploadPath);

            // очищаем СМС тк валидация прошла
            model.addAttribute("message", null);
            messageRepo.save(message);
        }
//        return "redirect:/mess"; // отказался тк аттрибуты ошибок модели не передаются через редирект! только напрямую в шаблон
        return checkFilter( model, filter, pageable, size );
    }

//    private void saveFile(@Valid Message message, @RequestParam("file") MultipartFile file) throws IOException {
//        if (!file.getOriginalFilename().isEmpty()) {
//            File uploadFolder = new File(uploadPath); // это не файл - это ДИРРЕКТОРИЯ !!!
//
//            if (!uploadFolder.exists()) {
//                uploadFolder.mkdir(); // создаем дирректорию - если ее не существует
//            }
//
//            // для предотвращения коллизий - создаем уникальное имя файла
//            String uuidFile = UUID.randomUUID().toString();
//
//            // + изначальное имя
//            String resultFilename = uuidFile + "." + file.getOriginalFilename();
//
//            //  Transfer the received file to the given destination file.
//            file.transferTo(new File(uploadPath + "/" + resultFilename)); // по идее на этом этапе файл создается у нас
//
//            message.setFilename(resultFilename);
//        }
//    }



    // заглушки
    @GetMapping("/filter")
    public String showBlockFilter() {
        return "block";
    }

//    @GetMapping("/mess") // отказался тк аттрибуты ошибок модели не передаются через редирект! только напрямую в шаблон
//    public String showMessage(Model model,
//                              @RequestParam(required = false) String filter) {
//
//        return checkFilter(model, filter);
//    }
}
