package com.example.shcool_hogwarts.configuration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HogwartsConfig {

    @Bean
    public GroupedOpenApi getFacultyGroup() {
        return GroupedOpenApi.builder()
                .displayName("Контроллеры для факультета")
                .pathsToMatch("/faculty/**")
                .group("faculty")
                .build();
    }

    @Bean
    public GroupedOpenApi getStudentGroup() {
        return GroupedOpenApi.builder()
                .displayName("Контроллеры для студента")
                .pathsToMatch("/student/**")
                .group("student")
                .build();
    }

    @Bean
    public GroupedOpenApi getAvatarGroup() {
        return GroupedOpenApi.builder()
                .displayName("Контроллеры для аватара")
                .pathsToMatch("/avatar/**")
                .group("avatar")
                .build();
    }
}
