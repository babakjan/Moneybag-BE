package com.babakjan.moneybag.config;

import com.babakjan.moneybag.authentication.JwtAuthenticationFilter;
import com.babakjan.moneybag.entity.Role;
import com.babakjan.moneybag.error.handler.CustomAccessDeniedHandler;
import com.babakjan.moneybag.error.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true) //enables @Secured annotation
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final AuthenticationProvider authenticationProvider;

    private final String[] noAuthPaths = {
            "/auth/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    private final String[] adminPaths = {
            "/categories/**",
            "/records/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                    .csrf()
                    .disable()
                    .authorizeHttpRequests()
                    .requestMatchers(noAuthPaths)
                    .permitAll()
                    .requestMatchers(adminPaths)
                    .hasAuthority(Role.ADMIN.name())
                    .anyRequest()
                    .authenticated()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //not saves session
                .and()
                    .authenticationProvider(authenticationProvider) //set custom user details service
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling()
                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                    .accessDeniedHandler(new CustomAccessDeniedHandler());

        return httpSecurity.build();
    }
}
