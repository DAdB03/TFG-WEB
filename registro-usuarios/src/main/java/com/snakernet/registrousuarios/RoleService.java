package com.snakernet.registrousuarios;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio para manejar la lógica relacionada con los roles.
 */
@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Obtiene una lista de todos los roles en forma de DTO.
     *
     * @return una lista de RoleDTO que representan todos los roles
     */
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> new RoleDTO(role.getId(), role.getName()))
                .collect(Collectors.toList());
    }
}

