package com.yabloko.repos;

import com.yabloko.models.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepo extends CrudRepository<Message, Long> {

//    List<Message> findByTag(String tag);

    // для пагинации добавляем аргумент
    // org.springframework.data.domain.Pageable
    Page<Message> findByTag(String tag, Pageable pageable);
    Page<Message> findAll(Pageable pageable);


}
