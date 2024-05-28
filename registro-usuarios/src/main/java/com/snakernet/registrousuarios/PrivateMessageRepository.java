package com.snakernet.registrousuarios;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {
    List<PrivateMessage> findByFromUserAndToUser(String fromUser, String toUser);
    List<PrivateMessage> findByToUserAndFromUser(String toUser, String fromUser);
}
