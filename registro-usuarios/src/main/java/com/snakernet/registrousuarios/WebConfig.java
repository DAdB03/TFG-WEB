package com.snakernet.registrousuarios;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración web para la aplicación.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configura las políticas de CORS para la aplicación.
     *
     * @param registry el registro de configuraciones CORS
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*")
            .allowCredentials(true);
    }

    /**
     * Configura el manejo de recursos estáticos.
     *
     * @param registry el registro de manejadores de recursos
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Sirve recursos estáticos desde el classpath (presumiblemente desde src/main/resources/static)
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
