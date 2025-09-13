package com.omegleclone.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Allow all endpoints
                .allowedOrigins("http://localhost:4200", "http://192.168.0.203:4200")  // Allow requests from Angular
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)  // Allow credentials (cookies, authorization headers)
                .allowedHeaders("*");  // Allow all headers
    }
}
