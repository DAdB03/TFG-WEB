package com.snakernet.registrousuarios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublicChatService {

    @Autowired
    private PublicChatRepository messageRepository;

    public PublicMessage saveMessage(String username, String content) {
        PublicMessage message = new PublicMessage();
        message.setUsername(username);
        message.setContent(content);
        return messageRepository.save(message);
    }

    public List<PublicMessage> getAllMessages() {
        return messageRepository.findAll();
    }
}