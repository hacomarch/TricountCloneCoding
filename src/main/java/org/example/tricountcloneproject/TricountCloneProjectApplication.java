package org.example.tricountcloneproject;

import org.example.tricountcloneproject.config.JdbcTemplateConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(JdbcTemplateConfig.class)
@SpringBootApplication
public class TricountCloneProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(TricountCloneProjectApplication.class, args);
    }

}
