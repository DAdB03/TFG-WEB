package com.snakernet.registrousuarios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoUsuarioRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findByUsuarioId(Long usuarioId);
}
