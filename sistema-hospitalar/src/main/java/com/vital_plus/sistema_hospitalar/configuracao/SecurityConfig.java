package com.vital_plus.sistema_hospitalar.configuracao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; 
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/autenticacao/login").permitAll()
                        .requestMatchers("/pacientes/**").permitAll()
                        .requestMatchers("/profissionais/**").permitAll()
                        .requestMatchers("/administradores/**").permitAll()
                        .requestMatchers("/prontuarios/**").permitAll()
                        .requestMatchers("/receitas/**").permitAll()
                        .requestMatchers("/notificacoes/**").permitAll()
                        .requestMatchers("/internacoes/**").permitAll()
                        .requestMatchers("/leitos/**").permitAll()
                        .requestMatchers("/exames/**").permitAll()
                        .requestMatchers("/gerenciar-consultas/**").permitAll()

                        .anyRequest().authenticated())
                .headers(headers -> headers.frameOptions().sameOrigin())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
