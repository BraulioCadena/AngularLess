package com.mx.collageamor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("https://collagelessx.netlify.app")
            .allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS")
            .allowedHeaders("*");
    }
}
