package com.snakernet.registrousuarios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivateChatService {

    @Autowired
    private PrivateMessageRepository privateMessageRepository;

    public List<PrivateMessage> getMessagesBetweenUsers(String fromUser, String toUser) {
        List<PrivateMessage> messages = privateMessageRepository.findByFromUserAndToUser(fromUser, toUser);
        messages.addAll(privateMessageRepository.findByToUserAndFromUser(fromUser, toUser));
        return messages;
    }

    public void saveMessage(String fromUser, String toUser, String content) {
        PrivateMessage message = new PrivateMessage();
        message.setFromUser(fromUser);
        message.setToUser(toUser);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        privateMessageRepository.save(message);
    }
}
