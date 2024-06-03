package com.snakernet.registrousuarios;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador para manejar las solicitudes a la página de inicio.
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    /**
     * Maneja las solicitudes GET a la ruta raíz ("/").
     *
     * @return el nombre del archivo HTML de la página de inicio
     */
    @GetMapping("/")
    public String index() {
        logger.debug("Solicitud GET recibida para la página de inicio");
        return "index.html";
    }
}
