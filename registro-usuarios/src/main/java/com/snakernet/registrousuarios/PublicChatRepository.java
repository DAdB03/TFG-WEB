package com.snakernet.registrousuarios;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicChatRepository extends JpaRepository<PublicMessage, Long> {
	
}