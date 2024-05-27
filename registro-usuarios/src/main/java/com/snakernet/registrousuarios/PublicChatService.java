package com.snakernet.registrousuarios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublicChatService {

    @Autowired
    private PublicChatRepository messageRepository;

    public List<PublicMessage> getAllMessages() {
        return messageRepository.findAll();
    }

    public void saveMessage(String username, String content) {
        PublicMessage message = new PublicMessage();
        message.setUsername(username);
        message.setContent(content);
        message.setHora(LocalDateTime.now());
        messageRepository.save(message);
    }
}