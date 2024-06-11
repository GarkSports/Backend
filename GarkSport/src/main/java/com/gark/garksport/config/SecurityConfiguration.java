package com.gark.garksport.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    SecurityContextHolder.clearContext();

                    // Expire and clear the JWT token cookie
                    ResponseCookie jwtCookie = ResponseCookie.from("accessToken", "")
                            .maxAge(0)
                            .secure(true)  // Sent only over HTTPS
                            .httpOnly(true)  // Not accessible via JavaScript
                            .sameSite("Strict")  // Strict SameSite policy
                            .path("/")  // Allow the entire application to access it
                            .build();

                    // Add the expired Cookie to the HTTP response to clear it on the client side
                    response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

                    response.setStatus(HttpServletResponse.SC_OK);
                })
                .invalidateHttpSession(true);

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("https://spr01.com");
        config.addAllowedOrigin("https://www.spr01.com");
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedOrigin("http://5.196.15.147:4250");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader(HttpHeaders.SET_COOKIE);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
