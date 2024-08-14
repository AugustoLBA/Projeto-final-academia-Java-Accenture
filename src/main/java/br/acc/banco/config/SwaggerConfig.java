package br.acc.banco.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;



@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public org.springdoc.core.models.GroupedOpenApi publicApi() {
        return org.springdoc.core.models.GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/**")
                .addOperationCustomizer((operation, handlerMethod) -> {
                    operation.addParametersItem(new io.swagger.v3.oas.models.parameters.Parameter()
                            .in("header")
                            .name("Authorization")
                            .schema(new io.swagger.v3.oas.models.media.StringSchema())
                            .required(false));
                    return operation;
                })
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Banco - API")
                        .description("API Banco")
                        .version("1.0")
                        .license(new io.swagger.v3.oas.models.info.License().name("").url("")))
                .schemaRequirement("Authorization", new SecurityScheme()
                        .type(Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}