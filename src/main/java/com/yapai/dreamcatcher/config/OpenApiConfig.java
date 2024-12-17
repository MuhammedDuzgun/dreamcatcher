package com.yapai.dreamcatcher.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI getOpenAPI(@Value("${application-description}") String applicationDescription,
                              @Value("${application-version}") String applicationVersion) {
        return new OpenAPI()
                .info(new Info()
                        .title("Dream Catcher")
                        .version(applicationVersion)
                        .description(applicationDescription));
    }

}
