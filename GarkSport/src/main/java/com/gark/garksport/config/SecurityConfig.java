package com.gark.garksport.config;

import com.gark.garksport.modal.User;
import com.gark.garksport.modal.enums.Permission;
import com.gark.garksport.modal.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.http.HttpMethod.*;
import static com.gark.garksport.modal.enums.Permission.*;
import static com.gark.garksport.modal.enums.Role.ADMIN;
import static com.gark.garksport.modal.enums.Role.MANAGER;
import static org.springframework.http.HttpMethod.DELETE;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/auth/**").permitAll()

                .requestMatchers("/admin/**")
                .hasRole(ADMIN.name())

                .requestMatchers("/manager/**")
                .hasAnyRole(ADMIN.name(), MANAGER.name())

//                .requestMatchers(GET, "/user/**").hasAuthority(ADMIN_READ.getPermission())
//                .requestMatchers(POST, "/user/**").hasAuthority(ADMIN_CREATE.getPermission())
//                .requestMatchers(PUT, "/user/**").hasAuthority(ADMIN_UPDATE.getPermission())
//                .requestMatchers(DELETE, "/user/**").hasAuthority(ADMIN_DELETE.getPermission())

                .requestMatchers(GET, "/random/**").hasAuthority(READ.getPermission())
                .requestMatchers(POST, "/random/**").hasAuthority(CREATE.getPermission())
                .requestMatchers(PUT, "/random/**").hasAuthority(UPDATE.getPermission())
                .requestMatchers(DELETE, "/random/**").hasAuthority(Permission.DELETE.getPermission())

                .requestMatchers(GET, "/academie/**").hasAuthority(READ.getPermission())
                .requestMatchers(POST, "/academie/**").hasAuthority(CREATE.getPermission())
                .requestMatchers(PUT, "/academie/**").hasAuthority(UPDATE.getPermission())
                .requestMatchers(DELETE, "/academie/**").hasAuthority(Permission.DELETE.getPermission())

//                .requestMatchers(GET, "/discipline/**").hasAuthority(ADMIN_READ.getPermission())
//                .requestMatchers(POST, "/discipline/**").hasAuthority(ADMIN_CREATE.getPermission())
//                .requestMatchers(PUT, "/discipline/**").hasAuthority(ADMIN_UPDATE.getPermission())
//                .requestMatchers(DELETE, "/discipline/**").hasAuthority(ADMIN_DELETE.getPermission())
//
//                .requestMatchers(GET, "/staff/hello").hasAnyAuthority(STAFF_READ.getPermission(), ADMIN_READ.getPermission())
//                .requestMatchers(GET, "/staff/**").hasAnyAuthority(STAFF_SECOND_READ.getPermission(), ADMIN_READ.getPermission())
//                .requestMatchers(POST, "/staff/**").hasAnyAuthority(STAFF_CREATE.getPermission(), ADMIN_CREATE.getPermission())
//                .requestMatchers(PUT, "/staff/**").hasAnyAuthority(STAFF_UPDATE.getPermission(), ADMIN_UPDATE.getPermission())
//                .requestMatchers(DELETE, "/staff/**").hasAnyAuthority(STAFF_DELETE.getPermission(), ADMIN_DELETE.getPermission())

                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();

    }
}