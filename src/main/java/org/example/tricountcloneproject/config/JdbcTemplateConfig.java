package org.example.tricountcloneproject.config;

import lombok.RequiredArgsConstructor;
import org.example.tricountcloneproject.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class JdbcTemplateConfig {

    private final DataSource dataSource;

    @Bean
    public MemberRepository memberRepository() {
        return new MemberRepository(dataSource);
    }
}
