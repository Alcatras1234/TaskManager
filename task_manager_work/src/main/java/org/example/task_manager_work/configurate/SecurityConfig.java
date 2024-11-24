package org.example.task_manager_work.configurate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**") // Указывает, что настройка применяется ко всем запросам
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**", // Swagger UI
                                "/v3/api-docs/**", // OpenAPI спецификация
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        .anyRequest().permitAll()// Доступ без авторизации

                )
                .csrf(AbstractHttpConfigurer::disable) // Отключение CSRF-защиты
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
