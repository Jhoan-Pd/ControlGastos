package com.finanzas.controlgastos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Configuración de CORS dinámica: lee orígenes desde application.properties o variable de entorno CORS_ORIGINS
@Configuration
public class CorsConfig {

    @Value("${cors.origins:http://localhost:3001,http://localhost:5173}")
    private String corsOrigins;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        String[] origins = corsOrigins.split(",");
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins(origins)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
