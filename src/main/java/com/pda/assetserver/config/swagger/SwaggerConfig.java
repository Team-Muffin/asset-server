package com.pda.assetserver.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("자산 서비스 API")
                .version("1.0")
                .description("유저의 마이데이터를 위한 서비스 입니다")
                .contact(new Contact()
                    .name("김동원")
                    .email("dongwon000103@gmail.com")));
    }
}
