package com.e7san.bot.entity;

import com.e7san.bot.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoryMessage {

    MessageRepository messageRepository;

    public HistoryMessage(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void saveMessage(String role, String content) {
        Message message = Message.builder()
                .role(role)
                .content(content)
                .time(LocalDateTime.now())
                .build();
        messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}
