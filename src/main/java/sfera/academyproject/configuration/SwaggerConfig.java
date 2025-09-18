package sfera.academyproject.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {
    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Academy API")
                        .description("Bu API hujjatlari loyihangiz uchun Swagger orqali avtomatik yaratiladi.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Quvonchbek Bobomurodov")
                                .email("quvonchbekbobomurodov9@gmail.com")
                                .url("https://sferaacademy.uz"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")))
                .servers(Arrays.asList(
                        new Server().url("http://167.86.121.42:8080").description("Production server"),
                        new Server().url("http://localhost:8080").description("Local server")
                ))
                .externalDocs(new ExternalDocumentation()
                        .description("Full Documentation")
                        .url("https://your-docs-link.com"))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}
