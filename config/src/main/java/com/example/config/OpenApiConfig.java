package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("GPTCheck Config API")
                        .version("1.0.0")
                        .description("Configuration server endpoints")
                        .contact(new Contact().name("Your Name").email("you@example.com"))
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")));
    }
}
