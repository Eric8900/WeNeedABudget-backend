package com.example.wnabudgetbackend.config;

import com.example.wnabudgetbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("#{'${frontend.origin.pattern}'.split(',')}")
    private List<String> FRONTEND_PATTERNS;

    @Autowired private OAuth2SuccessHandler oAuth2SuccessHandler;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserRepository userRepo;
    @Autowired private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/oauth2/**").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint))
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2SuccessHandler));

        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        return req -> {
            CorsConfiguration cfg = new CorsConfiguration();
            cfg.setAllowedOriginPatterns(FRONTEND_PATTERNS);
            cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE"));
            cfg.setAllowedHeaders(List.of("Authorization", "Content-Type"));
            cfg.setExposedHeaders(List.of("Authorization"));
            cfg.setAllowCredentials(false);
            cfg.setMaxAge(3600L);
            return cfg;
        };
    }

    @Bean
    public JwtAuthenticationFilter jwtFilter () {
        return new JwtAuthenticationFilter(jwtUtil, userRepo);
    }
}