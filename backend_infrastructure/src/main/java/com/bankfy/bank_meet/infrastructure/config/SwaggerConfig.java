package com.bankfy.bank_meet.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Bankfy API - Sistema de Gestión Bancaria")
                                                .version("1.0")
                                                .description(
                                                                "API para la gestión de clientes, cuentas y movimientos financieros con generación de reportes en PDF.")
                                                .contact(new Contact()
                                                                .name("Soporte Bankfy")
                                                                .email("dev@bankfy.com")));
        }
}