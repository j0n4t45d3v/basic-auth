package com.example.basicauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.
                authorizeHttpRequests(auth -> {
                    //rota publica
                    auth.requestMatchers("/api/public").permitAll();
                    //so será possivel acessar essa rotar se for o ADMIN
                    auth.requestMatchers("/api/admin").hasRole("ADMIN");
                    //so será possivel acessar essa rotar se for um USER ou ADMIN
                    auth.requestMatchers("/api/user").hasAnyRole("USER", "ADMIN");
                    auth.anyRequest().authenticated();
                })
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        //cria o usuario
        UserDetails admin = User.builder()
                .username("jonatas")
                .password("admin123")
                .roles("ADMIN")
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password("user123")
                .roles("USER")
                .build();

        List<UserDetails> users = List.of(admin, user);

//        guarda os usuarios
        return new InMemoryUserDetailsManager(users);
    }
}
