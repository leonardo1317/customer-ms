package co.com.customer.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Value("${swagger.api.info.title}")
    private String title;

    @Value("${swagger.api.info.description}")
    private String description;

    @Value("${swagger.api.info.version}")
    private String version;

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title(title)
                .description(description)
                .version(version);
    }
}