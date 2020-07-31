package com.yabloko.models;


import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO) // IDENTITY
    private Long id;

    @NotBlank(message = "fill the tag field")
    @Length(max = 255, message = "max = 255 (255B)")
    private String tag;

    @NotBlank(message = "fill the text field")
    @Length(max = 2048, message = "max = 2048 (2kB)")
    private String text;

    private String filename; // здесь храним не весь путь - только имя файла, путь - в настройках

    @ManyToOne(fetch = FetchType.EAGER) // МНОГО сообщений для ОДНОГО ЮЗЕРА - СО СТОРОНЫ СООБЩЕНИЙ
    @JoinColumn(name = "user_id") // название колонки по которой объединяюся
    private User author;

    // при добавлении конструктора с аргументами - нужен конструктор по умолчанию !
    public Message() {
    }
    public Message(String text, String tag, User user) {
        this.text = text;
        this.tag = tag;
        this.author = user;
    }

    public String getAuthorName(){
        return author == null ? "none" : author.getUsername();
    }

    public User getAuthor() {
        return author;
    }
    public void setAuthor(User author) {
        this.author = author;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }

}
