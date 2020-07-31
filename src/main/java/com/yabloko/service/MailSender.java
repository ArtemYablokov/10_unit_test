package com.yabloko.service;

/*
* КЛАСС ДЛЯ РАССЫЛКИ СООБЩЕНИЙ - содержит единственный метод отправки
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service // Spring создает класс автоматом
public class MailSender {
    @Autowired
    private JavaMailSender mailSender; // в текущем релизе СПРИНГ БИН автоматом не генерируется
    // ! для этого в конфигах создаем БИН - от туда он @Autowired
    // ошибка пропадает при добавлении в пропертиз св-в

    @Value("${spring.mail.username}")
    private String username;

    public void send(String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }




}
