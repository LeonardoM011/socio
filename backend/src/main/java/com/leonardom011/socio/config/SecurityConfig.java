package com.leonardom011.socio.config;

import com.leonardom011.socio.jwt.filter.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    //@Qualifier("delegatedAuthEntryPoint")
    private final AuthenticationEntryPoint delegatedAuthEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/auth/authenticate").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/v1/user/current-user").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/v1/user/{userId}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/user/all").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/v1/events").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/events/{eventId}").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/events/{eventId}").hasAnyAuthority("ORGANIZER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/events/{eventId}").hasAnyAuthority("ORGANIZER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/events/{eventId}").hasAnyAuthority("ORGANIZER")
                .requestMatchers(HttpMethod.GET, "/api/v1/events-user/{userUUID}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/events/{eventId}/image").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/events/{eventId}/image").hasAnyAuthority("ORGANIZER")
                .requestMatchers(HttpMethod.GET, "/api/v1/events-categories").permitAll()

//                .requestMatchers(HttpMethod.GET, "/api/v1/event/liked-by/{userId}").permitAll()
//                .requestMatchers(HttpMethod.GET, "/api/v1/event/liked-for/{eventId}").permitAll()
//                .requestMatchers(HttpMethod.POST, "/api/v1/event/like/{eventId}").authenticated()
//                .requestMatchers(HttpMethod.DELETE, "/api/v1/event/remove-like/{eventId}").authenticated()

                .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                .anyRequest().denyAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(delegatedAuthEntryPoint);
        return http.build();
    }
}
