package com.luv2code.springboot.cruddemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsManager()
    {
        UserDetails John = User.builder()
                            .username("John")
                            .password("{noop}test123")
                            .roles("Employee")
                            .build();
        UserDetails mary = User.builder()
                .username("mary")
                .password("{noop}test123")
                .roles("Employee","Manager")
                .build();

        UserDetails Susan = User.builder()
                .username("Susan")
                .password("{noop}test123")
                .roles("Employee","Manager","admin")
                .build();
        return new InMemoryUserDetailsManager(John,mary,Susan);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "/api/employees").hasRole("Employee")
                        .requestMatchers(HttpMethod.GET, "/api/employees/**").hasRole("Employee")
                        .requestMatchers(HttpMethod.POST, "/api/employees").hasRole("Manager")
                        .requestMatchers(HttpMethod.PUT, "/api/employees").hasRole("Manager")
                        .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasRole("admin")


        );

        //Use HTTP basic Authentication
        http.httpBasic(Customizer.withDefaults());
        //diable csrf generaly not used for stateless REST APIs GET POST PUT DELETE OR PATCH
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

}
