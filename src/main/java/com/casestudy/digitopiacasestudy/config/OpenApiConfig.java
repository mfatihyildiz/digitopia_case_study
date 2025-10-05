package com.casestudy.digitopiacasestudy.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Digitopia Backend Case Study API")
                        .description("Microservice-based backend for user, organization and invitation management")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Mehmet Fatih Yıldız")
                                .email("example@mail.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .servers(List.of(
                        new Server().url("http://localhost:8080")
                                .description("Local Development Server")));
    }
}
