package com.example.Aura.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry){

        registry.addMapping("/**")//Permitir todos los endPoints
                .allowedOrigins("*")//Permitir cualquier origen (FlutterFlow)
                .allowedMethods("POST","GET", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*");

    }
}
